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
public class LoadCountriesController extends ActionSupport implements ServletRequestAware {

    private String hostname;
    private File upload;
    private String uploadContentType;
    private String uploadFilename;
    private Boolean updateOnly;

    private HttpServletRequest request;

    private String host(HttpServletRequest request) {
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    }

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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

     public void setUpload(File upload) {
        this.upload = upload;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFilename = uploadFileName;
    }

    public String getUploadFileName() {
        return uploadFilename;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setUpdateOnly(Boolean updateOnly) {
        this.updateOnly = updateOnly;
    }

    public Boolean getUpdateOnly() {
        return updateOnly;
    }
}
