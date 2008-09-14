package com.blueskyminds.framework.persistence.jdbc;

import com.blueskyminds.framework.test.BaseTestCase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

import org.hsqldb.jdbc.jdbcDataSource;

/**
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestHypersonic extends BaseTestCase {

    public TestHypersonic(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestHypersonic with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Creates an in-meory hypersonic database */
    public void testSetupHypersonic() throws Exception {
        try {
            Class.forName("org.hsqldb.jdbcDriver" );
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
    }

    /** Creates an in-meory hypersonic database */
    public void testHypersonicDataSource() throws Exception {
        DataSource dataSource = new jdbcDataSource();

        Connection c = dataSource.getConnection();        
    }
}
