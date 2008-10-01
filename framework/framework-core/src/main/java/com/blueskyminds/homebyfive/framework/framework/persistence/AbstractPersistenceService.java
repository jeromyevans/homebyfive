package com.blueskyminds.homebyfive.framework.framework.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.blueskyminds.homebyfive.framework.framework.IdentityRef;
import com.blueskyminds.homebyfive.framework.framework.HasIdentity;
import com.blueskyminds.homebyfive.framework.framework.persistence.query.PersistenceQuery;

import java.util.List;
import java.util.Collection;
import java.util.Properties;

/**
 * Provides some of the default/common implementation of a PersistenceService
 *
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class AbstractPersistenceService implements PersistenceService {

    private static final Log LOG = LogFactory.getLog(AbstractPersistenceService.class);

    /** A map of sessions that have been explicitly opened by threads using the openSession method.
     * The map is used to re-use the session until it's closed
     */
    protected static final ThreadLocal<PersistenceSession> sessionPerThread = new ThreadLocal<PersistenceSession>();

    protected Properties properties;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractPersistenceService with default attributes
     */
    private void init() {
    }

    /** Determines if the current thread has a long session open already. */
    protected abstract boolean hasSession();

    /**
     * Creates a new session for the current thread.
     * If a session is already open it is reused, otherwise the EntityManagerFactory is requested to
     * get the current session (which may create a new one) */
    protected abstract PersistenceSession newSession() throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

    /** Open a session for a thread
     * If a session is already open it is reused, otherwise the session Factory is requested to
     * get the current session (which may create a new one) */
    public PersistenceSession openSession() throws PersistenceServiceException {
        if (hasSession()) {
            PersistenceSession currentSession = sessionPerThread.get();
            currentSession.continueConversation();
            if (LOG.isDebugEnabled()) {
                LOG.debug("=== Continuing current session (Thread:"+Thread.currentThread().getName()+") ===");
            }
            return currentSession;
        } else {
            return newSession();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Close a session for a thread.  The session implementation may not actually close if it deems there
     *  is a reason to stay open (such as a nested session) */
    public void closeSession() throws PersistenceServiceException {
        try {
            PersistenceSession session = sessionPerThread.get();

            // performs a flush and closes the session.
            if (session != null) {
                if (session.close()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("=== Closed current session (Thread:"+Thread.currentThread().getName()+") ===");
                    }
                    // clear the threadlocal session
                    sessionPerThread.set(null);
                }
            }
        }
        catch (Exception e) {
            throw new PersistenceServiceException("Error closing the session", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup the list of the specified domain objects.  Uses the Current Session.
     * @param clazz
     * @return List of DomainObjects of the specified class
     */
    @SuppressWarnings({"unchecked"})
    protected abstract <T extends HasIdentity> List<T> doFindAll(Class<T> clazz) throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup the list of the specified domain objects.  Uses the Current Session.
     * @param clazz
     * @return List of DomainObjects of the specified class
     */
    @SuppressWarnings({"unchecked"})
    public <T extends HasIdentity> List<T> findAll(Class<T> clazz) throws PersistenceServiceException {
        if (hasSession()) {
            // use an existing long session
            return doFindAll(clazz);
        }
        else {
            // create a new session and close it after the task
            openSession();
            List<T> found = doFindAll(clazz);
            closeSession();
            return found;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Save the specified domain object to the persistent store.
     * This version opens a session and performs the save as a transaction.
     *
     * Returns the DomainObject after saving.
     * If the DomainObject is new, it will now have an IdentityRef Field that uniquely identifies it in the
     * store.  */
    protected abstract HasIdentity doSave(HasIdentity domainObject) throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

    /** Save the specified domain object to the persistent store.
     * This version opens a session and performs the save as a transaction.
     *
     * Returns the DomainObject after saving.
     * If the DomainObject is new, it will now have an IdentityRef Field that uniquely identifies it in the
     * store.  */
    public HasIdentity save(HasIdentity domainObject) throws PersistenceServiceException {
        if (hasSession()) {
            // use an existing long session
            return doSave(domainObject);
        }
        else {
            // create a new session and close it after the task
            openSession();
            HasIdentity saved = doSave(domainObject);
            closeSession();
            return saved;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for domain objects that match the given criteria using the session provided
     * @return
     * @throws PersistenceServiceException
     */
    @SuppressWarnings({"unchecked"})
    protected abstract <T extends HasIdentity> Collection<T> doFind(PersistenceQuery criteria) throws PersistenceServiceException;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for domain objects that match the given criteria
     * @return
     * @throws PersistenceServiceException
     */
    public <T extends HasIdentity> Collection<T> find(Class<T> clazz, PersistenceQuery criteria) throws PersistenceServiceException {
        if (hasSession()) {
            // use an existing long session
            return doFind(criteria);
        }
        else {
            // create a new session and close it after the task
            openSession();
            Collection<T> found = doFind(criteria);
            closeSession();
            return found;
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for domain objects that match the given criteria and returns the first valid result
     * @return
     * @throws PersistenceServiceException
     */
    public <T extends HasIdentity> T findOne(Class<T> clazz, PersistenceQuery criteria) throws PersistenceServiceException {
        T foundMatch = null;
        Collection<T> matches;

        if (hasSession()) {
            // use an existing long session
             matches = doFind(criteria);
        }
        else {
            // create a new session and close it after the task
            openSession();
            matches = doFind(criteria);
            closeSession();
        }

        for (T match : matches) {
            //if (match.isValid()) {
                foundMatch = match;
                break;
            //}
        }
        return foundMatch;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for the domain object with the specified identityRef
     * @return
     * @throws PersistenceServiceException
     */
    public HasIdentity findById(IdentityRef identityRef) throws PersistenceServiceException {
        return findById(identityRef.getClazz(), identityRef.getId());
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
