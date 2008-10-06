package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test the creation of sample region data
 *
 * Date Started: 8/07/2007
 * <p/>
 * History:
 */
public class TestRegionSampleData extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestRegionDAO.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestRegionSampleData() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    public void testSetupData() throws Exception {
        AddressTestTools.initialiseCountryList(getConnection());
    }
}
