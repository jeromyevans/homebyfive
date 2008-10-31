package com.blueskyminds.homebyfive.web.struts2.actions.services.region;

import com.opensymphony.xwork2.ActionSupport;
import com.blueskyminds.homebyfive.web.struts2.actions.Results;
import com.blueskyminds.homebyfive.web.struts2.actions.services.substitution.SubstitutionClient;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.SubstitutionsFileReader;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.Countries;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Load the set of countries 
 *
 * Date Started: 28/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class LoadCountriesController extends LoadSupport {

    public String index() throws Exception {
        hostname = host(request);
        return Results.INDEX;
    }

    public String create() throws Exception {

        if (upload != null) {
            List<Country> countries = Countries.readCSV(new FileInputStream(upload));

            RegionClient regionClient = new RegionClient();
            for (Country country : countries) {
                if (updateOnly != null && updateOnly) {
                    regionClient.updateCountry(hostname, country);
                } else {
                    regionClient.createCountry(hostname, country);
                }
            }
            return SUCCESS;
        }
        return INPUT;
    }
}
