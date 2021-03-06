package com.blueskyminds.homebyfive.web.core.startup;

import com.blueskyminds.homebyfive.framework.core.tools.LoggerTools;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

/**
 * Initialises Log4j - reads log4j.xml
 *
 * Date Started: 18/08/2007
 * <p/>
 * History:
 */
public class LoggingInitialiser  implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LoggerTools.configure();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}