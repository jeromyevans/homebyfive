package com.blueskyminds.landmine.extractor.web;

import com.blueskyminds.homebyfive.framework.framework.tasks.service.TaskingService;
import com.blueskyminds.homebyfive.framework.framework.tasks.service.TaskingServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
*
 * Creates and shutsdown the asynchronous tasking service
 *
 * Date Started: 9/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TaskingServiceListener implements ServletContextListener {

    private static final Log LOG = LogFactory.getLog(TaskingServiceListener.class);

    private static TaskingService taskingService;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("TaskingService created");
        createInstance();
    }

    private static void createInstance() {
        taskingService = new TaskingServiceImpl();
    }

    public static TaskingService getInstance() {
        if (taskingService == null) {
            createInstance();
        }            
        return taskingService;
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        taskingService.shutdownNow();
        LOG.info("TaskingService shutdown");
    }
}
