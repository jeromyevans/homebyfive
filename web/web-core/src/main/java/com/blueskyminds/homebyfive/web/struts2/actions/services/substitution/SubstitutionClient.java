package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.business.address.Addresses;
import com.blueskyminds.homebyfive.business.address.service.AddressProcessingException;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Date Started: 17/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SubstitutionClient {

    private static final Log LOG = LogFactory.getLog(SubstitutionClient.class);
    
    /**
     * Persist a new substitution
     *
     * @param substitution
     * @return the Substitution instance created
     */
    public void createSubstitution(String hostname, Substitution substitution) throws SubstitutionClientException {

        HttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
        HttpClient client = new HttpClient(connectionManager);
        client.getParams().setParameter("http.socket.timeout", 0);
        client.getParams().setParameter("http.connection.timeout", new Integer(500));

        makeRequest(hostname, substitution, client);
    }

    /** Initialize the XStream instance for serialization/deserialization */
    public static XStream setupXStream() {
        XStream xStream = new XStream();
//        xStream.setMode(XStream.NO_REFERENCES);
//        xStream.addImplicitCollection(Addresses.class, "list");
        return xStream;
    }

    /**
     * Serialize the bean and issue the request to the remote service
     * @param
     * @return
     * @throws com.blueskyminds.homebyfive.business.address.service.AddressProcessingException
     */
    private Addresses makeRequest(String serviceURI, Substitution substitution, HttpClient client) throws SubstitutionClientException {
        EntityEnclosingMethod method;
        Addresses results;

        method = new PutMethod(serviceURI);


        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String body = serialize(substitution);
        try {
//            LOG.info(serviceURI+":\n"+body);
            RequestEntity entity = new StringRequestEntity(body, "text/xml", "ISO-8859-1");
            method.setRequestEntity(entity);

            int result = client.executeMethod(method);

            if (result >= 300) {
                throw new SubstitutionClientException("Remote server responded with an error status line:"+ method.getStatusLine().toString());
            } else {
                // deserialize the body
                results = deserialize(method.getResponseBodyAsString());
            }
        } catch (HttpException e) {
            throw new SubstitutionClientException(e.getMessage(), e);
        } catch (IOException e) {
            throw new SubstitutionClientException(e.getMessage(), e);
        } finally {
            method.releaseConnection();
            stopWatch.stop();
        }
        LOG.info("makeRequest "+serviceURI+" took: "+stopWatch.toString());
        return results;
    }

    protected String serialize(Substitution substitution) {
        XStream xStream = setupXStream();
        return xStream.toXML(substitution);
    }

    protected Addresses deserialize(String response) {
        XStream xStream = setupXStream();
        return (Addresses) xStream.fromXML(response);
    }
}
