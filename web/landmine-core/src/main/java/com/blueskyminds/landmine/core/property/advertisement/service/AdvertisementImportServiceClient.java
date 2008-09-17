package com.blueskyminds.landmine.core.property.advertisement.service;

import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementBean;
import com.blueskyminds.landmine.core.property.advertisement.ejb.AdvertisementServiceException;
import com.google.inject.Inject;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.*;

import java.io.IOException;
import java.util.Properties;

/**
 * A remote http client of the AdvertisementImportService
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
public class AdvertisementImportServiceClient implements AdvertisementImportService {

    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final String HOSTNAME_PROPERTY = "landmine.hostname";

    private String hostname;
    private static final String RESOURCE_NAME = "/importer/advertisement";

    public AdvertisementImportServiceClient(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Reads the remote landmine.hostname from the properties
     *  
     * @param properties
     */
    @Inject
    public AdvertisementImportServiceClient(@LandmineProperties Properties properties) {

        if (properties != null) {
            this.hostname = (String) properties.get(HOSTNAME_PROPERTY);
        }

        if (hostname == null) {
            this.hostname = DEFAULT_HOSTNAME;
        }
    }

    public AdvertisementImportServiceClient() {
    }


    /**
     * Serializes the bean as XML and posts it to the remote service
     * 
     * @param bean
     * @throws AdvertisementServiceException
     */
    public Long importAdvertisementBean(PropertyAdvertisementBean bean) throws AdvertisementServiceException {
        return makeRequest(hostname+RESOURCE_NAME+".xml", false, bean);
    }

    public Long updateAdvertisementBean(Long advertisementId, PropertyAdvertisementBean newBean) throws AdvertisementServiceException {
        return makeRequest(hostname+RESOURCE_NAME+"/"+advertisementId+".xml", true, newBean);
    }

    /**
     * Serialize the been and issue the request to the remote service
     * @param bean
     * @return
     * @throws AdvertisementServiceException
     */
    private Long makeRequest(String serviceURI, boolean put, PropertyAdvertisementBean bean) throws AdvertisementServiceException {
        Long advertisementId = null;
        EntityEnclosingMethod method;
        if (put) {
           method = new PutMethod(serviceURI);
        } else {
           method = new PostMethod(serviceURI);
        }

        XStream xStream = new XStream();

        String body = xStream.toXML(bean);
        try {
            RequestEntity entity = new StringRequestEntity(body, "text/xml", "ISO-8859-1");
            method.setRequestEntity(entity);

            HttpClient client = new HttpClient();
            int result = client.executeMethod(method);

            if (result >= 300) {
                throw new AdvertisementServiceException("Remote server responded with an error status line:"+ method.getStatusLine().toString());
            } else {
                // the id of the advertisement is available in the response header
                Header[] advertisementIds = method.getResponseHeaders("Location");     // todo: this is invalid
                if ((advertisementIds != null) && (advertisementIds.length > 0)) {
                    for (Header header : advertisementIds) {
                        try {
                            advertisementId = Long.parseLong(header.getValue());
                        } catch (NumberFormatException e) {
                            // ignored
                        }
                    }
                }
            }
        } catch (HttpException e) {
            throw new AdvertisementServiceException(e);
        } catch (IOException e) {
            throw new AdvertisementServiceException(e);
        } finally {
            method.releaseConnection();
        }
        return advertisementId;
    }

}
