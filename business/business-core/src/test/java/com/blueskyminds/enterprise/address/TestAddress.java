package com.blueskyminds.enterprise.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.test.TestTools;
import com.blueskyminds.enterprise.region.graph.*;
import com.blueskyminds.enterprise.region.RegionFactory;
import com.blueskyminds.enterprise.region.graph.SuburbHandle;
import com.blueskyminds.enterprise.region.graph.RegionHandle;
import com.blueskyminds.enterprise.AddressTestTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test Persistence of an Address
 *
 * Date Started: 8/06/2006
 */
public class TestAddress extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestAddress.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    public TestAddress() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    public void testAddress() {
        CountryHandle australia = new RegionFactory().createCountry("Australia");

        StateHandle nsw = australia.addState(new RegionFactory().createState("New South Wales", "NSW"));

        PostCodeHandle nbPostCode = nsw.addPostCode(new RegionFactory().createPostCode("2089"));
        PostCodeHandle kPostCode = nsw.addPostCode(new RegionFactory().createPostCode("2060"));
        PostCodeHandle mPostCode = nsw.addPostCode(new RegionFactory().createPostCode("2088"));

        SuburbHandle mosmon = nsw.addSuburb(new RegionFactory().createSuburb("Mosmon", mPostCode));
        SuburbHandle neutralBay = nsw.addSuburb(new RegionFactory().createSuburb("Neutral Bay", nbPostCode));
        SuburbHandle kirribilli = nsw.addSuburb(new RegionFactory().createSuburb("Kirribilli", kPostCode));

        StreetHandle sprusonStreet = neutralBay.addStreet(new StreetHandle("Spruson", StreetType.Street));
        StreetHandle philipStreet = neutralBay.addStreet(new StreetHandle("Phillip", StreetType.Street));
        StreetHandle militaryRoad = neutralBay.addStreet(new StreetHandle("Military", StreetType.Road));
        mosmon.addStreet(militaryRoad);
        StreetHandle carabellaStreet = kirribilli.addStreet(new StreetHandle("Carabella", StreetType.Street));

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
