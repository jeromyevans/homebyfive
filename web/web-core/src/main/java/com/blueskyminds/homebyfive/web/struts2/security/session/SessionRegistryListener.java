package com.blueskyminds.homebyfive.web.struts2.security.session;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

/**
 * Listens for session create and destroy events reported by the container and submits data to the SessionRegistry
 * available from the ServletContext
 *
 * <p/>
 * Date Started: 9/05/2008
 */
public class SessionRegistryListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = (HttpSession) event.getSource();

        ServletContext servletContext = event.getSession().getServletContext();
        addSession(servletContext, session);
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = (HttpSession) event.getSource();

        ServletContext servletContext = event.getSession().getServletContext();
        removeSession(servletContext, session);
    }

    /**
     * Record that a session has been started
     */
    private void addSession(ServletContext servletContext, HttpSession session) {
        SessionRegistry sessionRegistry = sessionRegistry(servletContext);
        if (sessionRegistry != null) {
            sessionRegistry.add(session);
        }
    }

    /**
     * Record that a session has been started
     */
    private void removeSession(ServletContext servletContext, HttpSession session) {
        SessionRegistry sessionRegistry = sessionRegistry(servletContext);
        if (sessionRegistry != null) {
            sessionRegistry.remove(session);
        }
    }

    /**
     * Get the set of current sessions from the ServletContext, placed there by the {@link SessionRegistryInitializer}
     */
    private SessionRegistry sessionRegistry(ServletContext servletContext) {
        return SessionRegistry.getInstance();
        //return (SessionRegistry) servletContext.getAttribute(SessionRegistryInitializer.SESSION_REGISTRY);
    }
}
