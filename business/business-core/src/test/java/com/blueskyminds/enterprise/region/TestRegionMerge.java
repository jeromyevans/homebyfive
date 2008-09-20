package com.blueskyminds.enterprise.region;

import com.blueskyminds.framework.test.JPATestCase;
import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;

/**
 * Date: 31/03/2008
*/
public class TestRegionMerge extends JPATestCase {

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private RegionGraphDAO regionGraphDAO;

    public TestRegionMerge() {
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

        newTransaction(); // clear hashsets to enable remove functions

        regionGraphDAO = new RegionGraphDAO(em);
        RegionHandle bullcreek = regionGraphDAO.findRegionByName(TestRegionTools.BULL_CREEK).iterator().next();
        RegionHandle willetton = regionGraphDAO.findRegionByName(TestRegionTools.WILLETTON).iterator().next();

        RegionHandle state1 = regionGraphDAO.findRegionByName("WA").iterator().next();
        RegionHandle postCode1 = regionGraphDAO.findRegionByName("6149").iterator().next();
        RegionHandle postCode2 = regionGraphDAO.findRegionByName("6155").iterator().next();


        assertTrue(state1.hasChild(bullcreek));
        assertTrue(state1.hasChild(willetton));
        assertTrue(postCode1.hasChild(bullcreek));
        assertFalse(postCode2.hasChild(bullcreek));
        assertFalse(postCode1.hasChild(willetton));
        assertTrue(postCode2.hasChild(willetton));
       
        bullcreek.mergeWith(willetton);

        em.flush();

        assertTrue(state1.hasChild(bullcreek));
        assertFalse(state1.hasChild(willetton));
        assertTrue(postCode1.hasChild(bullcreek));
        assertTrue(postCode2.hasChild(bullcreek));
        assertFalse(postCode1.hasChild(willetton));
        assertFalse(postCode2.hasChild(willetton));

    }
}
