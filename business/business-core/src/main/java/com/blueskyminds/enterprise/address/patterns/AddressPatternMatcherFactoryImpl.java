package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.framework.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
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
public class AddressPatternMatcherFactoryImpl implements AddressParserFactory {

    private static final Log LOG = LogFactory.getLog(AddressPatternMatcherFactoryImpl.class);

    private AddressScoringStrategy addressScoringStrategy;
    private EntityManager em;
    private AddressDAO addressDAO;
    private SubstitutionService substitutionService;

    public AddressPatternMatcherFactoryImpl(EntityManager em, AddressDAO addressDAO, SubstitutionService substitutionService) {
        addressScoringStrategy = new AddressScoringStrategy();
        this.em = em;
        this.addressDAO = addressDAO;
        this.substitutionService = substitutionService;
    }

    

    /**
     * Create a broad matcher for the specified country
     *
     * @param iso3Code
     * @return
     * @throws com.blueskyminds.framework.patterns.PatternMatcherInitialisationException
     *
     */
    public AddressParser create(String iso3Code) {
        LOG.info("Creating a new AddressPatternMatcher for " + iso3Code);
        try {
            AddressPatternMatcher matcher = new AddressPatternMatcher(iso3Code, addressScoringStrategy);

            matcher.setEntityManager(em);
            matcher.setAddressDAO(addressDAO);
            matcher.setSubstitutionService(substitutionService);
            matcher.setupBins();

            return matcher;
        } catch (PatternMatcherInitialisationException e) {
            LOG.error(e);
            return null;
        }        
    }

    /**
     * Create a fast matcher operating in the specified suburb only
     *
     * @param suburb
     * @return
     * @throws com.blueskyminds.framework.patterns.PatternMatcherInitialisationException
     *
     */
    public AddressParser create(SuburbHandle suburb) {
        LOG.info("Creating a new AddressPatternMatcher for " + suburb);
        try {
            AddressPatternMatcher matcher = new AddressPatternMatcher(suburb, addressScoringStrategy);

            matcher.setEntityManager(em);
            matcher.setAddressDAO(addressDAO);
            matcher.setSubstitutionService(substitutionService);
            matcher.setupBins();
            return matcher;
         } catch (PatternMatcherInitialisationException e) {
            LOG.error(e);
            return null;
        }
    }
}