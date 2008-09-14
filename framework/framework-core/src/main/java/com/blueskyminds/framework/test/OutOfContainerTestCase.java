package com.blueskyminds.framework.test;

import com.blueskyminds.framework.persistence.jdbc.PersistenceTools;
import com.blueskyminds.framework.tools.LoggerTools;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.util.Properties;

/**
 * Superclass for unit tests performed outside an EJB container but using an EntityManager
 *
 * The EntityManagerFactory is created created prior to executing each fixture in setUp().
 * This creates an empty HSQL database for each fixture.  An EntityManager is also created and
 * a new transaction is started.
 * After each fixture this is all closed in tearDown().
 *
 * During setup an EntityManagerFactory is used to create an EM instance and a new transaction is commenced.
 * The same PersistenceUnit must be used for all tests
 *
 * Requires JUnit3
 *
 * Date Started: 6/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class OutOfContainerTestCase extends TestCase {

    private static final Log LOG = LogFactory.getLog(OutOfContainerTestCase.class);

    private String persistenceUnitName;
    private EntityManagerFactory emf;
    protected EntityManager em;
    protected EntityTransaction transaction;
    protected Properties persistenceUnitProperties;

    public OutOfContainerTestCase(String persistenceUnitName) {
        LoggerTools.configure();
        LOG.info("PersistenceUnit:"+persistenceUnitName);
        this.persistenceUnitName = persistenceUnitName;
        this.persistenceUnitProperties = null;
    }

     /**
     * This method allows properties to be supplied that override the properties defined by the
     * persistence unit
     *
     * @param persistenceUnitProperties   properties to override the default properties of the persistence unit */
    public void setPersistenceUnitProperties(Properties persistenceUnitProperties) {
        this.persistenceUnitProperties = persistenceUnitProperties;
    }

    /**
     * Creates an EntityManager and starts a transaction
     * @throws Exception                                                 
     */
    protected void setUp() throws Exception {
        super.setUp();
        if (persistenceUnitProperties == null) {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        } else {
            LOG.info("The PersistenceUnit properties have been overrriden");
            emf = Persistence.createEntityManagerFactory(persistenceUnitName, persistenceUnitProperties);
        }

        startTransaction();
    }

    private void startTransaction() {
        em = emf.createEntityManager();

        if (LOG.isDebugEnabled()) {
            LOG.debug(PersistenceTools.getTableMetadata(getConnection(), "system_"));
        }

        transaction = em.getTransaction();
        transaction.begin();
    }

    protected void newTransaction() {
        endTransaction();
        startTransaction();
    }

    private void endTransaction() {
        if (transaction != null) {
            if (transaction.isActive()) {
                if (!transaction.getRollbackOnly()) {
                    transaction.commit();
                } else {
                    transaction.rollback();
                }
            }
        }
        if (em != null) {
            if (em.isOpen()) {
                em.close();
            }
        } else {
            LOG.error("EntityManager has not been setup.  Fixtures must invoke the superclass setUp() method to create an EM and transaction.");
        }
    }

    protected void tearDown() throws Exception {
        endTransaction();
        emf.close();
        super.tearDown();
    }

    /**
     * Gets the JDBC connection from the hibernate session underlying the EntityManager.
     * If a hibernate persistence provider is not being used this will fail
     * */
    protected Connection getConnection() {
        Session session = (Session) em.getDelegate();
        return session.connection();
    }

    /**
     * Provides access to the EntityManagerFactory that was used to create the current EntityManager.
     * It can be used to object metadata if you can typecast it to an implementation.
     *
     * @return
     */
    protected EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
