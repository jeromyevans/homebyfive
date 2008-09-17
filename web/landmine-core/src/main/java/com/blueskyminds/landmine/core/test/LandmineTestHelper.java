package com.blueskyminds.landmine.core.test;

import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementCampaignDAO;
import com.blueskyminds.landmine.core.property.dao.PremiseEAO;
import com.blueskyminds.landmine.core.property.service.PremiseService;
import com.blueskyminds.landmine.core.property.service.PremiseServiceImpl;
import com.blueskyminds.landmine.core.property.patterns.PropertyTypeMatcher;
import com.blueskyminds.landmine.core.property.patterns.AskingPriceMatcher;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.party.service.PartyService;
import com.blueskyminds.enterprise.party.service.PartyServiceImpl;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.address.patterns.AddressPatternMatcher;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import com.blueskyminds.housepad.core.region.eao.CountryEAO;
import com.blueskyminds.housepad.core.region.eao.PostCodeEAO;
import com.blueskyminds.housepad.core.region.eao.StateEAO;
import com.blueskyminds.housepad.core.region.service.RegionService;
import com.blueskyminds.housepad.core.region.service.RegionServiceImpl;
import com.blueskyminds.housepad.core.property.eao.PropertyEAO;
import com.blueskyminds.housepad.core.property.service.PropertyService;
import com.blueskyminds.housepad.core.property.service.PropertyServiceImpl;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionServiceImpl;
import com.blueskyminds.framework.tools.substitutions.SubstitutionsFileReader;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper to setup a test environment
 *
 * Date Started: 21/05/2008
 */
public class LandmineTestHelper {

    private static final Log LOG = LogFactory.getLog(LandmineTestHelper.class);

    private static final String MEASUREMENT_SUBSTITUTIONS = "measurementSubstitutions.csv";
    private static final String PROPERTY_TYPE_SUBSTITUTIONS = "/propertyTypeSubstitutions.csv";
    private static final String ASKING_PRICE_SUBSTITUTIONS = "/askingPriceTypesSubstitutions.csv";
    private static final String PERIOD_TYPE_SUBSTITUTIONS = "/periodTypesSubstitutions.csv";

    private EntityManager em;

    // -- regions
    private CountryEAO countryEAO;
    private StateEAO stateEAO;
    private PostCodeEAO postCodeEAO;
    private SuburbEAO suburbEAO;
    private AddressDAO addressDAO;
    private AddressPatternMatcher matcher;
    private AddressService addressService;
    private RegionService regionService;
    private boolean regionsInitialised;

    // --- substitutions
    private SubstitutionDAO substitutionDAO;
    private SubstitutionService substitutionService;
    private boolean substitutionsInitialised;

    // --- parties
    private PartyService partyService;
    private boolean partiesInitialised;

    // --- properties
    private PremiseEAO premiseEAO;
    private PropertyEAO propertyEAO;
    private PropertyService propertyService;
    private PremiseService premiseService;
    private boolean propertiesInitialised;

    protected LandmineTestHelper(EntityManager em) {
        this.em = em;
    }

    public static LandmineTestHelper setupTestEnvironment(EntityManager em) {
        return new LandmineTestHelper(em);
    }

    /**
     * Setups regions.
     * Also sets up substitutions if not already done so
     * @return
     */
    public LandmineTestHelper withRegions() {
        AddressTestTools.initialiseCountryList();

        em.flush();

        if (!substitutionsInitialised) {
            withSubstitutions();
        }
        
        countryEAO = new CountryEAO(em);
        stateEAO = new StateEAO(em);
        postCodeEAO = new PostCodeEAO(em);
        suburbEAO = new SuburbEAO(em);
        addressDAO = new AddressDAO(em);

        // initialise the address matcher
        try {
            matcher = new AddressPatternMatcher(AddressService.AUS, em);
        } catch (PatternMatcherInitialisationException e) {
            LOG.error(e);
        }

        addressService = new AddressServiceImpl(em);        
        regionService = new RegionServiceImpl(em, countryEAO, stateEAO, suburbEAO, postCodeEAO, addressService);

        regionsInitialised = true;
        return this;
    }

    public LandmineTestHelper withSampleAddresses() {
        AddressTestTools.initialiseSampleAusAddresses();
        return this;
    }

    public LandmineTestHelper withSamplePremises() {
        PremiseTestTools.initialiseSampleAusPremises();
        return this;
    }

    public LandmineTestHelper withSubstitutions() {
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);

        loadSubstitutions(MEASUREMENT_SUBSTITUTIONS);
        loadSubstitutions(PROPERTY_TYPE_SUBSTITUTIONS);
        loadSubstitutions(ASKING_PRICE_SUBSTITUTIONS);
        loadSubstitutions(PERIOD_TYPE_SUBSTITUTIONS);

        em.flush();

        substitutionDAO = new SubstitutionDAO(em);
        substitutionService = new SubstitutionServiceImpl(substitutionDAO);
        substitutionsInitialised = true;
        return this;
    }

    public LandmineTestHelper withParties() {
        partyService = new PartyServiceImpl(em);
        return this;
    }

    /**
     * Set up properties.
     * Also sets up regions if not already done so.
     * @return
     */
    public LandmineTestHelper withProperties() {
        if (!regionsInitialised) {
            withRegions();
        }
        premiseEAO = new PremiseEAO(em);

        propertyEAO = new PropertyEAO(em);
        propertyService = new PropertyServiceImpl(propertyEAO, suburbEAO, em);
        premiseService = new PremiseServiceImpl(addressService, propertyService, premiseEAO, new AdvertisementCampaignDAO(em));

        try {
            PropertyTypeMatcher propertyTypeMatcher = new PropertyTypeMatcher(substitutionService);
            AskingPriceMatcher askingPriceMatcher = new AskingPriceMatcher(substitutionService);
        } catch (PatternMatcherInitialisationException e) {
            LOG.error(e);
        }

        return this;
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

    public CountryEAO getCountryEAO() {
        return countryEAO;
    }

    public StateEAO getStateEAO() {
        return stateEAO;
    }

    public PostCodeEAO getPostCodeEAO() {
        return postCodeEAO;
    }

    public SuburbEAO getSuburbEAO() {
        return suburbEAO;
    }

    public AddressDAO getAddressDAO() {
        return addressDAO;
    }

    public AddressPatternMatcher getMatcher() {
        return matcher;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public SubstitutionDAO getSubstitutionDAO() {
        return substitutionDAO;
    }

    public SubstitutionService getSubstitutionService() {
        return substitutionService;
    }

    public PartyService getPartyService() {
        return partyService;
    }

    public PremiseEAO getPremiseEAO() {
        return premiseEAO;
    }

    public PropertyEAO getPropertyEAO() {
        return propertyEAO;
    }

    public PropertyService getPropertyService() {
        return propertyService;
    }

    public PremiseService getPremiseService() {
        return premiseService;
    }

    public RegionService getRegionService() {
        return regionService;
    }
}
