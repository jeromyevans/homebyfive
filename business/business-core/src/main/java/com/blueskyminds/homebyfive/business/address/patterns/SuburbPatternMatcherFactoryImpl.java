package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.service.SuburbService;
import com.blueskyminds.homebyfive.business.region.service.StateService;
import com.blueskyminds.homebyfive.business.region.service.CountryService;
import com.blueskyminds.homebyfive.business.region.service.PostalCodeService;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.persistence.EntityManager;

/**
 * Date Started: 31/05/2008
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class SuburbPatternMatcherFactoryImpl implements SuburbPatternMatcherFactory {

    private static final Log LOG = LogFactory.getLog(SuburbPatternMatcherFactoryImpl.class);

    private SuburbScoringStrategy suburbScoringStrategy;
    private EntityManager em;
    private AddressDAO addressDAO;
    private CountryService countryService;
    private StateService stateService;
    private PostalCodeService postalCodeService;
    private SuburbService suburbService;

    public SuburbPatternMatcherFactoryImpl(EntityManager em, AddressDAO addressDAO, CountryService countryService, StateService stateService, PostalCodeService postalCodeService, SuburbService suburbService) {
        suburbScoringStrategy = new SuburbScoringStrategy();
        this.em = em;
        this.addressDAO = addressDAO;
        this.countryService = countryService;
        this.stateService = stateService;
        this.postalCodeService = postalCodeService;
        this.suburbService = suburbService;
    }

    /**
     * Create a broad matcher for the specified country
     *
     * @param countryAbbr
     * @return
     * @throws com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException
     *
     */
    public SuburbPatternMatcher create(String countryAbbr) throws PatternMatcherInitialisationException {
        LOG.info("Creating a new SuburbPatternMatcher for " + countryAbbr);
        SuburbPatternMatcher matcher = new SuburbPatternMatcher(countryAbbr, suburbScoringStrategy, countryService, stateService, suburbService, postalCodeService);

        matcher.setEntityManager(em);
        matcher.setupBins();
        return matcher;
    }

    /**
     * Create a fast matcher operating in the specified suburb only
     *
     * @param stateHandle
     * @return
     * @throws com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException
     *
     */
    public SuburbPatternMatcher create(State stateHandle) throws PatternMatcherInitialisationException {
        LOG.info("Creating a new SuburbPatternMatcher for " + stateHandle);
        SuburbPatternMatcher matcher = new SuburbPatternMatcher(stateHandle, suburbScoringStrategy, countryService, stateService, suburbService, postalCodeService);

        matcher.setEntityManager(em);
        matcher.setupBins();
        return matcher;
    }
}