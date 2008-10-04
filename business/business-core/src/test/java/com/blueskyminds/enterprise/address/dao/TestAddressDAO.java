package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.test.TestTools;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.region.graph.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;
import java.util.Collection;

/**
 * Date Started: 26/10/2007
 * <p/>
 * History:
 */
public class TestAddressDAO extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestAddressDAO.class);
    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private AddressDAO addressDAO;

    public TestAddressDAO() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    /**
     * Creates some reference data
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        addressDAO = new AddressDAO(em);
    }

    public void testGetCountry() throws Exception {
        Country aus = addressDAO.lookupCountry("AUS");
        assertNotNull(aus);
    }

    public void testListSuburbsInCountry() throws Exception {
        Country aus = addressDAO.lookupCountry("AUS");

        Set<Suburb> suburbs = addressDAO.listSuburbsInCountry(aus);
        assertNotNull(suburbs);
        assertTrue(suburbs.size() > 0);
    }

    public void testListSuburbsInState() throws Exception {
        Country aus = addressDAO.lookupCountry("AUS");
        State state = addressDAO.lookupState("NSW", aus);

        Set<Suburb> suburbs = addressDAO.listSuburbsInState(state);
        assertNotNull(suburbs);
        assertTrue(suburbs.size() > 0);
    }

    public void testListStreetsInCountry() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        Country aus = addressDAO.lookupCountry("AUS");

        Set<Street> streets = addressDAO.listStreetsInCountry(aus);
        assertNotNull(streets);
        assertTrue(streets.size() > 0);
    }

    public void testListStreetsInSuburb() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        Country aus = addressDAO.lookupCountry("AUS");
        State state = addressDAO.lookupState("VIC", aus);
        Suburb suburb = addressDAO.lookupSuburb("Carlton", state);
        Set<Street> streets = addressDAO.listStreetsInSuburb(suburb);
        assertNotNull(streets);
        assertTrue(streets.size() > 0);
    }


    public void testCountries() throws Exception {
        AddressTestTools.initialiseCountryList();

        TestTools.printAll(em, Country.class);
        TestTools.printAll(em, State.class);

        Country australia = addressDAO.lookupCountry("AUS");

        Collection<PostalCode> postCodes = addressDAO.listPostCodesInCountry(australia);
        DebugTools.printCollection(postCodes);
    }

    public void testSuburbs() {
        AddressTestTools.initialiseCountryList();

        TestTools.printAll(em, State.class);
        TestTools.printAll(em, PostalCode.class);
        TestTools.printAll(em, Suburb.class);
    }

}