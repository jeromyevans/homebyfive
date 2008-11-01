package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.region.dao.RegionGraphDAO;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The RegionDAO provides faster access to the region graph
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestRegionDAO extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestRegionDAO.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestRegionDAO() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }


    /**
     * Creates some reference data for the fixtures
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        //RegionOLD aus = TestRegionTools.initialiseAusRegions();
        //em.persist(aus);
        em.flush();
    }

    public void testDescendants() throws Exception {
        RegionGraphDAO regionGraphDAO = new RegionGraphDAO(em);

        Region aus = new Country("Australia", "au");
        em.persist(aus);

        Region aus2 = regionGraphDAO.findRegionByName(TestRegionTools.AUSTRALIA).iterator().next();
        Region neutralBay = regionGraphDAO.findRegionByName(TestRegionTools.NEUTRAL_BAY).iterator().next();

        // find all descendants of AUS
//        Set<Region> foundDescendants = regionGraphDAO.findDescendants(aus);
//
//        // assert that the set of descendants found by the DAO matches the set found by visiting the graph
//        Set<Region> descendants = aus.getDecendents();
//
//        assertEquals(descendants.size(), foundDescendants.size());
//        assertTrue(descendants.contains(neutralBay));
    }

     public void testAncestors() throws Exception {
        RegionGraphDAO regionGraphDAO = new RegionGraphDAO(em);

        Region neutralBay = regionGraphDAO.findRegionByName(TestRegionTools.NEUTRAL_BAY).iterator().next();
        Region nsw = regionGraphDAO.findRegionByName(TestRegionTools.NSW).iterator().next();

        // find all ancestors of Neutral Bay
//        Set<Region> foundAncestors = regionGraphDAO.findAncestors(neutralBay);
//
//        // assert that the set of ancestors found by the DAO matches the set found by visiting the graph
//        Set<Region> ancestors = neutralBay.getAncestors();
//
//        assertEquals(ancestors.size(), foundAncestors.size());
//        assertTrue(ancestors.contains(nsw));
    }
}
