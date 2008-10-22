package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import com.opensymphony.xwork2.ActionSupport;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.SubstitutionsFileReader;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.web.struts2.actions.Results;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Date Started: 17/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class LoadController extends ActionSupport {

    private String hostname;
    private File upload;
    private String uploadContentType;
    private String uploadFilename;

    public String index() throws Exception {
        return Results.INDEX;
    }

    public String create() throws Exception {

        SubstitutionsFileReader substitutionsFileReader = new SubstitutionsFileReader();

        if (upload != null) {
            List<Substitution> substitutions;
            substitutions = substitutionsFileReader.readCsv(new FileInputStream(upload));

            SubstitutionClient substitutionClient = new SubstitutionClient();
            for (Substitution substitution : substitutions) {
                substitutionClient.createSubstitution(hostname, substitution);
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
}
