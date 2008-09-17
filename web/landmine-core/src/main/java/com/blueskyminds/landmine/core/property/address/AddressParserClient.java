package com.blueskyminds.landmine.core.property.address;

import com.blueskyminds.housepad.core.region.AddressPath;
import com.blueskyminds.housepad.core.region.Addresses;
import com.blueskyminds.enterprise.address.service.AddressProcessingException;
import com.blueskyminds.landmine.core.property.advertisement.service.LandmineProperties;
import com.thoughtworks.xstream.XStream;
import com.google.inject.Inject;

import java.util.*;
import java.util.concurrent.*;
import java.io.IOException;

import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.collections.CollectionUtils;

/**
 * Date Started: 4/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class AddressParserClient implements AddressParser {

    private static final Log LOG = LogFactory.getLog(AddressParserClient.class);

    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final String HOSTNAME_PROPERTY = "addressparser.hostname";

    /** Settings used when creating an Executor */
    private static final int CORE_POOL_SIZE     = 5;
    private static final int MAXIMUM_POOL_SIZE  = 10;
    private static final int KEEP_ALIVE_TIME    = 180;

    /** Process 50 addresses in each package */
    private static final int DEFAULT_PACKAGE_SIZE = 50;

    private static final String RESOURCE_NAME = "/address";

    private String[] hosts;

    /** The BlockingQueue is used to ensure we don't send multiple requests to the same host at the same time */
    private final BlockingQueue<String> availableHosts;

    /** Hosts that were attempted but failed */
    //private final List<String> staleHosts;


    public AddressParserClient(String hostname) {
        hosts = new String[]{hostname};
        //staleHosts = new LinkedList<String>();
        availableHosts = new LinkedBlockingQueue<String>(1);
        availableHosts.add(hostname);
    }

    /**
     * Reads the remote landmine.hostname from the properties
     *
     * @param properties
     */
    @Inject
    public AddressParserClient(@LandmineProperties Properties properties) {

        String hostname = null;

        if (properties != null) {
            hostname = (String) properties.get(HOSTNAME_PROPERTY);
        }

        if (hostname == null) {
            hostname = DEFAULT_HOSTNAME;
        }

        hosts = StringUtils.stripAll(StringUtils.split(hostname, ","));
        //staleHosts = new LinkedList<String>();

        availableHosts = new LinkedBlockingQueue<String>(hosts.length);
        availableHosts.addAll(Arrays.asList(hosts));
    }

    /**
     * Parse a list of raw addresses into well-formed address paths
     * The result always has the same order and length as the input
     **/
    public List<AddressPath> parseAddresses(List<AddressPath> rawAddresses) throws AddressProcessingException {

        int maxPoolSize = hosts.length; 

        /** The blocking queue of work used by the executor */
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(Math.min(CORE_POOL_SIZE, maxPoolSize), maxPoolSize, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);

        CompletionService<Addresses> ecs = new ExecutorCompletionService<Addresses>(executor);

        // divide the work into packages and submit them to the completion service
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpClient client = new HttpClient(connectionManager);
        client.getParams().setParameter("http.socket.timeout", 0);
        client.getParams().setParameter("http.connection.timeout", new Integer(500));
        
        int packageSize = DEFAULT_PACKAGE_SIZE;
        boolean allPackaged = false;
        int start = 0;
        int end = start + packageSize;
        int packagesSubmitted = 0;
        while (!allPackaged) {
            if (end >= rawAddresses.size()) {
                end = rawAddresses.size();
                allPackaged = true;
            }
            List<AddressPath> subList = new ArrayList<AddressPath>(rawAddresses.subList(start, end)); // create a new list containing a sublist
            Addresses rawAddressPackage = new Addresses(packagesSubmitted, subList);
            LOG.info("Submitting work package "+packagesSubmitted+" to worker");
            ecs.submit(new Worker(client, rawAddressPackage));
            packagesSubmitted++;
            start += packageSize;
            end += packageSize;
            if (start >= rawAddresses.size()) {
                allPackaged = true;
            }           
        }

        LOG.info("Submitted "+packagesSubmitted+" work packages for "+rawAddresses.size()+" addresses. Waiting for responses...");
        List<AddressPath> reassembled;
        try {
            List<Addresses> resultsOutOfSequence = new ArrayList<Addresses>(packagesSubmitted);

            // wait for the results
            int packagesReceived = 0;
            boolean interrupted = false;
            boolean discard = false;
            while ((packagesReceived < packagesSubmitted) && (!interrupted) && (!discard)) {

                Addresses addresses = ecs.take().get();
                resultsOutOfSequence.add(addresses);
                if (addresses == null) {
                    LOG.info("Received a null result from worker.  This entire group will be discarded");
                    discard = true;
                }

                packagesReceived++;
            }


            if (!discard) {
                LOG.info(packagesReceived+" work packages returned");

                // reassemble the results
                reassembled = new ArrayList<AddressPath>(rawAddresses.size());
                for (int sequenceNo = 0; sequenceNo < packagesSubmitted; sequenceNo++) {
                    for (Addresses batch : resultsOutOfSequence) {
                        if (batch != null) {
                            if (batch.getSequenceNo() == sequenceNo) {
                                reassembled.addAll(sequenceNo*packageSize, batch.getList());
                            }
                        }
                    }
                }

                LOG.info(reassembled.size()+" addresses returned");
            } else {
                // nothing to return
                reassembled = rawAddresses;
            }

        } catch (InterruptedException e) {
            LOG.info("Interrupted.  Not returning any new work");
            reassembled = rawAddresses;
        } catch (ExecutionException e) {
            LOG.error("A task failed execution.", e);   // todo: remove task?/abort all?
            throw new AddressProcessingException("Processing failed execution", e);
        } catch (CancellationException e) {
            LOG.warn("A task was cancelled.", e);
            throw new AddressProcessingException("Processing was cancelled", e);
        } finally {
            executor.shutdownNow();
        }

        return reassembled;
    }

    /**
     * Selects a host and sends the work for processing.
     * If the host doesn't respond selects a different host and tries again
     */
    private class Worker implements Callable<Addresses> {

        private HttpClient client;
        private Addresses rawAddresses;
        private Random random;

        private Worker(HttpClient client, Addresses rawAddresses) {
            this.rawAddresses = rawAddresses;
            random = new Random();
            this.client = client;
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        public Addresses call() throws Exception {

            Addresses result = null;
            String hostName;

            // take a host
            hostName = availableHosts.take();

            int attemptNo = 0;
            while (result == null && attemptNo < hosts.length) {
                try {
                    LOG.info("Worker "+rawAddresses.getSequenceNo()+" makeRequest to "+hostName);
                    result = makeRequest(hostName+RESOURCE_NAME+"/parse.xml", rawAddresses, client);
                    // release the host by putting it back on the queue
                    availableHosts.put(hostName);
                } catch (AddressProcessingException e) {        
                    // indicates that the request failed, possibly because the host is down.  We can swallow this and try a different host

                    if (hosts.length > 1) {
                        LOG.info("makeRequest "+hostName+" aborted due to: "+e.getMessage()+".  Trying a different host when available");
                        // remember the host attempted - we'll release it, but not before getting a different one to use
                        String lastHost = hostName;
                        attemptNo++;

                        // select a different host
                        hostName = availableHosts.take();

                        // return the last host for someone else (if available)
                        availableHosts.put(lastHost);
                    } else {
                        // try the same host in a moment
                        LOG.info("makeRequest "+hostName+" aborted due to: "+e.getMessage()+".  Trying again in a moment if available");

                        // return the host for someone else (if available)
                        availableHosts.put(hostName);
                        attemptNo++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            // ignore interruption
                        }

                        // ask for it back
                        hostName = availableHosts.take();
                    }
                }
            }

            if (result == null) {
                LOG.info("Worker is returning null result. Attempts="+attemptNo);
            }
            return result;
        }
    }

    /** Initialize the XStream instance for serialization/deserialization */
    public static XStream setupXStream() {
        XStream xStream = new XStream();
//        xStream.setMode(XStream.NO_REFERENCES);
//        xStream.addImplicitCollection(Addresses.class, "list");
        return xStream;
    }

    /**
     * Serialize the been and issue the request to the remote service
     * @param
     * @return
     * @throws com.blueskyminds.enterprise.address.service.AddressProcessingException
     */
    private Addresses makeRequest(String serviceURI, Addresses rawAddresses, HttpClient client) throws AddressProcessingException {
        EntityEnclosingMethod method;
        Addresses results;

        method = new PutMethod(serviceURI);

        XStream xStream = setupXStream();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String body = xStream.toXML(rawAddresses);
        try {
//            LOG.info(serviceURI+":\n"+body);
            RequestEntity entity = new StringRequestEntity(body, "text/xml", "ISO-8859-1");
            method.setRequestEntity(entity);
            
            int result = client.executeMethod(method);
            
            if (result >= 300) {
                throw new AddressProcessingException("Remote server responded with an error status line:"+ method.getStatusLine().toString());
            } else {
                // deserialize the body
                results = (Addresses) xStream.fromXML(method.getResponseBodyAsString());
            }
        } catch (HttpException e) {
            throw new AddressProcessingException(e.getMessage(), e);
        } catch (IOException e) {
            throw new AddressProcessingException(e.getMessage(), e);
        } finally {
            method.releaseConnection();
            stopWatch.stop();
        }
        LOG.info("makeRequest "+serviceURI+" took: "+stopWatch.toString());
        return results;
    }
}
