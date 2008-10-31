package com.blueskyminds.homebyfive.web.struts2.actions.services.region;

import com.blueskyminds.homebyfive.web.struts2.actions.Results;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.States;
import com.blueskyminds.homebyfive.business.region.Suburbs;
import com.blueskyminds.homebyfive.business.region.PostalCodes;

import java.util.List;
import java.io.FileInputStream;

/**
 * Date Started: 30/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class LoadPostalCodesController extends LoadSupport {

    public String index() throws Exception {
        hostname = host(request);
        return Results.INDEX;
    }

    public String create() throws Exception {

        if (upload != null) {
            List<PostalCode> postalCodes = PostalCodes.readCSV(new FileInputStream(upload));

            RegionClient regionClient = new RegionClient();
            for (PostalCode postalCode : postalCodes) {
                if (updateOnly != null && updateOnly) {
                    regionClient.updatePostalCode(hostname, postalCode);
                } else {
                    regionClient.createPostalCode(hostname, postalCode);
                }
            }
            return SUCCESS;
        }
        return INPUT;
    }

}