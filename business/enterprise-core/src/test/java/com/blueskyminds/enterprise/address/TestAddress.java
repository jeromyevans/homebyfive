package com.blueskyminds.enterprise.address;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.country.CountryFactory;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.state.StateFactory;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeFactory;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbFactory;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.AddressTestTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test Persistence of an Address
 *
 * Date Started: 8/06/2006
 */
public class TestAddress extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestAddress.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestAddress() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    public void testAddress() {
        CountryHandle australia = new CountryFactory().createCountry("Australia");

        StateHandle nsw = australia.addState(new StateFactory().createState("New South Wales", "NSW"));

        PostCodeHandle nbPostCode = nsw.addPostCode(new PostCodeFactory().createPostCode("2089"));
        PostCodeHandle kPostCode = nsw.addPostCode(new PostCodeFactory().createPostCode("2060"));
        PostCodeHandle mPostCode = nsw.addPostCode(new PostCodeFactory().createPostCode("2088"));

        SuburbHandle mosmon = nsw.addSuburb(new SuburbFactory().createSuburb("Mosmon", mPostCode));
        SuburbHandle neutralBay = nsw.addSuburb(new SuburbFactory().createSuburb("Neutral Bay", nbPostCode));
        SuburbHandle kirribilli = nsw.addSuburb(new SuburbFactory().createSuburb("Kirribilli", kPostCode));

        Street sprusonStreet = neutralBay.addStreet(new Street("Spruson", StreetType.Street));
        Street philipStreet = neutralBay.addStreet(new Street("Phillip", StreetType.Street));
        Street militaryRoad = neutralBay.addStreet(new Street("Military", StreetType.Road));
        mosmon.addStreet(militaryRoad);
        Street carabellaStreet = kirribilli.addStreet(new Street("Carabella", StreetType.Street));

        Address addressA = new UnitAddress("1", "22", sprusonStreet, neutralBay, nbPostCode);
        Address addressB = new UnitAddress("3", "60", carabellaStreet, kirribilli, kPostCode);

        em.persist(australia);
        em.persist(addressA);
        em.persist(addressB);
        em.flush();

        TestTools.printAll(em, RegionHandle.class);
        //TestTools.findById(RegionHandle.class, australia.getId(), em).printHierarchy(System.out);

        TestTools.printAll(em, SuburbHandle.class);
        TestTools.printAll(em, PostCodeHandle.class);
        TestTools.printAll(em, Street.class);
        TestTools.printAll(em, Address.class);
    }

    public void testAddressRegistry() {
        AddressTestTools.initialiseAddresses(em);

        Address address = AddressTestTools.testAddressA();

        TestTools.printAll(em, SuburbHandle.class);
        TestTools.printAll(em, PostCodeHandle.class);
        TestTools.printAll(em, Street.class);
        TestTools.printAll(em, Address.class);
    }
}
