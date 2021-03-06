package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.business.region.dao.RegionGraphDAO;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public class TestRegionXDAO extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestRegionXDAO.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestRegionXDAO() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }


    /**
     * Creates some reference data for the fixtures
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        TestRegionTools.initialiseAusRegionsX(em);        
    }

    public void testFind() throws Exception {

        Set<Region> australia1 = new RegionGraphDAO(em).findRegionByName("Australia");
        assertNotNull(australia1);
        assertEquals(1, australia1.size());

        Set<Region> australia2 = new RegionGraphDAO(em).findRegionByName(Countries.AU);
        assertNotNull(australia2);
        assertEquals(1, australia2.size());

        Set<Region> australia3 = new RegionGraphDAO(em).findRegionByName("AU");
        assertNotNull(australia3);
        assertEquals(1, australia3.size());


    }
     public void testChildren() throws Exception {
        RegionGraphDAO regionGraphDAO = new RegionGraphDAO(em);

        //Set<RegionProxy> children = regionGraphDAO.findChildren(TestRegionTools.AUSTRALIA);
    }

}
