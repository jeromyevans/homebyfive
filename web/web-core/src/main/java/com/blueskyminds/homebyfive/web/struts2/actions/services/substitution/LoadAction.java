package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import com.opensymphony.xwork2.ActionSupport;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.SubstitutionsFileReader;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Date Started: 17/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class LoadAction extends ActionSupport {

    private String hostname;
    private File file;
    private String contentType;
    private String filename;

    public String execute() throws Exception {

        SubstitutionsFileReader substitutionsFileReader = new SubstitutionsFileReader();

        List<Substitution> substitutions;
        substitutions = substitutionsFileReader.readCsv(new FileInputStream(file));

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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
