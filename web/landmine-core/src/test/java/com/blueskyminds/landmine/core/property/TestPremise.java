package com.blueskyminds.landmine.core.property;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.landmine.core.property.service.PremiseService;
import com.blueskyminds.landmine.core.property.service.PremiseServiceImpl;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.property.events.Constructed;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementCampaignDAO;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.housepad.core.property.service.PropertyService;
import com.blueskyminds.housepad.core.property.service.PropertyServiceImpl;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;

import java.util.Date;
import java.util.List;

/**
 * Tests the method for creating/updating premise details
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestPremise extends OutOfContainerTestCase {

    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremisePersistenceUnit";

    // ------------------------------------------------------------------------------------------------------

    public TestPremise() {
        super(TEST_PREMISE_PERSISTENCE_UNIT);
    }

    // ------------------------------------------------------------------------------------------------------

    public void testPropertyAttributes() {
        Premise premise = new Premise();

        premise.associateAddress(AddressTestTools.testAddressA(), new Date());

        PremiseAttributeSet specA = new PremiseAttributeSet(new Date());
        specA.setBedrooms(3);
        specA.setBathrooms(2);
        specA.setType(PropertyTypes.Unit);

        PremiseAttributeSet specB = new PremiseAttributeSet(new Date());
        specB.setBedrooms(3);
        specB.setType(PropertyTypes.Villa);

        // save the feature specififcation
        em.persist(specA);
        em.persist(specB);


        em.persist(premise);
//
//            premise.associateWithAttributes(specA);
//
//            gateway.save(premise);

        premise.associateWithAttributes(specA);
        premise.associateWithAttributes(specB);

        em.persist(premise);

        em.flush();

        premise.addEvent(new Constructed(premise, new Date()));
        
        em.persist(premise);

        em.flush();

        TestTools.printAll(em, PremiseAttributeSet.class);
        TestTools.printAll(em, Premise.class);
    }

    public void testAddress() throws Exception  {
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);

        em.persist(AddressTestTools.testAddressA());
        em.flush();

        List<Address> addresses = new AddressDAO(em).findAll();
        assertEquals(1, addresses.size());
    }

    public void testPremiseLookup() throws Exception  {
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);

        AddressService addressService = new AddressServiceImpl(em);
        PremiseEAO premiseEAO = new PremiseEAO(em);
        PropertyEAO propertyEAO = new PropertyEAO(em);
        SuburbEAO suburbEAO = new SuburbEAO(em);
        PropertyService propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);
        PremiseService premiseService = new PremiseServiceImpl(addressService, propertyService, premiseEAO, new AdvertisementCampaignDAO(em));

        Address address1 = addressService.lookupOrCreateAddress(AddressTestTools.testAddressA());
        assertNotNull(address1);
        address1.print(System.out);

        List<Address> addresses = new AddressDAO(em).findAll();
        assertEquals(1, addresses.size());

        Premise premise = premiseService.lookupOrCreatePremise(address1, PropertyTypes.House, null, null);
        
        assertNotNull(premise);
    }

    // ------------------------------------------------------------------------------------------------------
}
