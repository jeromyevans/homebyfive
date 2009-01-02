package com.blueskyminds.homebyfive.web.struts2.actions.services.region;

import com.blueskyminds.homebyfive.framework.core.net.RESTfulClient;
import com.blueskyminds.homebyfive.framework.core.net.RemoteClientException;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.google.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.HttpConnectionManager;

/**
 * An implementation of the RegionServiceClient interface for remote creation of regions
 *
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RegionClient extends RESTfulClient<Region> {

    private static final Log LOG = LogFactory.getLog(RegionClient.class);

    private static final String COUNTRY_SERVICE = "/countries";
    private static final String STATE_SERVICE = "/states";
    private static final String SUBURB_SERVICE = "/suburbs";
    private static final String POSTCODE_SERVICE = "/postcodes";

    @Inject
    public RegionClient(HttpConnectionManager connectionManager) {
        super(connectionManager);
    }

    /**
     * Persist a new region
     *
     * @param country
     * @return the Region instance created
     */
    public void createCountry(String hostname, Country country) throws RemoteClientException {
        doPost(hostname+COUNTRY_SERVICE +".xml", country);
    }

    public void updateCountry(String hostname, Country country) throws RemoteClientException {
        doPut(hostname+PathHelper.buildPath(country)+".xml", country);
    }

    public void createState(String hostname, State state) throws RemoteClientException {
        doPost(hostname+state.getParentPath()+STATE_SERVICE+".xml", state);
    }

    public void updateState(String hostname, State state) throws RemoteClientException {
        doPut(hostname+PathHelper.buildPath(state)+".xml", state);
    }

    public void createSuburb(String hostname, Suburb suburb) throws RemoteClientException {
        doPost(hostname+suburb.getParentPath()+SUBURB_SERVICE+".xml", suburb);
    }

    public void updateSuburb(String hostname, Suburb suburb) throws RemoteClientException {
        doPut(hostname+PathHelper.buildPath(suburb)+".xml", suburb);
    }

    public void createPostalCode(String hostname, PostalCode postalCode) throws RemoteClientException {
        doPost(hostname+postalCode.getParentPath()+POSTCODE_SERVICE+".xml", postalCode);
    }

    public void updatePostalCode(String hostname, PostalCode postalCode) throws RemoteClientException {
        doPut(hostname+PathHelper.buildPath(postalCode)+".xml", postalCode);
    }
}
