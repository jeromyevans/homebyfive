package com.blueskyminds.framework.persistence.hibernate;

import com.blueskyminds.framework.HasIdentity;
import com.blueskyminds.framework.persistence.*;
import com.blueskyminds.framework.persistence.paging.Page;
import com.blueskyminds.framework.persistence.hibernate.query.HibernateCriteriaImpl;
import com.blueskyminds.framework.persistence.hibernate.query.HibernateQuery;
import com.blueskyminds.framework.persistence.hibernate.query.HibernateNamedQueryImpl;
import com.blueskyminds.framework.persistence.hibernate.query.HibernateQueryImpl;
import com.blueskyminds.framework.persistence.query.PersistenceQuery;
import com.blueskyminds.framework.persistence.query.PersistenceNamedQuery;
import com.blueskyminds.framework.persistence.jpa.EntityDefinition;
import com.blueskyminds.framework.tools.ResourceLocator;
import com.blueskyminds.framework.tools.PropertiesContext;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.dialect.Dialect;
import org.hibernate.cfg.AnnotationConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Collection;
import java.util.Random;
import java.net.URL;

/**
 * An implementation of a PersistenceService that uses Hibernate 3.2+
 *
 * Important: This implementation supports long conversations - that is, sessions that span
 *   multiple database transactions.  You must call openSession and closeSession to demark sessions and
 *   cause the session to flush.
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class HibernatePersistenceService extends AbstractPersistenceService implements PersistenceService {

    private static final Log LOG = LogFactory.getLog(HibernatePersistenceService.class);

    /** The properties file that contains configuration information for this implementation */
    private static final String PROPERTIES_FILE = "hibernatePersistence.properties";

    private static final String HIBERNATE_CONFIG_PROPERTY = "hibernate.config";
    private static final String HIBERNATE_ENTITIES_PROPERTY = "entity.definitions";

    /** Singleton instance of the sessionFactory */
    private static SessionFactory sessionFactory;


    /** A map of sessions that have been explicitly opened by threads using the openSession method.
     * The map is used to re-use the session until it's closed
     */
    //private Map<Thread, PersistenceSession> currentSessions;

    // ------------------------------------------------------------------------------------------------------

    public HibernatePersistenceService() {
        //currentSessions = new HashMap<Thread, PersistenceSession>();
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Reads all of the entity definitions found on the classpath and loads them into the hibernate
     * configuration programmatically
     */
    private static void loadEntityDefinitions(String[] definitions, AnnotationConfiguration annotationConfiguration) {
        String[] entityClassNames = new EntityDefinition(definitions).loadEntityDefinitions();

        // now configure hibernate
        if (entityClassNames.length > 0) {
            for (String className : entityClassNames) {
                try {
                    annotationConfiguration.addAnnotatedClass(Class.forName(className));
                    LOG.debug("Configured entity '"+className+"'");
                } catch (ClassNotFoundException e) {
                    LOG.error("Entity with classname '"+className+"' was not found in the classpath (skipped)");
                }
            }
        } else {
            LOG.warn("No entities defined");
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialize the gateway implementation - loads the hibernate configuration */
    private static void initialise () {
        List<String> entities;
        // load the configuration
        PropertiesContext propertiesContext = new PropertiesContext(PROPERTIES_FILE);

        try {
            URL hibernateConfig = ResourceLocator.locateResourceByProperty(HIBERNATE_CONFIG_PROPERTY, propertiesContext);
            String[] definitions = propertiesContext.getPropertyCSV(HIBERNATE_ENTITIES_PROPERTY);

            if (hibernateConfig != null) {
                // Create the SessionFactory
                AnnotationConfiguration annotationConf = new AnnotationConfiguration();
                loadEntityDefinitions(definitions, annotationConf);

                org.hibernate.cfg.Configuration hibernateConfiguration = annotationConf.configure(hibernateConfig);

                Dialect dialect = Dialect.getDialect(hibernateConfiguration.getProperties());
    //            String[] dropSql = hibernateConfiguration.generateDropSchemaScript(dialect);
                String[] createSql = hibernateConfiguration.generateSchemaCreationScript(dialect);

    //            for (String statement : dropSql) {
    //                LOG.debug(statement);
    //            }
                if (LOG.isDebugEnabled()) {
                    for (String statement : createSql) {
                        LOG.debug(statement);
                    }
                }

                sessionFactory = hibernateConfiguration.buildSessionFactory();
            } else {
                LOG.error("Failed to locate Hibernate configuration: "+ propertiesContext.getProperty(HIBERNATE_CONFIG_PROPERTY));
            }

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            LOG.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a reference to the session factory
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            initialise();
        }
        return sessionFactory;
    }



    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Determines if the current thread has a long session open already. */
    protected boolean hasSession() {
        PersistenceSession currentSession = sessionPerThread.get();

        if (currentSession != null) {
            if (((Session) currentSession.getSessionImpl()).isOpen()) {
                // the current session for this thread is still open - use it.
                return true;
            } else {
                LOG.debug("=== Sesssion is no longer open (Thread:"+Thread.currentThread().getName()+") ===");
            }
        }
        return false;
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a new session for the current thread.
     * If a session is already open it is reused, otherwise the session Factory is requested to
     * get the current session (which may create a new one) */
    protected PersistenceSession newSession() throws PersistenceServiceException {
        try {
            Session hibernateSession = getSessionFactory().openSession();
            hibernateSession.setFlushMode(FlushMode.COMMIT);   // disable automatic flushing - it is controlled explictly
            PersistenceSession newSession = new HibernatePersistenceSessionImpl(hibernateSession);

            if (LOG.isDebugEnabled()) {
                LOG.debug("=== Created new session (Thread:"+Thread.currentThread().getName()+") ===");
            }

            // start a transaction for this new session
            //Transaction transaction = hibernateSession.beginTransaction();
            newSession.beginTransaction();

            // remember the new session for this thread
            sessionPerThread.set(newSession);

            return newSession;

        }
        catch (HibernateException e) {
            throw new PersistenceServiceException("Error opening a session", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Gets a reference to the current open session for this thread, assuming there is on */
    private Session currentSession() {
        PersistenceSession session = sessionPerThread.get();
        if (session != null) {
            return (Session) session.getSessionImpl();
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
            Criteria criteria = currentSession().createCriteria(clazz);
            list = (List<T>) criteria.list();
        }
        catch (HibernateException e) {
            if (currentSession() != null) {
                Transaction transaction = currentSession().getTransaction();
                if (transaction != null) {
                    LOG.error("HibernateError: Requesting transaction rollback");
                    try {
                        transaction.rollback();
                    } catch (HibernateException e2) {
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
            currentSession().saveOrUpdate(domainObject);
        }
        catch (HibernateException e) {
            e.printStackTrace();
            if (currentSession() != null) {
                Transaction transaction = currentSession().getTransaction();
                if (transaction != null) {
                    LOG.error("HibernateError: Requesting transaction rollback");
                    try {
                        transaction.rollback();
                    } catch (HibernateException e2) {
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
    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Transform the vendor-independent query into a JpaQuery */
    private HibernateQuery transformQuery(PersistenceQuery query) throws PersistenceServiceException {
        HibernateQuery hibernateQuery;

        if (query instanceof PersistenceNamedQuery) {
            hibernateQuery = new HibernateNamedQueryImpl(((PersistenceNamedQuery) query).getName());
            hibernateQuery.setParameters(query.getParameters());
        } else {
            if (HibernateQuery.class.isAssignableFrom(query.getClass())) {
                hibernateQuery = (HibernateQuery) query;
            } else {
                hibernateQuery = new HibernateQueryImpl(query.getQueryString());
                hibernateQuery.setParameters(query.getParameters());
            }
        }
        return hibernateQuery;
    }

    /**
     * Search for domain objects that match the given criteria using the session provided
     * @return
     * @throws PersistenceServiceException
     */
    @SuppressWarnings({"unchecked"})
    protected <T extends HasIdentity> Collection<T> doFind(PersistenceQuery query) throws PersistenceServiceException {
        List<T> foundObjects;

        try {
            HibernateQuery hibernateQuery = transformQuery(query);
            hibernateQuery.prepareQuery(currentSession());
            if (hibernateQuery instanceof HibernateCriteriaImpl) {
                foundObjects = ((Criteria) hibernateQuery.getUnderlying()).list();
            } else {
                foundObjects = ((Query) hibernateQuery.getUnderlying()).list();
            }
        } catch (HibernateException e) {
            throw new PersistenceServiceException("Error searching for domain objects", e);
        }

        return foundObjects;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for the domain object with the specified id
     * @return
     * @throws PersistenceServiceException
     */
    public <T extends HasIdentity> T findById(Class<T> clazz, Long id) throws PersistenceServiceException {
        T foundMatch = null;
        Collection<T> matches;

        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        criteria.add(Expression.idEq(id));

        if (hasSession()) {
            // use an existing long session
            matches = doFind(new HibernateCriteriaImpl(criteria));
        }
        else {
            // create a new session and close it after the task
            openSession();
            matches = doFind(new HibernateCriteriaImpl(criteria));
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
    // ------------------------------------------------------------------------------------------------------

    /**
     * Count the number of instances of the specified domain object
     *
     * @param clazz
     * @return count, as an int
     * @throws PersistenceServiceException
     */
    public <T extends HasIdentity> long count(Class<T> clazz) throws PersistenceServiceException {
        Long result;

        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        criteria.setProjection(Projections.rowCount());

        if (hasSession()) {
            result = (Long) criteria.getExecutableCriteria(currentSession()).uniqueResult();
        } else {
            openSession();
            result = (Long) criteria.getExecutableCriteria(currentSession()).uniqueResult();
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

        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);

        if (hasSession()) {
            result = (T) criteria.getExecutableCriteria(currentSession()).setFirstResult(offset).setMaxResults(1).uniqueResult();
        } else {
            openSession();
            result = (T) criteria.getExecutableCriteria(currentSession()).setFirstResult(offset).setMaxResults(1).uniqueResult();
            closeSession();
        }

        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns a page containing domain objects of the specified class. */
    public <T> Page findPage(Class<T> clazz, int pageNo, int pageSize) throws PersistenceServiceException {
        Page page;
        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        if (hasSession()) {
            // use an existing long session
            page = new HibernatePageImpl(criteria.getExecutableCriteria(currentSession()), pageNo, pageSize);
        }
        else {
            // create a new session and close it after the task
            openSession();
            page = new HibernatePageImpl(criteria.getExecutableCriteria(currentSession()), pageNo, pageSize);
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
        Page page;
        boolean closeSession = false;

        if (!hasSession()) {
            openSession();
            closeSession = true;
        }

        if (criteria instanceof HibernateQuery) {
            Object query = ((HibernateQuery) criteria).getUnderlying();
            if (query instanceof Query) {
                page = new HibernatePageImpl((Query) query, pageNo, pageSize);
            } else {
                page = new HibernatePageImpl((Criteria) query, pageNo, pageSize);
            }
        } else {
            throw new PersistenceServiceException("HibernatePersistenceService does not support queries of class "+criteria.getClass());
        }

        if (closeSession) {
            closeSession();
        }

        return page;
    }   

    // ------------------------------------------------------------------------------------------------------

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
            merged = (T) currentSession().merge(domainObject);
        }
        else {
            // create a new session and close it after the task
            openSession();
            merged = (T) currentSession().merge(domainObject);
            closeSession();
        }

        return domainObject;
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
                entitiesAffected += currentSession().createSQLQuery(sqlLine).executeUpdate();
            }
        }
        else {
            // create a new session and close it after the task
            openSession();
            for (String sqlLine : nativeSqlLines) {
                entitiesAffected += currentSession().createSQLQuery(sqlLine).executeUpdate();
            }
            closeSession();
        }

        return entitiesAffected;
    }


     /**
     * Execute an update operation in the specified query
     *
     * @param criteria containing the update operation
     *
     * @return number of affected entities
     * @throws PersistenceServiceException if the update can't be performed
     */
    public int executeUpdate(PersistenceQuery criteria) throws PersistenceServiceException {

        int affected;
        boolean closeSession = false;

        if (!hasSession()) {
            openSession();
            closeSession = true;
        }

        if (criteria instanceof HibernateQuery) {
            Object query = ((HibernateQuery) criteria).getUnderlying();
            if (query instanceof Query) {
                affected = ((Query) query).executeUpdate();
            } else {
                throw new PersistenceServiceException("HibernatePersistenceService does not support updates with query class "+criteria.getClass());
            }
        } else {
            throw new PersistenceServiceException("HibernatePersistenceService does not support queries of class "+criteria.getClass());
        }

        if (closeSession) {
            closeSession();
        }
        return affected;
    }

}
