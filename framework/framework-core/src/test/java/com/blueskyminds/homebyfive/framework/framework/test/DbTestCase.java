package com.blueskyminds.homebyfive.framework.framework.test;

import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceService;
import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.framework.persistence.PersistenceServiceFactory;
import com.blueskyminds.homebyfive.framework.framework.persistence.jdbc.PersistenceTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * A TestCase superclass that initialises a hypersonic database and configures an Persistence Service
 *
 * The PersistenceService is created at time of construction (prior to the first test)
 *
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class DbTestCase extends BaseTestCase {

    private static final Log LOG = LogFactory.getLog(DbTestCase.class);

    private PersistenceService persistenceService;

    public DbTestCase(String name) {
        super(name);
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the DbTestCase with default attributes
     */
    private void init() {
        try {
            persistenceService = PersistenceServiceFactory.createPersistenceService(getPropertiesContext());

            // quick hack to create an empty schema
            persistenceService.openSession();
            persistenceService.closeSession();

            if (LOG.isInfoEnabled()) {
                try {
                    LOG.info(PersistenceTools.getTableMetadata(getConnection(), "SYSTEM_"));
                } catch(Exception e) {
                    LOG.error("Failed to get table metadata");
                }
            }

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    /** This is a hack implementation that assumes the PersistenceUnit is hypersonic mem */
    public Connection getConnection() throws Exception {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            throw e;
        }

        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:mem", "sa", "");
        return connection;
    }
}
