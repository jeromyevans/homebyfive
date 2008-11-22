package com.blueskyminds.homebyfive.business.test;

import com.blueskyminds.homebyfive.business.region.dao.CountryEAO;
import com.blueskyminds.homebyfive.business.region.dao.StateEAO;
import com.blueskyminds.homebyfive.business.region.dao.PostCodeEAO;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.region.service.*;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.address.patterns.AddressPatternMatcher;
import com.blueskyminds.homebyfive.business.address.service.AddressService;
import com.blueskyminds.homebyfive.business.address.service.AddressServiceImpl;
import com.blueskyminds.homebyfive.business.tag.dao.TagDAO;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.tag.service.TagServiceImpl;

import javax.persistence.EntityManager;

/**
 * Sets up a test environment for region related tests
 *
 * Date Started: 16/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RegionTestEnvironment {

    public CountryEAO countryEAO;
    public StateEAO stateEAO;
    public PostCodeEAO postCodeEAO;
    public SuburbEAO suburbEAO;
    public AddressDAO addressDAO;
    public AddressPatternMatcher matcher;
    public AddressService addressService;
    public RegionService regionService;
    public TagDAO tagDAO;
    public TagService tagService;
    public CountryService countryService;
    public StateService stateService;
    public SuburbService suburbService;
    public PostalCodeService postalCodeService;

    public RegionTestEnvironment setup(EntityManager em) {
        countryEAO = new CountryEAO(em);
        stateEAO = new StateEAO(em);
        postCodeEAO = new PostCodeEAO(em);
        suburbEAO = new SuburbEAO(em);
        addressDAO = new AddressDAO(em);

        tagDAO = new TagDAO(em);
        tagService = new TagServiceImpl(tagDAO);

        countryService = new CountryServiceImpl(em, tagService, countryEAO);
        stateService = new StateServiceImpl(em, tagService, countryService, stateEAO);
        suburbService = new SuburbServiceImpl(em, tagService, stateService, suburbEAO);
        postalCodeService = new PostalCodeServiceImpl(em, tagService, stateService, postCodeEAO);

        regionService = new RegionServiceImpl();

        addressService = new AddressServiceImpl(em);
        regionService = new RegionServiceImpl(em, countryService, stateService, postalCodeService, suburbService);
        return this;
    }
}
