package com.blueskyminds.homebyfive.framework.core.net;

import com.blueskyminds.homebyfive.framework.core.tools.xml.XMLSerializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.*;
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
       
    protected HttpClient setupClient() {
        HttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
        HttpClient client = new HttpClient(connectionManager);
        client.getParams().setParameter("http.socket.timeout", 0);
        client.getParams().setParameter("http.connection.timeout", new Integer(500));
        return client;
    }

     /**
     * Serialize the bean and issue the request to the remote service
     * @param
     * @return
     */
    protected void doPost(String serviceURI, T model) throws RemoteClientException {
        EntityEnclosingMethod method;

        HttpClient client = setupClient();
        method = new PostMethod(serviceURI);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String body = new XMLSerializer<T>().serialize(model);
        try {
            LOG.info(serviceURI+":\n"+body);
            RequestEntity entity = new StringRequestEntity(body, "application/xml", "ISO-8859-1");
            method.setRequestEntity(entity);

            int result = client.executeMethod(method);

            if (result >= 300) {
                throw new RemoteClientException(method);
            }
        } catch (HttpException e) {
            throw new RemoteClientException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RemoteClientException(e.getMessage(), e);
        } finally {
            method.releaseConnection();
            stopWatch.stop();
        }
        LOG.info("doPost "+serviceURI+" took: "+stopWatch.toString());
    }

    /**
     * Serialize the bean and issue the request to the remote service
     * @param
     * @return
     */
    protected void doPut(String serviceURI, T model) throws RemoteClientException {
        EntityEnclosingMethod method;

        HttpClient client = setupClient();
        method = new PutMethod(serviceURI);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String body = new XMLSerializer<T>().serialize(model);
        try {
            LOG.info(serviceURI+":\n"+body);
            RequestEntity entity = new StringRequestEntity(body, "application/xml", "ISO-8859-1");
            method.setRequestEntity(entity);

            int result = client.executeMethod(method);

            if (result >= 300) {
                throw new RemoteClientException(method);
            }
        } catch (HttpException e) {
            throw new RemoteClientException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RemoteClientException(e.getMessage(), e);
        } finally {
            method.releaseConnection();
            stopWatch.stop();
        }
        LOG.info("doPut "+serviceURI+" took: "+stopWatch.toString());
    }

    public int head(String serviceURI) throws RemoteClientException {
        HttpClient client = setupClient();
        HeadMethod method = new HeadMethod(serviceURI);

        try {
            int result = client.executeMethod(method);
            return result;
        } catch (HttpException e) {
            throw new RemoteClientException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RemoteClientException(e.getMessage(), e);
        } finally {
            method.releaseConnection();
        }
    }
}
