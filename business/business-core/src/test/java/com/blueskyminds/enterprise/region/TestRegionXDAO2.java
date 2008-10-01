package com.blueskyminds.enterprise.region;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.homebyfive.framework.framework.test.JPATestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * These DAO unit tests use the Country List as the basis for testing
 *
 * Date Started: 16/09/2007
 */
public class TestRegionXDAO2 extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestRegionXDAO2.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private RegionGraphDAO regionGraphDAO;

    public TestRegionXDAO2() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    /**
     * Creates some reference data for the fixtures
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        regionGraphDAO = new RegionGraphDAO(em);
    }


    public void testFindChildren() throws Exception {
        RegionHandle nsw = regionGraphDAO.findRegionByName(TestRegionTools.NSW).iterator().next();
        // find suburbs in NSW
        Set<RegionHandle> children = regionGraphDAO.findChildren(nsw);
        assertNotNull(children);
        assertTrue(children.size() > 0);
    }

    // ------------------------------------------------------------------------------------------------------
}
