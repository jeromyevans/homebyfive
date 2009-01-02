package com.blueskyminds.homebyfive.web.struts2.actions.services.region;

import com.opensymphony.xwork2.ActionSupport;
import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.service.DuplicateRegionException;
import com.blueskyminds.homebyfive.business.region.service.RegionService;
import com.blueskyminds.homebyfive.business.region.service.RegionServiceI;
import com.blueskyminds.homebyfive.business.region.service.InvalidRegionException;
import com.google.inject.Inject;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * Date Started: 30/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public abstract class LoadSupport<T extends Region> extends ActionSupport implements ServletRequestAware {

    protected String hostname;
    protected File upload;
    protected String uploadContentType;
    protected String uploadFilename;
    protected Boolean updateOnly;
    protected Boolean createOrUpdate = false;
    protected HttpServletRequest request;
    protected RegionClient regionClient;

    protected String host(HttpServletRequest request) {
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    }

    /**
     * Perform the operations on the local database
     * 
     * @param regionService
     * @param regions
     * @throws InvalidRegionException
     */
    protected void performLocalLoad(RegionServiceI regionService, List<T> regions) throws InvalidRegionException {
        // use the local interface
        for (T suburb : regions) {
            if (updateOnly != null && updateOnly) {
                regionService.update(suburb.getPath(), suburb);
            } else {
                try {
                    regionService.create(suburb);
                } catch (DuplicateRegionException e) {
                    if (createOrUpdate) {
                        regionService.update(suburb.getPath(), suburb);
                    }
                }
            }
        }
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

    protected boolean localhost() {
        return StringUtils.contains(hostname, "localhost");
    }

    @Inject
    public void setRegionClient(RegionClient regionClient) {
        this.regionClient = regionClient;
    }
}
