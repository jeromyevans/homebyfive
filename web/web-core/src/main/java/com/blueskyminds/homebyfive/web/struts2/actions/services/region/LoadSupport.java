package com.blueskyminds.homebyfive.web.struts2.actions.services.region;

import com.opensymphony.xwork2.ActionSupport;
import com.wideplay.warp.persist.Transactional;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Date Started: 30/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public abstract class LoadSupport extends ActionSupport implements ServletRequestAware {

    protected String hostname;
    protected File upload;
    protected String uploadContentType;
    protected String uploadFilename;
    protected Boolean updateOnly;
    protected Boolean createOrUpdate = false;
    protected HttpServletRequest request;

    protected String host(HttpServletRequest request) {
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    }

    public abstract String index() throws Exception;

    @Transactional
    public abstract String create() throws Exception;

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

    public Boolean getUpdateOnly() {
        return updateOnly;
    }

    public void setUpdateOnly(Boolean updateOnly) {
        this.updateOnly = updateOnly;
    }

    public Boolean getCreateOrUpdate() {
        return createOrUpdate;
    }

    public void setCreateOrUpdate(Boolean createOrUpdate) {
        this.createOrUpdate = createOrUpdate;
    }
}
