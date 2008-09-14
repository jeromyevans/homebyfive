package com.blueskyminds.framework;

import com.blueskyminds.framework.tools.PropertiesContext;
import com.blueskyminds.framework.test.BaseTestCase;

/**
 * Unit tests for the PropertiesContext
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestPropertiesContext extends BaseTestCase {

    public TestPropertiesContext(String string) {
        super(string);
    }

    public void testConfiguration() {
        PropertiesContext properties = new PropertiesContext();
        properties.loadProperties("servicelocator.properties");

        String value = properties.getProperty("persistence.service");

        System.out.println(value);
        assertNotNull(value);
    }
}
