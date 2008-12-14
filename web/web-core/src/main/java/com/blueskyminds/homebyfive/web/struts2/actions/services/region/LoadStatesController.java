package com.blueskyminds.homebyfive.web.struts2.actions.services.region;

import com.opensymphony.xwork2.ActionSupport;
import com.blueskyminds.homebyfive.web.struts2.actions.Results;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.States;
import com.blueskyminds.homebyfive.business.region.service.RegionService;
import com.blueskyminds.homebyfive.business.region.service.StateService;
import com.google.inject.Inject;
import com.wideplay.warp.persist.Transactional;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Date Started: 29/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class LoadStatesController extends LoadSupport<State> {

    private StateService stateService;

    public String index() throws Exception {
        hostname = host(request);
        return Results.INDEX;
    }    

    public String create() throws Exception {

        if (upload != null) {

            List<State> states = States.readCSV(new FileInputStream(upload));

            if (localhost()) {
                performLocalLoad(stateService, states);
            } else {
                RegionClient regionClient = new RegionClient();
                for (State state : states) {
                    if (updateOnly != null && updateOnly) {
                        regionClient.updateState(hostname, state);
                    } else {
                        regionClient.createState(hostname, state);
                    }
                }
            }
            return SUCCESS;
        }
        return INPUT;
    }

    @Inject
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }
}
