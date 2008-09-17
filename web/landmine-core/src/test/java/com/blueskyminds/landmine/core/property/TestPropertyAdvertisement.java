package com.blueskyminds.landmine.core.property;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.PlainTextAddress;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.patterns.AddressPatternMatcher;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.contact.*;
import com.blueskyminds.enterprise.party.*;
import com.blueskyminds.enterprise.party.service.PartyService;
import com.blueskyminds.enterprise.party.service.PartyServiceImpl;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.measurement.UnitsOfArea;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.framework.tools.substitutions.SubstitutionsFileReader;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionServiceImpl;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.landmine.core.property.advertisement.*;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementDAO;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementCampaignDAO;
import com.blueskyminds.landmine.core.property.advertisement.service.AdvertisementImportServiceImpl;
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

import java.util.Collection;
import java.util.Currency;
import java.util.Date;

/**
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestPropertyAdvertisement extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestPropertyAdvertisement.class);

    private static final String TEST_PREMISE_PERSISTENCE_UNIT = "TestPremisePersistenceUnit";

    private SuburbEAO suburbEAO;

    public TestPropertyAdvertisement() {
        super(TEST_PREMISE_PERSISTENCE_UNIT);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestPropertyAdvertisement with default attributes
     */
    private void init() {
    }

    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.

        suburbEAO = new SuburbEAO(em);
    }

    // ------------------------------------------------------------------------------------------------------

    public void testPropertyAdvertisement() {
        PropertyAdvertisement advertisement = new PropertyAdvertisement(PropertyAdvertisementTypes.Lease);
        advertisement.setAddress(new PlainTextAddress("1/22 Spruson Street, Neutral Bay NSW 2089"));
        advertisement.setDateListed(new Date());
        advertisement.setDescription("Sample description");
        advertisement.setPrice(new AskingPrice(new Money(300, Currency.getInstance("AUD")), PeriodTypes.Week));

        PremiseAttributeSet attributeSet = new PremiseAttributeSet(new Date());
        attributeSet.setBedrooms(3);
        attributeSet.setBathrooms(2);
        attributeSet.setType(PropertyTypes.Unit);
        attributeSet.setBuildingArea(new Area(120, UnitsOfArea.SquareMetre));
        advertisement.setAttributes(attributeSet);

        em.persist(advertisement);
        em.flush();

        TestTools.printAll(em, PropertyAdvertisement.class);
    }

    // ------------------------------------------------------------------------------------------------------

    private static final String MEASUREMENT_SUBSTITUTIONS = "measurementSubstitutions.csv";
    private static final String PROPERTY_TYPE_SUBSTITUTIONS = "/propertyTypeSubstitutions.csv";
    private static final String ASKING_PRICE_SUBSTITUTIONS = "/askingPriceTypesSubstitutions.csv";
    private static final String PERIOD_TYPE_SUBSTITUTIONS = "/periodTypesSubstitutions.csv";

    public void testPropertyAdvertisementBeanConversion() throws Exception {
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);

        loadSubstitutions(MEASUREMENT_SUBSTITUTIONS);
        loadSubstitutions(PROPERTY_TYPE_SUBSTITUTIONS);
        loadSubstitutions(ASKING_PRICE_SUBSTITUTIONS);
        loadSubstitutions(PERIOD_TYPE_SUBSTITUTIONS);

        PropertyAdvertisementBean bean = new PropertyAdvertisementBean();
        bean.setDateEntered(new Date());
        bean.setAddress1("196 Seventh Road");   //reference: REIWA 16671
        bean.setAddress2(null);
        bean.setBathrooms(0);
        bean.setBedrooms(0);
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
        PropertyAdvertisementAgencyBean agentBean = new PropertyAdvertisementAgencyBean();
        agentBean.setAgencyName("Kelmscott First National Real Estate");
        agentBean.setAgencyPhone("9495 1212");
        agentBean.setWebsite("www.kelmscottfn.com.au");
        agentBean.setAgencyEmail("properties@kelmscottfn.com.au");
        PropertyAdvertisementContactBean contact = new PropertyAdvertisementContactBean(agentBean);
        contact.setContactName("Sarah Williams");
        agentBean.addContact(contact);
        bean.setAgent(agentBean);

        SubstitutionDAO substitutionDAO = new SubstitutionDAO(em);
        SubstitutionService substitutionService = new SubstitutionServiceImpl(substitutionDAO);
        PremiseEAO premiseEAO = new PremiseEAO(em);
        //AddressPatternMatcher addressPatternMatcher = new AddressPatternMatcher("AUS", em);
        //addressPatternMatcher.setSubstitutionService(substitutionDAO);

        AddressService addressService = new AddressServiceImpl(em);
        PropertyEAO propertyEAO = new PropertyEAO(em);
        SuburbEAO suburbEAO = new SuburbEAO(em);
        PropertyService propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);
        PremiseService premiseService = new PremiseServiceImpl(addressService, propertyService, premiseEAO, new AdvertisementCampaignDAO(em));
        PartyService partyService = new PartyServiceImpl(em);

        PropertyTypeMatcher propertyTypeMatcher = new PropertyTypeMatcher(substitutionService);
        AskingPriceMatcher askingPriceMatcher = new AskingPriceMatcher(substitutionService);

        AdvertisementImportServiceImpl importService = new AdvertisementImportServiceImpl(addressService, premiseService, partyService, em, propertyTypeMatcher, askingPriceMatcher);

        // convert the bean to a PropertyAdvertisement
        PropertyAdvertisement propertyAdvertisement = new PropertyAdvertisement(bean.getType());
        propertyAdvertisement.setAddress(addressService.parseAddress(bean.getAddressString(), "AUS"));
        PremiseAttributeSet attributes = new PremiseAttributeSet(bean.getDateEntered());
        attributes.setBathrooms(bean.getBathrooms());
        attributes.setBedrooms(bean.getBedrooms());
        attributes.setBuildingArea((bean.getFloorArea() != null ? new Area(bean.getFloorArea(), UnitsOfArea.SquareMetre) : null)); // todo: sqm assumption
        attributes.setLandArea((bean.getLandArea() != null ? new Area(bean.getLandArea(), UnitsOfArea.SquareMetre) : null)); // todo: sqm assumption
        attributes.setType(propertyTypeMatcher.extractBest(bean.getPropertyType()));
        propertyAdvertisement.setAttributes(attributes);
        propertyAdvertisement.setDateListed(bean.getDateEntered());
        propertyAdvertisement.setDateUnlisted(null);
        propertyAdvertisement.setDescription(bean.getDescription());
        AskingPrice price = askingPriceMatcher.extractBest(bean.getPrice());
        if (PeriodTypes.OnceOff.equals(price.getPeriod())) {
            if (PropertyAdvertisementTypes.Lease.equals(propertyAdvertisement.getType())) {
                price.setPeriod(PeriodTypes.Week);
            }
        }
        propertyAdvertisement.setPrice(price);//

        // get the premise...
        Premise premise = premiseService.lookupOrCreatePremise(propertyAdvertisement.getAddress(), propertyAdvertisement.getAttributes().getType(), null, null);
        // add associate the advertisement with it
        premise = importService.associateAdvertisement(premise, propertyAdvertisement);

        // lookup or create the organization
        // lookup or create the agentBean(s)
        Organisation agency = new Company(agentBean.getAgencyName());
        agency.addWebsite(new Website(agentBean.getWebsite(), POCRole.Business));
        agency.addPhoneNumber(new PhoneNumber(agentBean.getAgencyPhone(), POCRole.Business, PhoneNumberTypes.Fixed));
        agency.addPhoneNumber(new PhoneNumber(agentBean.getAgencyFax(), POCRole.Business, PhoneNumberTypes.Fixed));

        PropertyAdvertisementContactBean agentContact = agentBean.getContacts().get(0);
        Individual agent = new Individual(agentContact.getContactName());
        agent.addEmailAddress(new EmailAddress(agentContact.getEmail(), POCRole.Business));
        agent.addPhoneNumber(new PhoneNumber(agentContact.getMobile(), POCRole.Business, PhoneNumberTypes.Mobile));

        agent = partyService.createOrMergeIndividual(agent);

        agency.addIndividualRelationship(new IndividualRelationship(agency, agent, "Agent", new IndividualRole("Agent")));
        //importService.importAdvertisement(propertyAdvertisementBean);

        agency = partyService.createOrMergeOrganisation(agency);
    }

    // ------------------------------------------------------------------------------------------------------


    private void loadSubstitutions(String filename) {
        int patterns = 0;
        try {
            patterns = SubstitutionsFileReader.readCsvAndPersist(filename, em);

            LOG.info("Initialised "+patterns+" substitutions in "+filename);
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Tests the association of an Advertisement with a property
     */
    public void testAdvertisementImport() throws Exception {
        int count = 20;
        int advertisementCount = 0;
        AddressTestTools.initialiseCountryList();

        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        PremiseTestTools.initialiseExampleAdvertisements(em);

        AddressDAO addressDAO = new AddressDAO(em);
        PremiseEAO premiseEAO = new PremiseEAO(em);
        CountryHandle country = addressDAO.lookupCountry(AddressService.AUS);
        SubstitutionDAO substitutionDAO = new SubstitutionDAO(em);
        
        // initialise the address matcher
        AddressPatternMatcher matcher = new AddressPatternMatcher(AddressService.AUS, em);

        AddressService addressService = new AddressServiceImpl(em);
        PropertyEAO propertyEAO = new PropertyEAO(em);
        PropertyService propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);
        PremiseService premiseService = new PremiseServiceImpl(addressService, propertyService, premiseEAO, new AdvertisementCampaignDAO(em));
        PartyService partyService = new PartyServiceImpl(em);


        SubstitutionService substitutionService = new SubstitutionServiceImpl(substitutionDAO);

        PropertyTypeMatcher propertyTypeMatcher = new PropertyTypeMatcher(substitutionService);
        AskingPriceMatcher askingPriceMatcher = new AskingPriceMatcher(substitutionService);


        AdvertisementImportServiceImpl advertisementBean = new AdvertisementImportServiceImpl(addressService, premiseService, partyService, em, propertyTypeMatcher, askingPriceMatcher);

        // load all of the advertisements
        Collection<PropertyAdvertisement> advertisements = new AdvertisementDAO(em).findAll();

        for (PropertyAdvertisement advertisement : advertisements) {

            //advertisement.print();

            Premise premise = null;

            // associate this advertisement with a premise
            Address address = advertisement.getAddress();
            Address formattedAddress;
            if (address instanceof PlainTextAddress) {
                // format  the address into a formatted address
                formattedAddress = matcher.extract((PlainTextAddress) address);
            } else {
                formattedAddress = address;
            }

            if (formattedAddress != null) {
                // get the premise...
                premise = premiseService.lookupOrCreatePremise(formattedAddress, advertisement.getAttributes().getType(), null, null);
                // add associate the advertisement with it
                premise = advertisementBean.associateAdvertisement(premise, advertisement);

                premise.print(System.out);

                advertisementCount++;
                if (advertisementCount > count) {
                    break;
                }

            } else {

            }

        }

        em.flush();

        TestTools.printAll(em, Premise.class);

    }

    // -----------------------------------------------------------------------------------------------------

    public void testAdvertisementService() throws Exception {
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);

        loadSubstitutions(MEASUREMENT_SUBSTITUTIONS);
        loadSubstitutions(PROPERTY_TYPE_SUBSTITUTIONS);
        loadSubstitutions(ASKING_PRICE_SUBSTITUTIONS);
        PremiseEAO premiseEAO = new PremiseEAO(em);
        PropertyAdvertisementBean bean = new PropertyAdvertisementBean();
        bean.setAddress1("196 Seventh Road");   //reference: REIWA 16671
        bean.setAddress2(null);
        bean.setBathrooms(0);
        bean.setBedrooms(0);
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

        AddressService addressService = new AddressServiceImpl(em);
        PropertyEAO propertyEAO = new PropertyEAO(em);
        PropertyService propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);
        PremiseService premiseService = new PremiseServiceImpl(addressService, propertyService, premiseEAO, new AdvertisementCampaignDAO(em));
        PartyService partyService = new PartyServiceImpl(em);

        SubstitutionDAO substitutionDAO = new SubstitutionDAO(em);
        SubstitutionService substitutionService = new SubstitutionServiceImpl(substitutionDAO);

        PropertyTypeMatcher propertyTypeMatcher = new PropertyTypeMatcher(substitutionService);
        AskingPriceMatcher askingPriceMatcher = new AskingPriceMatcher(substitutionService);

        AdvertisementImportServiceImpl advertisementBean = new AdvertisementImportServiceImpl(addressService, premiseService, partyService, em, propertyTypeMatcher, askingPriceMatcher);

        PropertyAdvertisement propertyAdvertisement = advertisementBean.importAdvertisement(bean);

        assertNotNull(propertyAdvertisement);

    }


}
