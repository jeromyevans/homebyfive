package com.blueskyminds.homebyfive.web.struts2.actions.services.region;

import com.blueskyminds.homebyfive.web.struts2.actions.Results;
import com.blueskyminds.homebyfive.web.core.client.RemoteClientException;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.States;
import com.blueskyminds.homebyfive.business.region.Suburbs;

import java.util.List;
import java.io.FileInputStream;

/**
 * Date Started: 30/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class LoadSuburbsController extends LoadSupport {

    public String index() throws Exception {
        hostname = host(request);
        return Results.INDEX;
    }

    public String create() throws Exception {

        if (upload != null) {
            List<Suburb> suburbs = Suburbs.readCSV(new FileInputStream(upload));

            RegionClient regionClient = new RegionClient();
            for (Suburb suburb : suburbs) {
                if (updateOnly != null && updateOnly) {
                    regionClient.updateSuburb(hostname, suburb);
                } else {
                    try {
                        regionClient.createSuburb(hostname, suburb);
                    } catch (RemoteClientException e) {
                        if (createOrUpdate) {
                            LOG.info("Received a 400 response.  Attempting an update...");
                            if (e.getStatusCode() == 400) {
                                regionClient.updateSuburb(hostname, suburb);
                            }
                        }
                    }
                }
            }
            return SUCCESS;
        }
        return INPUT;
    }

}
