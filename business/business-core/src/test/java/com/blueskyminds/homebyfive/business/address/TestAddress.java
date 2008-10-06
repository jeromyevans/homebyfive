package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.test.TestTools;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.RegionFactory;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.AddressTestTools;
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
        Country australia = new RegionFactory().createCountry("Australia", "AU");

        State nsw = australia.addState(new RegionFactory().createState("New South Wales", "NSW", australia));

        PostalCode nbPostCode = nsw.addPostCode(new RegionFactory().createPostCode("2089", nsw));
        PostalCode kPostCode = nsw.addPostCode(new RegionFactory().createPostCode("2060", nsw));
        PostalCode mPostCode = nsw.addPostCode(new RegionFactory().createPostCode("2088", nsw));

        Suburb mosmon = nsw.addSuburb(new RegionFactory().createSuburb("Mosmon", nsw, mPostCode));
        Suburb neutralBay = nsw.addSuburb(new RegionFactory().createSuburb("Neutral Bay", nsw, nbPostCode));
        Suburb kirribilli = nsw.addSuburb(new RegionFactory().createSuburb("Kirribilli", nsw, kPostCode));

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

        TestTools.printAll(em, Region.class);
        //TestTools.findById(RegionHandle.class, australia.getId(), em).printHierarchy(System.out);

        TestTools.printAll(em, Suburb.class);
        TestTools.printAll(em, PostalCode.class);
        TestTools.printAll(em, Street.class);
        TestTools.printAll(em, Address.class);
    }

    public void testAddressRegistry() {
        AddressTestTools.initialiseAddresses(em);

        Address address = AddressTestTools.testAddressA();

        TestTools.printAll(em, Suburb.class);
        TestTools.printAll(em, PostalCode.class);
        TestTools.printAll(em, Street.class);
        TestTools.printAll(em, Address.class);
    }
}
