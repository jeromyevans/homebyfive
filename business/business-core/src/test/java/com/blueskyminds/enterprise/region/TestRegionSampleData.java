package com.blueskyminds.enterprise.region;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.persistence.jdbc.PersistenceTools;
import com.blueskyminds.enterprise.AddressTestTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test the creation of sample region data
 *
 * Date Started: 8/07/2007
 * <p/>
 * History:
 */
public class TestRegionSampleData extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestRegionDAO.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestRegionSampleData() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    public void testSetupData() throws Exception {
        AddressTestTools.initialiseCountryList(getConnection());
    }
}
