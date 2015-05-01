package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.tag.service.TagServiceImpl;
import com.blueskyminds.homebyfive.business.tag.dao.TagDAO;
import com.blueskyminds.homebyfive.business.tag.expression.TagExpressionFactory;
import com.blueskyminds.homebyfive.business.tag.factory.CachingTagFactory;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.patterns.AddressPatternMatcher;
import com.blueskyminds.homebyfive.business.region.dao.*;
import com.blueskyminds.homebyfive.business.region.service.*;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RegionTestCase extends JPATestCase {
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
    protected StreetService streetService;
    protected StreetEAO streetEAO;
    protected TagExpressionFactory tagExpressionFactory;

    protected Tag a;
    protected Tag b;
    protected Tag c;

    public RegionTestCase() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        em.flush();

        countryEAO = new CountryEAO(em);
        stateEAO = new StateEAO(em);
        postCodeEAO = new PostCodeEAO(em);
        suburbEAO = new SuburbEAO(em);
        addressDAO = new AddressDAO(em);
        streetEAO = new StreetEAO(em);

        tagDAO = new TagDAO(em);
        tagService = new TagServiceImpl(tagDAO);
        tagExpressionFactory = new TagExpressionFactory(new CachingTagFactory(tagService));
        
        countryService = new CountryServiceImpl(em, tagService, countryEAO);
        stateService = new StateServiceImpl(em, tagService, countryService, stateEAO);
        suburbService = new SuburbServiceImpl(em, tagService, stateService, suburbEAO);
        postalCodeService = new PostalCodeServiceImpl(em, tagService, stateService, postCodeEAO);
        streetService = new StreetServiceImpl(em, tagService, suburbService, streetEAO);
        regionService = new RegionServiceImpl();

        addressService = new AddressServiceImpl(em);
        regionService = new RegionServiceImpl(em, countryService, stateService, postalCodeService, suburbService, streetService);

        a = tagService.lookupOrCreateTag("a");
        b = tagService.lookupOrCreateTag("b");
        c = tagService.lookupOrCreateTag("c");

        em.flush();
    }
}
