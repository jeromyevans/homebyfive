package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.dao.RegionGraphDAO;
import com.blueskyminds.homebyfive.business.region.service.RegionGraphService;
import com.blueskyminds.homebyfive.business.region.service.RegionGraphServiceImpl;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestAddressService extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestAddressService.class);
    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private AddressService addressService;

    public TestAddressService() {
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
        RegionGraphDAO regionGraphDAO = new RegionGraphDAO(em);
        RegionGraphService regionGraphService = new RegionGraphServiceImpl(em, regionGraphDAO);
        addressService = new AddressServiceImpl(null, em, regionGraphService);
    }

    /**
     * Tests the creation and persistence of an address
     */
    public void testAddress() throws Exception {
        em.persist(AddressTestTools.testAddressA());
        em.flush();

        List<Address> addresses = new AddressDAO(em).findAll();
        assertEquals(1, addresses.size());
    }

    public void testAddressCreation() throws Exception {
        Address address1 = addressService.lookupOrCreateAddress(AddressTestTools.testAddressA());
        assertNotNull(address1);
        address1.print(System.out);

        List<Address> addresses = new AddressDAO(em).findAll();
        assertEquals(1, addresses.size());
    }

    /**
     * Ensure that streets are reused
     * @throws Exception
     */
    public void testTwoAddressesSameStreet() throws Exception {
        Address address1 = addressService.lookupOrCreateAddress("1/22 Spruson Street, Neutral Bay NSW", "AUS");
        Address address2  = addressService.lookupOrCreateAddress("11 Spruson Street, Neutral Bay NSW", "AUS");
        assertNotNull(address1);
        address1.print(System.out);
        assertNotNull(address2);
        address2.print(System.out);
        assertEquals(((StreetAddress) address1).getStreet(), ((StreetAddress) address2).getStreet());


        address1 = addressService.lookupOrCreateAddress("Apt5305/560 Lygon Street Carlton VIC", "AUS");
        address2  = addressService.lookupOrCreateAddress("Ic03, 570 Lygon Street Carlton VIC", "AUS");
        assertNotNull(address1);
        address1.print(System.out);
        assertNotNull(address2);
        address2.print(System.out);
        assertEquals(((StreetAddress) address1).getStreet(), ((StreetAddress) address2).getStreet());
    }

    /**
     * Tests the lookup of a suburb by name
     */
    public void testSuburbSearch() throws Exception {
        List<Suburb> sub1 = addressService.findSuburbLike("Neutral Bay", "AUS");
        List<Suburb> sub2 = addressService.findSuburbLike("NeutralBay", "AUS");
        List<Suburb> sub3 = addressService.findSuburbLike("Neutrel Bay", "AUS");

        assertEquals(1, sub1.size());
        assertEquals(1, sub2.size());
        assertEquals(1, sub3.size());

        // try some places that aren't unique names
        List<Suburb> sub4 = addressService.findSuburbLike("Aberdeen", "AUS");
        List<Suburb> sub5 = addressService.findSuburbLike("Aberdean", "AUS");
        List<Suburb> sub6 = addressService.findSuburbLike("Aberden", "AUS");

        DebugTools.printCollection(sub6);
        assertEquals(2, sub4.size());
        assertEquals(2, sub5.size());
        assertEquals(3, sub6.size());   // two aberdeens and aberdare


    }

     public void testFindCountry() throws Exception {
        List<Country> countries1 = addressService.findCountry("Australia");
        assertNotNull(countries1);
        assertEquals(1, countries1.size());
        Country x = countries1.get(0);
        assertEquals("Australia", x.getName());

        List<Country> countries2 = addressService.findCountry("AUS");
        assertNotNull(countries2);
        assertEquals(1, countries2.size());
        assertEquals("Australia", countries2.iterator().next().getName());

        List<Country> countries3 = addressService.findCountry("Australa");
        assertNotNull(countries3);
        assertEquals(2, countries3.size());
        assertEquals("Australia", countries3.iterator().next().getName());
    }

    public void testFindSuburb() throws Exception {

        List<Suburb> suburbs1 = addressService.findSuburb("Neutral Bay", "AUS");
        assertNotNull(suburbs1);
        assertEquals(1, suburbs1.size());
        assertEquals("Neutral Bay", suburbs1.get(0).getName());
    }

    public void testListAddressesBySuburb() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        List<Suburb> suburbs = addressService.findSuburb("Carlton", "AUS");
        for (Suburb suburb : suburbs) {
            Suburb carlton = (Suburb) suburb;

            Set<Address> addresses = addressService.listAddresses(carlton);
            for (Address adddress : addresses) {
                adddress.print(System.out);
            }
        }
    }

    public void testListAddressesByPostCode() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        List<PostalCode> postcodes = addressService.findPostCode("3053", "AUS");
        for (PostalCode postCodeHandle : postcodes) {
            Set<Address> addresses = addressService.listAddresses(postCodeHandle);
            for (Address adddress : addresses) {
                adddress.print(System.out);
            }
        }
    }

    public void testListAddressesByStreet() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        List<Street> streets = addressService.findStreet("lygon", "AUS");
        for (Street street : streets) {
            Set<Address> addresses = addressService.listAddresses(street);
            for (Address adddress : addresses) {
                adddress.print(System.out);
            }
        }
    }

   /**
     * Ensure that streets are reused
    *
    * Address1 exists, address2 doesn't
     * @throws Exception
     */
   public void testLookupOrCreateTwoAddressesExistingSameStreet() throws Exception {
       AddressTestTools.initialiseSampleAusAddresses();

       Address existingAddress = addressService.lookupOrCreateAddress("Apt5305/560 Lygon Street Carlton VIC", "AUS");
       Address newAddressSameStreet = addressService.lookupOrCreateAddress("Ic03, 571 Lygon Street Carlton VIC", "AUS");
       assertNotNull(existingAddress);
       existingAddress.print(System.out);
       assertNotNull(newAddressSameStreet);
       newAddressSameStreet.print(System.out);
       assertEquals(((StreetAddress) existingAddress).getStreet(), ((StreetAddress) newAddressSameStreet).getStreet());
   }

    public void testAutocomplete() throws Exception {
        List<Region> regionHandles = addressService.autocompleteRegion("Bull");
        assertNotNull(regionHandles);
        DebugTools.printCollection(regionHandles);
    }
}
