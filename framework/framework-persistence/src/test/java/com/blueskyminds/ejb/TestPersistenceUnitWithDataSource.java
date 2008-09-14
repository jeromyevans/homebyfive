package com.blueskyminds.ejb;

import com.blueskyminds.framework.test.BaseTestCase;
//import org.jboss.ejb3.embedded.EJB3StandaloneBootstrap;

import javax.persistence.EntityManager;
import javax.naming.InitialContext;
import java.util.Hashtable;

/**
 * Date Started: 14/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestPersistenceUnitWithDataSource extends BaseTestCase {

    public TestPersistenceUnitWithDataSource(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestPersistenceUnitWithDataSource with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public void testPersistenceUnit() throws Exception {
        //EJB3StandaloneBootstrap.boot(null);
        //EJB3StandaloneBootstrap.scanClasspath();
        
        // This is a transactionally aware EntityManager and must be accessed within a JTA transaction

        EntityManager em = (EntityManager) getInitialContext().lookup("java:/EntityManagers/TestDb");
        assertNotNull(em);
    }

    public static InitialContext getInitialContext() throws Exception {
        Hashtable props = getInitialContextProperties();
        return new InitialContext(props);
    }

    private static Hashtable getInitialContextProperties() {
        Hashtable props = new Hashtable();
        props.put("java.naming.factory.initial", "org.jnp.interfaces.LocalOnlyContextFactory");
        props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        return props;
    }
}
