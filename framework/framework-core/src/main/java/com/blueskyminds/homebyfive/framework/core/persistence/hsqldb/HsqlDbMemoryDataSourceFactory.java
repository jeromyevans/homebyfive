package com.blueskyminds.homebyfive.framework.core.persistence.hsqldb;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.hsqldb.jdbc.jdbcDataSource;

import javax.sql.DataSource;

/**
 * Date Started: 13/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class HsqlDbMemoryDataSourceFactory {

    private static final Log LOG = LogFactory.getLog(HsqlDbMemoryDataSourceFactory.class);

    public static final String HSQL_DRIVER_NAME = "org.hsqldb.jdbcDriver";
    public static final String HSQL_JDBC_URL = "jdbc:hsqldb:mem:";
    public static final String HSQL_USER = "sa";
    public static final String HSQL_PASSWORD = "";

    // ------------------------------------------------------------------------------------------------------

    /**
     * Load the driver
     */
    private static void initDriver() {
        try {
            Class.forName(HSQL_DRIVER_NAME);
        } catch (Exception e) {
            LOG.error("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create an hsqldb in-memory datasource
     *
     * @param database              database name
     * @param user                  user to login as
     * @param plainTextPassword    plain text password to use
     * @return a jdbc datasource
     */
    public static DataSource createDataSource(String database, String user, String plainTextPassword) {
        initDriver();

        jdbcDataSource dataSource = new jdbcDataSource();

        dataSource.setDatabase(HSQL_JDBC_URL+database);
        dataSource.setUser(user);
        dataSource.setPassword(plainTextPassword);

        return dataSource;
    }
}
