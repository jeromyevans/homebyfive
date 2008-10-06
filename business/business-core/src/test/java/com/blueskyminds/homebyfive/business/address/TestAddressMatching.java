package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestAddressMatching extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestAddressMatching.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestAddressMatching() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }


    /**
     * Creates some refrence data for the fixtures
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();

        // setup the list of countries and suburbs
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);    
    }

    /** Given an address, we want to find possible matches */
    public void testFuzzyMatch() throws Exception {

        AddressService addressService = new AddressServiceImpl(em);

        Address neutralBay = addressService.parseAddress("Neutral Bay", "AUS");

        assertNotNull(neutralBay);
    }

        /** Given an address, we want to find possible matches */
    public void testFuzzyMatch2() throws Exception {

        AddressService addressService = new AddressServiceImpl(em);

        Address phillipIsland = addressService.parseAddress("Phillip Island", "AUS");

        assertNotNull(phillipIsland);
        LOG.info(phillipIsland);
    }

    /** Given an address, we want to find possible matches */
    public void testCrudRemoval() throws Exception {

        AddressService addressService = new AddressServiceImpl(em);

        Address blackheath = addressService.parseAddress("BLACKHEATH Expressions of interest over NSW", "AUS");

        assertNotNull(blackheath);
    }


    /** Given an address, we want to find possible matches */
    public void testRegionLookup() throws Exception {

        AddressService addressService = new AddressServiceImpl(em);

//        List<Region> matches = addressService.findRegion("Neutral Bay");
//
//        assertNotNull(matches);
//        assertEquals(1, matches.size());
    }
}
