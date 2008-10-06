package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.business.region.graph.State;
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

    public SuburbPatternMatcherFactoryImpl(EntityManager em, AddressDAO addressDAO) {
        suburbScoringStrategy = new SuburbScoringStrategy();
        this.em = em;
        this.addressDAO = addressDAO;
    }

    /**
     * Create a broad matcher for the specified country
     *
     * @param iso3Code
     * @return
     * @throws com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException
     *
     */
    public SuburbPatternMatcher create(String iso3Code) throws PatternMatcherInitialisationException {
        LOG.info("Creating a new SuburbPatternMatcher for " + iso3Code);
        SuburbPatternMatcher matcher = new SuburbPatternMatcher(iso3Code, suburbScoringStrategy);

        matcher.setEntityManager(em);
        matcher.setAddressDAO(addressDAO);
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
        SuburbPatternMatcher matcher = new SuburbPatternMatcher(stateHandle, suburbScoringStrategy);

        matcher.setEntityManager(em);
        matcher.setAddressDAO(addressDAO);
        matcher.setupBins();
        return matcher;
    }
}