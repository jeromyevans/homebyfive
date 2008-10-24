package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import com.opensymphony.xwork2.ActionSupport;
import com.blueskyminds.homebyfive.web.struts2.actions.Results;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.SubstitutionsFileReader;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.google.inject.Inject;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Read substitutions from the database and send them to a remote host for synchronization
 *
 * Date Started: 24/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class SynchronizeController extends ActionSupport implements ServletRequestAware {

    private SubstitutionService substitutionService;
    private String hostname;

    private HttpServletRequest request;

    private String host(HttpServletRequest request) {
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    }

    public String index() throws Exception {
        hostname = host(request) +"/services/substitution/substitution.xml";
        return Results.INDEX;
    }

    public String create() throws Exception {              
        List<Substitution> substitutions = substitutionService.listSubstitutions();

        SubstitutionClient substitutionClient = new SubstitutionClient();
        for (Substitution substitution : substitutions) {
            substitutionClient.createSubstitution(hostname, substitution);
        }
        return SUCCESS;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Inject
    public void setSubstitutionService(SubstitutionService substitutionService) {
        this.substitutionService = substitutionService;
    }
}
