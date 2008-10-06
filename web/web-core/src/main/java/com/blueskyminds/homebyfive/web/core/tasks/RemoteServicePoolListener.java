package com.blueskyminds.homebyfive.web.core.tasks;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletionService;
import java.util.List;
import java.util.LinkedList;

/**
 * Sets up and shuts down the thread pool/executor service for the remote service invocations.
 *
 * Date Started: 1/08/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RemoteServicePoolListener implements ServletContextListener {



    private final List<CompletionService> completionServices;

    public RemoteServicePoolListener() {
        completionServices = new LinkedList<CompletionService>();
    }

    /**
     * Notification that the web application initialization
     * process is starting.
     * All ServletContextListeners are notified of context
     * initialisation before any filter or servlet in the web
     * application is initialized.
     */
    public void contextInitialized(ServletContextEvent sce) {
//        sce.getServletContext().setAttribute();
//        CompletionServiceBuilder.init();
    }

    /**
     * Notification that the servlet context is about to be shut down. All servlets
     * have been destroy()ed before any ServletContextListeners are notified of context
     * destruction.
     */
    public void contextDestroyed(ServletContextEvent sce) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
