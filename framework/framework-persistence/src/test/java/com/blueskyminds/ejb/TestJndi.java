package com.blueskyminds.ejb;

import com.blueskyminds.framework.test.BaseTestCase;
import com.blueskyminds.framework.persistence.DataSourceFactory;
import com.blueskyminds.framework.persistence.DataSourceType;
//import org.jboss.ejb3.embedded.EJB3StandaloneBootstrap;
//import org.jnp.interfaces.LocalOnlyContextFactory;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;
import java.util.Hashtable;
import java.sql.Connection;

/**
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestJndi extends BaseTestCase {

    public TestJndi(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestJndi with default attributes
     */
    private void init() {
        //TestSuite
    }

   public static InitialContext getInitialContext() throws Exception
   {
      Hashtable props = getInitialContextProperties();
      return new InitialContext(props);
   }

   private static Hashtable getInitialContextProperties()
   {
      Hashtable props = new Hashtable();
      props.put("java.naming.factory.initial", "org.jnp.interfaces.LocalOnlyContextFactory");
      props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
      return props;
   }

    // ------------------------------------------------------------------------------------------------------

    public void testLocalJndi() throws Exception {
//        EJB3StandaloneBootstrap.boot(null);

        InitialContext ctx = getInitialContext();
        DataSource defaultDS = (DataSource) ctx.lookup("java:/DefaultDS");
        assertNotNull(defaultDS);
        Connection connection = defaultDS.getConnection();
        assertNotNull(connection);
        connection.close();

        // test loading a new datasource into the context
        DataSource hypersonic = DataSourceFactory.createDataSource(DataSourceType.hsqldb, "mem", "sa", "");
        ctx.bind("java:/hsqldbDS", hypersonic);

   //     EJB3StandaloneBootstrap.scanClasspath();
        DataSource ds2 = (DataSource) ctx.lookup("java:/hsqldbDS");
        assertNotNull(ds2);

    }
}
