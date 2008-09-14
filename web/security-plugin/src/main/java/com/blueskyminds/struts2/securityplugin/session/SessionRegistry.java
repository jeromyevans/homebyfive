package com.blueskyminds.struts2.securityplugin.session;

import com.opensymphony.xwork2.inject.util.ReferenceMap;
import com.opensymphony.xwork2.inject.util.ReferenceType;
import com.google.inject.Inject;

import javax.servlet.http.HttpSession;
import java.util.WeakHashMap;

/**
 * A simple registry of currently known sessions. The registry can be used to manage concurrent logins
 * and invalidate existing sessions.
 *
 *
 * Date Started: 9/05/2008
 */
public class SessionRegistry {

    private static SessionRegistry soleInstance;

    /**
     * Map of current sessions keyed by their id
     * Sessions are referenced only by a WEAK reference
     */
    private ReferenceMap<String, HttpSession> knownSessions;

    public SessionRegistry() {
        this.knownSessions = new ReferenceMap<String, HttpSession>(ReferenceType.STRONG, ReferenceType.WEAK);
    }

    /** Add a new session reference to the registry */
    public synchronized void add(HttpSession session) {
        knownSessions.put(session.getId(), session);
    }

    /** Remove a session reference from the registry */
    public synchronized void remove(HttpSession session) {
        knownSessions.remove(session.getId());
    }

    /** Remove a session reference from the registry */
    public synchronized void remove(String sessionId) {
        knownSessions.remove(sessionId);
    }

    /** Indicates whether a session is known to the registry.
     * The session may be invalid but still registered.
     * This method doesn't access the session object.  */
    public synchronized boolean isKnown(String sessionId) {
        return knownSessions.containsKey(sessionId);
    }

    /**
     * Invalidate the session with the specified id and remove it from the registry.
     * If the session is already invalidated or removed the request is ignored.
     *
     * @param sessionId   unique id of the session
     */
    public synchronized void invalidate(String sessionId) {
        HttpSession session = knownSessions.get(sessionId);
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // session was already invalidated
            } finally {
                remove(sessionId);
            }
        }
    }

    public synchronized static void injectSoleInstance(SessionRegistry soleInstance) {
        SessionRegistry.soleInstance = soleInstance;
    }

    public static SessionRegistry getInstance() {
        return soleInstance;
    }
}
