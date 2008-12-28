package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionServiceImpl;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.tag.service.TagServiceImpl;
import com.blueskyminds.homebyfive.business.tag.dao.TagDAO;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.patterns.AddressPatternMatcher;
import com.blueskyminds.homebyfive.business.region.dao.*;
import com.blueskyminds.homebyfive.business.region.service.*;

/**
 * Setup a common environment for the address tests
 *
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class AddressTestCase extends JPATestCase {
    protected static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    protected CountryEAO countryEAO;
    protected StateEAO stateEAO;
    protected PostCodeEAO postCodeEAO;
    protected SuburbEAO suburbEAO;
    protected AddressDAO addressDAO;
    protected AddressPatternMatcher matcher;
    protected AddressService addressService;
    protected RegionService regionService;
    protected TagDAO tagDAO;
    protected TagService tagService;
    protected CountryService countryService;
    protected StateService stateService;
    protected SuburbService suburbService;
    protected PostalCodeService postalCodeService;
    protected SubstitutionDAO substitutionDAO;
    protected SubstitutionService substitutionService;
    protected StreetEAO streetEAO;
    protected StreetService streetService;

    public AddressTestCase() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        em.flush();

        countryEAO = new CountryEAO(em);
        stateEAO = new StateEAO(em);
        postCodeEAO = new PostCodeEAO(em);
        suburbEAO = new SuburbEAO(em);
        streetEAO = new StreetEAO(em);
        addressDAO = new AddressDAO(em);

        tagDAO = new TagDAO(em);
        tagService = new TagServiceImpl(tagDAO);

        countryService = new CountryServiceImpl(em, tagService, countryEAO);
        stateService = new StateServiceImpl(em, tagService, countryService, stateEAO);
        suburbService = new SuburbServiceImpl(em, tagService, stateService, suburbEAO);
        postalCodeService = new PostalCodeServiceImpl(em, tagService, stateService, postCodeEAO);
        streetService = new StreetServiceImpl(em, tagService, suburbService, streetEAO);
        regionService = new RegionServiceImpl();

        addressService = new AddressServiceImpl(em);
        ((AddressServiceImpl) addressService).setStateService(stateService);
        ((AddressServiceImpl) addressService).setSuburbService(suburbService);
        ((AddressServiceImpl) addressService).setCountryService(countryService);
        regionService = new RegionServiceImpl(em, countryService, stateService, postalCodeService, suburbService);

        substitutionDAO = new SubstitutionDAO(em);
        substitutionService = new SubstitutionServiceImpl(substitutionDAO);
    }
}