package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.test.TestTools;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.street.StreetHandle;
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
        CountryHandle aus = addressDAO.lookupCountry("AUS");
        assertNotNull(aus);
    }

    public void testListSuburbsInCountry() throws Exception {
        CountryHandle aus = addressDAO.lookupCountry("AUS");

        Set<SuburbHandle> suburbs = addressDAO.listSuburbsInCountry(aus);
        assertNotNull(suburbs);
        assertTrue(suburbs.size() > 0);
    }

    public void testListSuburbsInState() throws Exception {
        CountryHandle aus = addressDAO.lookupCountry("AUS");
        StateHandle state = addressDAO.lookupState("NSW", aus);

        Set<SuburbHandle> suburbs = addressDAO.listSuburbsInState(state);
        assertNotNull(suburbs);
        assertTrue(suburbs.size() > 0);
    }

    public void testListStreetsInCountry() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        CountryHandle aus = addressDAO.lookupCountry("AUS");

        Set<StreetHandle> streets = addressDAO.listStreetsInCountry(aus);
        assertNotNull(streets);
        assertTrue(streets.size() > 0);
    }

    public void testListStreetsInSuburb() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        CountryHandle aus = addressDAO.lookupCountry("AUS");
        StateHandle state = addressDAO.lookupState("VIC", aus);
        SuburbHandle suburb = addressDAO.lookupSuburb("Carlton", state);
        Set<StreetHandle> streets = addressDAO.listStreetsInSuburb(suburb);
        assertNotNull(streets);
        assertTrue(streets.size() > 0);
    }


    public void testCountries() throws Exception {
        AddressTestTools.initialiseCountryList();

        TestTools.printAll(em, CountryHandle.class);
        TestTools.printAll(em, StateHandle.class);

        CountryHandle australia = addressDAO.lookupCountry("AUS");

        Collection<PostCodeHandle> postCodes = addressDAO.listPostCodesInCountry(australia);
        DebugTools.printCollection(postCodes);
    }

    public void testSuburbs() {
        AddressTestTools.initialiseCountryList();

        TestTools.printAll(em, StateHandle.class);
        TestTools.printAll(em, PostCodeHandle.class);
        TestTools.printAll(em, SuburbHandle.class);
    }

}