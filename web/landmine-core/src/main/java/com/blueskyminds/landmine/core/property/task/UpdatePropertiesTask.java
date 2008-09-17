package com.blueskyminds.landmine.core.property.task;

import com.blueskyminds.framework.tasks.CannotCompleteException;
import com.blueskyminds.framework.tasks.LongIterativeTask;
import com.blueskyminds.framework.memento.XMLMemento;
import com.blueskyminds.landmine.core.property.service.PremiseService;
import com.blueskyminds.landmine.core.property.service.PremiseServiceException;
import com.blueskyminds.housepad.core.property.service.PropertyService;
import com.google.inject.Provider;
import com.google.inject.Inject;
import com.wideplay.warp.persist.WorkManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * A LongIterativeTask that updates all the PropertyBeans from the current Premises
 *
 * Each iteration transfers one page of results.
 *
 * Date Started: 24/06/2008
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class UpdatePropertiesTask extends LongIterativeTask {

    private static final Log LOG = LogFactory.getLog(UpdatePropertiesTask.class);

    public static final String NAME = "UpdatePropertiesTask";

    private final Provider<PremiseService> premiseService;
    private final WorkManager unitOfWork;
    
    private int pageSize;
    private int pageNo = 0;

    @Inject
    public UpdatePropertiesTask(Provider<PremiseService> premiseService, WorkManager unitOfWork) {
        this.premiseService = premiseService;
        this.unitOfWork = unitOfWork;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    protected void prepare() {

    }

    /**
     * Updates the property beans from the premises
     *
     * @return true if completed
     */
    public boolean iterate() throws CannotCompleteException {
        try {
            LOG.info("pageNo:" + pageNo);

            unitOfWork.beginWork();
            premiseService.get().updatePropertyBeans(pageNo, pageSize);
            pageNo++;
        } catch (PremiseServiceException e) {
            LOG.error("Error while updating properties : " + e.getMessage());
            e.printStackTrace();
            throw new CannotCompleteException(e);
        } finally {
            unitOfWork.endWork();
        }

        return false;
    }

    protected Object getStatusModel() {
        return new PageInfo(pageNo, pageSize);
    }

}