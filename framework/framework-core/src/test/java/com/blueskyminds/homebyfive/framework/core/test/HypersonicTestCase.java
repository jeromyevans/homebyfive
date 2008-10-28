package com.blueskyminds.homebyfive.framework.core.test;

import com.blueskyminds.homebyfive.framework.core.persistence.hsqldb.HsqlDbMemoryDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.sql.DataSource;

/**
 * An in-memory hypersonic based unit test framework
 *
 * Date Started: 12/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class HypersonicTestCase extends BaseTestCase {

    private static final Log LOG = LogFactory.getLog(HypersonicTestCase.class);

    private static final String HSQL_DATABASE = "mem";
    private static final String HSQL_USER = "sa";
    private static final String HSQL_PASSWORD = "";

    private DataSource dataSource;

    public HypersonicTestCase(String string) {
        super(string);
        init();
    }

    public HypersonicTestCase() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the HypersonicTestCase with default attributes
     */
    private void init() {
        dataSource = HsqlDbMemoryDataSourceFactory.createDataSource(HSQL_DATABASE, HSQL_USER, HSQL_PASSWORD);
    }

    /** Get a connection to the Hypersonic database */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // ------------------------------------------------------------------------------------------------------
}
