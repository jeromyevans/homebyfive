package com.blueskyminds.homebyfive.web.core.startup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.naming.*;

/**
 * Startup listener that displays entries in the directory
 *
 * Date Started: 19/08/2007
 * <p/>
 * History:
 */
public class JndiViewer implements ServletContextListener {

    private static final Log LOG = LogFactory.getLog(JndiViewer.class);

    private void list(String path) {
        NamingEnumeration<NameClassPair> list = null;
        try {
            Context context = new InitialContext();
            LOG.info("Listing target context: "+path);
            // lookup all the names in the target context
            list = context.list(path);
        } catch(NamingException e) {
            LOG.error("Couldn't list in context "+path+": "+ e.getMessage());
            e.printStackTrace();
        }

        if (list != null) {
            // iterate through all the names looking for anything implementing the preparable interface
            try {
                while (list.hasMore()) {
                    NameClassPair next = list.next();
                    String className = next.getClassName();
                    String beanName = next.getName();
                    String jndiName;
                    if (next.isRelative()) {
                        LOG.info("relative: name:"+beanName);
                    } else {
                        LOG.info("absolute: name:"+beanName);
                    }
                }
            } catch(NamingException e) {
                LOG.error(e);
            }
        }
    }


    public void contextInitialized(ServletContextEvent servletContextEvent) {
        list("java:comp/env");
        list("java:comp/env/mail");
        list("java:comp/env/mail/");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
