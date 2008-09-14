package com.blueskyminds.framework.web.startup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import com.blueskyminds.framework.tools.LoggerTools;

/**
 * Date Started: 13/05/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class StartupListener implements ServletContextListener {

    private static final Log LOG = LogFactory.getLog(StartupListener.class);


    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LoggerTools.configure();
        LOG.info("contextInitialised");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOG.info("contextDestoyed");
    }

}
