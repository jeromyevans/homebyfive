package com.blueskyminds.framework.persistence.datasource;

import com.blueskyminds.framework.test.BaseTestCase;
import org.hsqldb.jdbc.jdbcDataSource;

import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.Hashtable;

/**
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestJndiDatasource extends BaseTestCase {

    public TestJndiDatasource(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestJndiDatasource with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public void testJndiDataSource() throws Exception {
        jdbcDataSource dataSource = new jdbcDataSource();

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
            "com.sun.jndi.fscontext.RefFSContextFactory");

        Context ctx = new InitialContext(env);
        ctx.bind("jdbc/hsqlDb", dataSource);
    }
}
