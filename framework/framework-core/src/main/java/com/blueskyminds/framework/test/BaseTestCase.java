package com.blueskyminds.framework.test;

import junit.framework.TestCase;
import com.blueskyminds.framework.tools.LoggerTools;
import com.blueskyminds.framework.tools.PropertiesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * Provides common methods for JUnit tests
 *
 * Date Started: 26/03/2006
 *
 * History:
 *    16 Apr 2006 - moved to the framework.tests package
 * --- Blue Sky Minds Pty Ltd ------------------------------------------------------------------------------
 */
public class BaseTestCase extends TestCase {

    private static final Log LOG = LogFactory.getLog(BaseTestCase.class);
    private static final String BASE_TEST_CASE_PROPERTIES = "baseTestCase.properties";

    private PropertiesContext propertiesContext;

    // ------------------------------------------------------------------------------------------------------

    public BaseTestCase(String string) {
        super(string);
        LoggerTools.configure();
        init();
    }

    public BaseTestCase() {
        LoggerTools.configure();
        init();
    }

    private void init() {
        propertiesContext = new PropertiesContext();
        propertiesContext.loadProperties(BASE_TEST_CASE_PROPERTIES);
    }

    public PropertiesContext getPropertiesContext() {
        return propertiesContext;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
