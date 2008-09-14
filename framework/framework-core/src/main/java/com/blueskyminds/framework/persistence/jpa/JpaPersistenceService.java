package com.blueskyminds.framework.persistence.jpa;

import com.blueskyminds.framework.HasIdentity;
import com.blueskyminds.framework.persistence.AbstractPersistenceService;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.PersistenceSession;
import com.blueskyminds.framework.persistence.jpa.query.JpaNamedQuery;
import com.blueskyminds.framework.persistence.jpa.query.JpaQuery;
import com.blueskyminds.framework.persistence.paging.Page;
import com.blueskyminds.framework.persistence.query.PersistenceNamedQuery;
import com.blueskyminds.framework.persistence.query.PersistenceQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Implements a PersistenceService using JPA (EntityManager)
 * </p>
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JpaPersistenceService extends AbstractPersistenceService implements PersistenceService {

    private static final Log LOG = LogFactory.getLog(JpaPersistenceService.class);
    private EntityManagerFactory emf;
    private static final String DEFAULT_PERSISTENCE_UNIT = "DefaultEntityManager";

    private String persistenceUnitName;
    public static final String PERSISTENCE_UNIT_PROPERTY = "jpa.persistenceService.persistenceUnit";

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    public JpaPersistenceService() {
    }

    public JpaPersistenceService(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Initialize the gateway implementation - creates an EntityManagerFactory */
    private void initialise() {
        try {
            if (persistenceUnitName == null) {
                persistenceUnitName = properties.getProperty(PERSISTENCE_UNIT_PROPERTY);
            }

            if (persistenceUnitName == null) {
                throw new ExceptionInInitializerError(PERSISTENCE_UNIT_PROPERTY +" has not been set");
            }

            LOG.info("Persistence Unit: "+persistenceUnitName);

            // NOTE: The EntityManagerFactory will look for a file in the classpath: META-INF/persistence.xml
            URL persistenceXml = getClass().getClassLoader().getResource("META-INF/persistence.xml");
            if (persistenceXml != null) {
                LOG.info("first persistence.xml found in classpath at: "+persistenceXml.toString());
            } else {
                LOG.warn("persistence.xml not found in classpath");
            }
            emf = Persistence.createEntityManagerFactory(persistenceUnitName);

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            LOG.error("Initial EntityManagerFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a reference to the EntityManagerFactory
     * @return EntityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            initialise();
        }
        return emf;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Determines if the current thread has a long session open already. */
    protected boolean hasSession() {
        PersistenceSession currentSession = sessionPerThread.get();

        if (currentSession != null) {
            if (((EntityManager) currentSession.getSessionImpl()).isOpen()) {
                // the current session for this thread is still open - use it.
                return true;
            } else {
                LOG.debug("=== EntityManager is no longer open (Thread:"+Thread.currentThread().getName()+") ===");
            }
        }
        return false;
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a new session for the current thread.
     * If a session is already open it is reused, otherwise the EntityManagerFactory is requested to
     * get the current session (which may create a new one) */
    protected PersistenceSession newSession() throws PersistenceServiceException {
        try {
            EntityManager em = getEntityManagerFactory().createEntityManager();
            em.setFlushMode(FlushModeType.COMMIT);   // disable automatic flushing - it is controlled explictly
            PersistenceSession newSession = new JpaPersistenceSessionImpl(em);

            if (LOG.isDebugEnabled()) {
                LOG.debug("=== Created new EntityManager (Thread:"+Thread.currentThread().getName()+") ===");
            }

            // start a transaction for this new session
            //Transaction transaction = em.beginTransaction();
            newSession.beginTransaction();

            // remember the new session for this thread
            sessionPerThread.set(newSession);

            return newSession;

        }
        catch (Exception e) {
            throw new PersistenceServiceException("Error opening a session", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Gets a reference to the current open session for this thread, assuming there is on */
    public EntityManager getEntityManager() {
        PersistenceSession session = sessionPerThread.get();
        if (session != null) {
            return (EntityManager) session.getSessionImpl();
        }
        else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup the list of the specified domain objects.  Uses the Current Session.
     * @param clazz
     * @return List of DomainObjects of the specified class
     */
    @SuppressWarnings({"unchecked"})
    protected <T extends HasIdentity> List<T> doFindAll(Class<T> clazz) throws PersistenceServiceException {
        List<T> list;
        try {
            Query criteria = allEntitiesOfClass(clazz);
            list = (List<T>) criteria.getResultList();
        }
        catch (Exception e) {
            if (getEntityManager() != null) {
                EntityTransaction transaction = getEntityManager().getTransaction();
                if (transaction != null) {
                    LOG.error("Persistence Error: Requesting transaction rollback");
                    try {
                        transaction.rollback();
                    } catch (Exception e2) {
                        // don't let the root cause get lost
                    }
                }
            }
            throw new PersistenceServiceException("Error searching for domain objects of class: "+clazz.getSimpleName(), e);
        }

        return list;
    }



    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Save the specified domain object to the persistent store.
     * This version opens a session and performs the save as a transaction.
     *
     * Returns the DomainObject after saving.
     * If the DomainObject is new, it will now have an IdentityRef Field that uniquely identifies it in the
     * store.  */
    protected HasIdentity doSave(HasIdentity domainObject) throws PersistenceServiceException {
        try {
            getEntityManager().persist(domainObject);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (getEntityManager() != null) {
                EntityTransaction transaction = getEntityManager().getTransaction();
                if (transaction != null) {
                    LOG.error("Persistence Error: Requesting transaction rollback");
                    try {
                        transaction.rollback();
                    } catch (Exception e2) {
                        // don't let the root cause get lost
                    }
                }
            }
            throw new PersistenceServiceException("Error saving a domain object", e);
        }

        return domainObject;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    
    // ------------------------------------------------------------------------------------------------------

    /** Transform the vendor-independent query into a JpaQuery */
    private JpaQuery transformQuery(PersistenceQuery query) throws PersistenceServiceException {
        JpaQuery jpaQuery = null;

        if (query instanceof PersistenceNamedQuery) {
            jpaQuery = new JpaNamedQuery(((PersistenceNamedQuery) query).getName());
            jpaQuery.setParameters(query.getParameters());
        } else {
            if (JpaQuery.class.isAssignableFrom(query.getClass())) {
                jpaQuery = (JpaQuery) query;
            } else {
                jpaQuery = new JpaQuery(query.getQueryString());
                jpaQuery.setParameters(query.getParameters());
            }
//            {
//                throw new PersistenceServiceException("JpaPersistenceService does not support queries of class "+query.getClass());
//            }
        }
        return jpaQuery;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for domain objects that match the given criteria using the session provided
     * @return
     * @throws PersistenceServiceException
     */
    @SuppressWarnings({"unchecked"})
    protected <T extends HasIdentity> Collection<T> doFind(PersistenceQuery query) throws PersistenceServiceException {
        List<T> foundObjects;

        try {
            JpaQuery jpaQuery = transformQuery(query);
            jpaQuery.prepareQuery(getEntityManager());
            foundObjects = jpaQuery.getUnderlying().getResultList();
        } catch (Exception e) {
            throw new PersistenceServiceException("Error searching for domain objects", e);
        }

        return foundObjects;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for the domain object with the specified id
     * @return
     * @throws PersistenceServiceException
     */
    public <T extends HasIdentity> T findById(Class<T> clazz, Long id) throws PersistenceServiceException {
        T foundMatch;

        if (hasSession()) {
            foundMatch = getEntityManager().find(clazz, id);
        }
        else {
            // create a new session and close it after the task
            openSession();
            foundMatch = getEntityManager().find(clazz, id);
            closeSession();
        }

        return foundMatch;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Count the number of instances of the specified domain object
     *
     * @param clazz
     * @return count, as a long
     * @throws PersistenceServiceException
     */
    public <T extends HasIdentity> long count(Class<T> clazz) throws PersistenceServiceException {
        Long result;

        if (hasSession()) {
            Query query = getEntityManager().createQuery("select count(*) from "+clazz.getName());
            result = (Long) query.getSingleResult();
        } else {
            openSession();
            Query query = getEntityManager().createQuery("select count(*) from "+clazz.getName());
            result = (Long) query.getSingleResult();
            closeSession();
        }

        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns a random instance of the specified domain object, fetched from the database
     *
     * @param clazz
     * @return the valid domain object instance, or null if there were none found
     * @throws PersistenceServiceException
     */
    @SuppressWarnings({"unchecked"})
    public <T extends HasIdentity> T random(Class<T> clazz) throws PersistenceServiceException {
        T result;

        // count the number of instances
        long count = count(clazz);

        Random generator = new Random();
        int offset = generator.nextInt((int) count);

        if (hasSession()) {
            Query query = allEntitiesOfClass(clazz).setFirstResult(offset).setMaxResults(1);
            result = (T) query.getSingleResult();
        } else {
            openSession();
            Query query = allEntitiesOfClass(clazz).setFirstResult(offset).setMaxResults(1);
            result = (T) query.getSingleResult();
            closeSession();
        }

        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Creates a commonly used query to find all objects of a particular class */
    private <T> Query allEntitiesOfClass(Class<T> clazz) {
        return getEntityManager().createQuery("select o from "+clazz.getName()+ " o");
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns a page containing domain objects of the specified class. */
    public <T> Page findPage(Class<T> clazz, int pageNo, int pageSize) throws PersistenceServiceException {
        Page page = null;

        if (hasSession()) {
            // use an existing long session
            page = new JpaPage(allEntitiesOfClass(clazz), pageNo, pageSize);
        }
        else {
            // create a new session and close it after the task
            openSession();
            page = new JpaPage(allEntitiesOfClass(clazz), pageNo, pageSize);
            closeSession();
        }

        return page;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for domain objects that match the given criteria, returning the requested page
     * @param criteria
     * @param pageNo
     * @param pageSize
     * @return
     * @throws PersistenceServiceException
     */
    public Page findPage(PersistenceQuery criteria, int pageNo, int pageSize) throws PersistenceServiceException {
        Page page = null;
        boolean closeSession = false;

        if (!hasSession()) {
            openSession();
            closeSession = true;
        }

        JpaQuery jpaQuery = transformQuery(criteria);
        jpaQuery.prepareQuery(getEntityManager());
        page = new JpaPage(jpaQuery.getUnderlying(), pageNo,  pageSize);

        if (closeSession) {
            closeSession();
        }

        return page;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Merge the state of the given DomainObject into the current persistence context
     *
     * @param domainObject
     * @return the result may not be the same instance of the domainObject, but it will have the same IdentityRef
     */
    public <T extends HasIdentity> T merge(T domainObject) throws PersistenceServiceException {
        T merged;

        if (hasSession()) {
            merged = getEntityManager().merge(domainObject);
        }
        else {
            // create a new session and close it after the task
            openSession();
            merged = getEntityManager().merge(domainObject);
            closeSession();
        }

        return merged;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Executes SQL directly
     * @param nativeSqlLines lines of sql (one statement per line)
     * @return number of entities affected rows
     * @throws PersistenceServiceException if an SQL exception occurred
     **/
    public int executeBulkUpdate(String[] nativeSqlLines) throws PersistenceServiceException {
        int entitiesAffected = 0;

        if (hasSession()) {
            for (String sqlLine : nativeSqlLines) {
                entitiesAffected += getEntityManager().createNativeQuery(sqlLine).executeUpdate();
            }
        }
        else {
            // create a new session and close it after the task
            openSession();
            for (String sqlLine : nativeSqlLines) {
                entitiesAffected += getEntityManager().createNativeQuery(sqlLine).executeUpdate();
            }
            closeSession();
        }

        return entitiesAffected;
    }

    // ------------------------------------------------------------------------------------------------------

     /**
     * Execute an update operation in the specified query
     *
     * @param query containing the update operation
     *
     * @return number of affected entities
     * @throws PersistenceServiceException if the update can't be performed
     */
    public int executeUpdate(PersistenceQuery query) throws PersistenceServiceException {

        int affected;
        boolean closeSession = false;

        if (!hasSession()) {
            openSession();
            closeSession = true;
        }

        JpaQuery jpaQuery = transformQuery(query);
        jpaQuery.prepareQuery(getEntityManager());
        affected = jpaQuery.getUnderlying().executeUpdate();

        if (closeSession) {
            closeSession();
        }

        return affected;
    }

}
