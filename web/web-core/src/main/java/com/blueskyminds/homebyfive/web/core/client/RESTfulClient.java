package com.blueskyminds.homebyfive.web.core.client;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * A specialised HTTP Client for invoking RESTful services
 *
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RESTfulClient<T> {

    private static final Log LOG = LogFactory.getLog(RESTfulClient.class);
    
    /** Initialize the XStream instance for serialization/deserialization */
    public static XStream setupXStream() {
        XStream xStream = new XStream();
//        xStream.setMode(XStream.NO_REFERENCES);
//        xStream.addImplicitCollection(Addresses.class, "list");
        return xStream;
    }

     protected String serialize(T model) {
        XStream xStream = setupXStream();
        return xStream.toXML(model);
    }

    protected T deserialize(String response) {
        XStream xStream = setupXStream();
        return (T) xStream.fromXML(response);
    }

     /**
     * Serialize the bean and issue the request to the remote service
     * @param
     * @return
     */
    protected void doPost(String serviceURI, T model, HttpClient client) throws RemoteClientException {
        EntityEnclosingMethod method;

        method = new PostMethod(serviceURI);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String body = serialize(model);
        try {
//            LOG.info(serviceURI+":\n"+body);
            RequestEntity entity = new StringRequestEntity(body, "application/xml", "ISO-8859-1");
            method.setRequestEntity(entity);

            int result = client.executeMethod(method);

            if (result >= 300) {
                throw new RemoteClientException("Remote server responded with an error status line:"+ method.getStatusLine().toString());
            }
        } catch (HttpException e) {
            throw new RemoteClientException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RemoteClientException(e.getMessage(), e);
        } finally {
            method.releaseConnection();
            stopWatch.stop();
        }
        LOG.info("makeRequest "+serviceURI+" took: "+stopWatch.toString());
    }
}
