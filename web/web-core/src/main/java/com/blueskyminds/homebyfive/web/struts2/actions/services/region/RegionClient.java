package com.blueskyminds.homebyfive.web.struts2.actions.services.region;

import com.blueskyminds.homebyfive.web.core.client.RESTfulClient;
import com.blueskyminds.homebyfive.web.core.client.RemoteClientException;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An implementation of the RegionServiceClient interface for remote creation of regions
 *
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RegionClient extends RESTfulClient<Region> {

    private static final Log LOG = LogFactory.getLog(RegionClient.class);

    private static final String COUNTRY_SERVICE = "/country";
    private static final String STATE_SERVICE = "/state";
    private static final String SUBURB_SERVICE = "/suburb";
    private static final String POSTCODE_SERVICE = "/postcode";

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
}
