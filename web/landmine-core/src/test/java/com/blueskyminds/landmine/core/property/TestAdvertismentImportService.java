package com.blueskyminds.landmine.core.property;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.patterns.AddressPatternMatcher;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.party.service.PartyService;
import com.blueskyminds.enterprise.party.service.PartyServiceImpl;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.tools.substitutions.SubstitutionsFileReader;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionServiceImpl;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.landmine.core.property.advertisement.*;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementCampaignDAO;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementDAO;
import com.blueskyminds.landmine.core.property.advertisement.service.AdvertisementImportServiceImpl;
import com.blueskyminds.landmine.core.property.advertisement.service.AdvertisementImportService;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.property.patterns.AskingPriceMatcher;
import com.blueskyminds.landmine.core.property.patterns.PropertyTypeMatcher;
import com.blueskyminds.landmine.core.property.service.PremiseService;
import com.blueskyminds.landmine.core.property.service.PremiseServiceImpl;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.housepad.core.property.service.PropertyService;
import com.blueskyminds.housepad.core.property.service.PropertyServiceImpl;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 17/04/2008
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class TestAdvertismentImportService extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestPropertyAdvertisement.class);

    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremisePersistenceUnit";
    private static final String MEASUREMENT_SUBSTITUTIONS = "measurementSubstitutions.csv";
    private static final String PROPERTY_TYPE_SUBSTITUTIONS = "/propertyTypeSubstitutions.csv";
    private static final String ASKING_PRICE_SUBSTITUTIONS = "/askingPriceTypesSubstitutions.csv";
    private static final String PERIOD_TYPE_SUBSTITUTIONS = "/periodTypesSubstitutions.csv";

    private AdvertisementImportService advertisementImportService;

    public TestAdvertismentImportService() {
        super(TEST_PREMISE_PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();

        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);

        loadSubstitutions(MEASUREMENT_SUBSTITUTIONS);
        loadSubstitutions(PROPERTY_TYPE_SUBSTITUTIONS);
        loadSubstitutions(ASKING_PRICE_SUBSTITUTIONS);
        loadSubstitutions(PERIOD_TYPE_SUBSTITUTIONS);

        SuburbEAO suburbEAO = new SuburbEAO(em);

        AddressDAO addressDAO = new AddressDAO(em);
        PremiseEAO premiseEAO = new PremiseEAO(em);
        CountryHandle country = addressDAO.lookupCountry(AddressService.AUS);
        SubstitutionDAO substitutionDAO = new SubstitutionDAO(em);
        SubstitutionService substitutionService = new SubstitutionServiceImpl(substitutionDAO);
        // initialise the address matcher
        AddressPatternMatcher matcher = new AddressPatternMatcher(AddressService.AUS, em);

        AddressService addressService = new AddressServiceImpl(em);
        PropertyEAO propertyEAO = new PropertyEAO(em);
        PropertyService propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);
        PremiseService premiseService = new PremiseServiceImpl(addressService, propertyService, premiseEAO, new AdvertisementCampaignDAO(em));
        PartyService partyService = new PartyServiceImpl(em);

        PropertyTypeMatcher propertyTypeMatcher = new PropertyTypeMatcher(substitutionService);
        AskingPriceMatcher askingPriceMatcher = new AskingPriceMatcher(substitutionService);
        AdvertisementDAO advertisementDAO = new AdvertisementDAO(em);
        advertisementImportService = new AdvertisementImportServiceImpl(addressService, premiseService, partyService, em, propertyTypeMatcher, askingPriceMatcher);
        ((AdvertisementImportServiceImpl) advertisementImportService).setAdvertisementDAO(advertisementDAO);

    }

    private void loadSubstitutions(String filename) {
        int patterns = 0;
        try {
            patterns = SubstitutionsFileReader.readCsvAndPersist(filename, em);

            LOG.info("Initialised "+patterns+" substitutions in "+filename);
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
    }

    public void testAdvertisementImportService() throws Exception {

        PropertyAdvertisementBean bean = prepareTestBean1();
        Long propertyAdvertisementId = advertisementImportService.importAdvertisementBean(bean);

        assertNotNull(propertyAdvertisementId);
        //PropertyAdvertisement propertyAdvertisement = advertisementImportService.lo
    }

    private PropertyAdvertisementBean prepareTestBean1() {
        PropertyAdvertisementBean bean = new PropertyAdvertisementBean();
        bean.setAddress1("196 Seventh Road");   //reference: REIWA 16671
        bean.setAddress2(null);
        bean.setBathrooms(3);
        bean.setBedrooms(1);
        bean.setConstructionDate(null);
        bean.setDescription("196 SEVENTH ROAD, ARMADALE3 BEDROOM, 1 BATHROOM HOUSE WITH LOUNGE/DINING/KITCHEN, SEPARATE FORMAL LOUNGE/FAMILY ROOM, WOOD FIRE, PATIO, UNDERCIVER PARKING, LOCATED ACROSS THE ROAD FROM NEERIGEN BROOK PRIMARY SCHOOL.");
        bean.addFeature("Exterior Construction: See Remarks");
        bean.addFeature("Roof: See Remarks");
        bean.setFloorArea(null);
        bean.setLandArea(null);
        bean.setPostCode(null);
        bean.setPrice("$ 130 Rent per wk.");
        bean.setPropertyType("House");
        bean.setState("WA");
        bean.setSuburb("Armadale");
        bean.setType(PropertyAdvertisementTypes.Lease);
        bean.setCountryISO3Code("AUS");
        PropertyAdvertisementAgencyBean agentBean = new PropertyAdvertisementAgencyBean();
        agentBean.setAgencyName("Kelmscott First National Real Estate");
        agentBean.setAgencyPhone("9495 1212");
        agentBean.setWebsite("www.kelmscottfn.com.au");
        agentBean.setAgencyEmail("properties@kelmscottfn.com.au");
        PropertyAdvertisementContactBean contact = new PropertyAdvertisementContactBean(agentBean);
        contact.setContactName("Sarah Williams");
        agentBean.addContact(contact);

        bean.setAgent(agentBean);
        return bean;
    }

    /**
     * Changes the number of bedrooms
     * @return
     */
    private PropertyAdvertisementBean prepareTestBean2() {
        PropertyAdvertisementBean bean = new PropertyAdvertisementBean();
        bean.setBedrooms(2);
        return bean;
    }

    public void testAdvertisementUpdateService() throws Exception {

        PropertyAdvertisementBean bean = prepareTestBean1();
        Long propertyAdvertisementId = advertisementImportService.importAdvertisementBean(bean);

        assertNotNull(propertyAdvertisementId);

        PropertyAdvertisementBean bean2 = prepareTestBean2();
        Long propertyAdvertisementId2 = advertisementImportService.updateAdvertisementBean(propertyAdvertisementId, bean2);

        assertNotNull(propertyAdvertisementId2);

        //PropertyAdvertisement propertyAdvertisement = advertisementImportService.lo
    }

}