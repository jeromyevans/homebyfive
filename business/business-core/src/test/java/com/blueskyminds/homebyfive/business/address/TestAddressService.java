package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.dao.RegionGraphDAO;
import com.blueskyminds.homebyfive.business.region.service.RegionGraphService;
import com.blueskyminds.homebyfive.business.region.service.RegionGraphServiceImpl;
import com.blueskyminds.homebyfive.business.region.service.SuburbService;
import com.blueskyminds.homebyfive.business.region.service.SuburbServiceImpl;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

/**
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestAddressService extends AddressTestCase {

    private static final Log LOG = LogFactory.getLog(TestAddressService.class);

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
        Address address1 = addressService.lookupOrCreateAddress("1/22 Spruson Street, Neutral Bay NSW", "AU");
        Address address2  = addressService.lookupOrCreateAddress("11 Spruson Street, Neutral Bay NSW", "AU");
        assertNotNull(address1);
        address1.print(System.out);
        assertNotNull(address2);
        address2.print(System.out);
        assertEquals(((StreetAddress) address1).getStreet(), ((StreetAddress) address2).getStreet());


        address1 = addressService.lookupOrCreateAddress("Apt5305/560 Lygon Street Carlton VIC", "AU");
        address2  = addressService.lookupOrCreateAddress("Ic03, 570 Lygon Street Carlton VIC", "AU");
        assertNotNull(address1);
        address1.print(System.out);
        assertNotNull(address2);
        address2.print(System.out);
        assertEquals(((StreetAddress) address1).getStreet(), ((StreetAddress) address2).getStreet());
    }

    

    public void testListAddressesBySuburb() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        List<Suburb> suburbs = suburbService.find("Carlton", Countries.AU);
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

        List<PostalCode> postcodes = postalCodeService.find("3053", Countries.AU);
        for (PostalCode postCodeHandle : postcodes) {
            Set<Address> addresses = addressService.listAddresses(postCodeHandle);
            for (Address adddress : addresses) {
                adddress.print(System.out);
            }
        }
    }

    public void testListAddressesByStreet() throws Exception {
        AddressTestTools.initialiseSampleAusAddresses();

        List<Street> streets = streetService.find("lygon", Countries.AU);
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

       Address existingAddress = addressService.lookupOrCreateAddress("Apt5305/560 Lygon Street Carlton VIC", "AU");
       Address newAddressSameStreet = addressService.lookupOrCreateAddress("Ic03, 571 Lygon Street Carlton VIC", "AU");
       assertNotNull(existingAddress);
       existingAddress.print(System.out);
       assertNotNull(newAddressSameStreet);
       newAddressSameStreet.print(System.out);
       assertEquals(((StreetAddress) existingAddress).getStreet(), ((StreetAddress) newAddressSameStreet).getStreet());
   }


   /**
     * If the suburb isn't known, ensure that its created and reparsed
    *
     * @throws Exception
     */
   public void testLookupOrCreateAddressInNewSuburb() throws Exception {
       AddressTestTools.initialiseSampleAusAddresses();

       Address existingAddress = addressService.lookupOrCreateAddress("24 Lygon Street", "Phillip Island", "VIC", "AU");
       assertNotNull(existingAddress);
       existingAddress.print(System.out);
   }

   public void testParseAddressInSuburb() throws Exception {
         Address streetAddress = addressService.parseAddress("58 Post Office Road", "GLENORIE", "NSW", "AU");
         assertNotNull(streetAddress);
         assertEquals("Glenorie", streetAddress.getSuburb().getName());
    }
}
