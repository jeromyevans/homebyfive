package com.blueskyminds.homebyfive.framework.core;

import com.blueskyminds.homebyfive.framework.core.tools.PropertiesContext;
import com.blueskyminds.homebyfive.framework.core.test.BaseTestCase;
import junit.framework.TestCase;

import java.util.Properties;

/**
 * Unit tests for the PropertiesContext
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd
 */
public class PropertiesContextTest extends TestCase {

    public void testConfiguration() {
        PropertiesContext properties = new PropertiesContext();
        properties.loadProperties("servicelocator.properties");

        String value = properties.getProperty("persistence.service");

        System.out.println(value);
        assertNotNull(value);
    }

    /** Assert that the hostname-specific properties take precedence */
    public void testLocalMachineProperties() {
        PropertiesContext.setEmulatedHostName("hosttest");
        Properties properties = PropertiesContext.loadPropertiesFile("propertiesContextTest.properties");
        assertNotNull(properties);
        assertEquals("hosttest", properties.getProperty("property1"));
    }
}
