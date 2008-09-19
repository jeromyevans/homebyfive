package com.blueskyminds.homebyfive.web.core.session;

/**
 * Registry of all the sessions currently active in this webapp
 *
 * todo: not sure how this behaves in a clustered environment - probably
 * a bad idea to use it
 *
 * Date Started: 8/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;

public class SessionRegistry {

    /**
     * Map of the current sessions. Key is the session id
     */
    private Map<String, HttpSession> currentSessions;

    public SessionRegistry() {
        init();
    }

    /**
     * Initialise the SessionRegistry with default attributes
     */
    private void init() {
        currentSessions = new HashMap<String, HttpSession>();
    }

     /** Record that a session has been started */
    public synchronized void addSession(HttpSession session) {
        currentSessions.put(session.getId(), session);
    }

    /** Record that a session has been ended */
    public synchronized void removeSession(HttpSession session) {
        currentSessions.remove(session.getId());
    }

    /** Indicates whether the session with the specified ID is still valid or not */
    public boolean isValid(String sessionId) {
        return currentSessions.containsKey(sessionId);
    }

    /**
     * Invalidate the session with the specified id
     * @param sessionId
     */
    public void invalidate(String sessionId) {
        HttpSession session = currentSessions.get(sessionId);
        if (session != null) {
            session.invalidate();
        }
    }
}
