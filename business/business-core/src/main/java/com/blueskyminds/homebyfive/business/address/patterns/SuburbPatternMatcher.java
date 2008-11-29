package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.*;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.ScoringStrategy;
import com.blueskyminds.homebyfive.business.address.*;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Street;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.service.*;

import javax.persistence.EntityManager;
import java.util.*;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Extends a PatternMatcher for matching a suburb string to a SuburbHandle.  The PatternMatcher handles noise in the
 *  string better than the fuzzy matching algorithm alone as it analyses each phrase in the input string
 *
 * Date Started: 31/05/2008
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class SuburbPatternMatcher extends PatternMatcher<Suburb> {

    private static final Log LOG = LogFactory.getLog(SuburbPatternMatcher.class);

    private String countryAbbr;

    private EntityManager em;

    private CountryService countryService;
    private StateService stateService;
    private PostalCodeService postalCodeService;
    private SuburbService suburbService;
    
    private Country country;
    private State state;

    /**
     * Create an SuburbPatternMatcher for the specified country with default settings.  Sets up all the bins for
     * matching
     *
     * The SuburbPatternMatcher is used to match a suburb sting to a SuburbHandle
     *
     * @param countryAbbr   3 digit ISO code for the country
     * @param em                the entity manager
     * @param countrySrevice
     * @param stateService
     * @param postalCodeService
     * @param suburbService
     */
     public SuburbPatternMatcher(String countryAbbr, EntityManager em, CountryService countrySrevice, StateService stateService, PostalCodeService postalCodeService, SuburbService suburbService) throws PatternMatcherInitialisationException {
        super(new SuburbScoringStrategy());

        if (countryAbbr == null) {
            throw new PatternMatcherInitialisationException("CountryAbbr cannot be null");
        }
        this.em = em;
        this.countryAbbr = countryAbbr;
        this.countryService = countrySrevice;
        this.stateService = stateService;
        this.postalCodeService = postalCodeService;
        this.suburbService = suburbService;
        init();
    }

    /**
     * Create an SuburbPatternMatcher for a state
     *
     * @param state
     * @param em
     * @param countryService
     *@param stateService
     * @param suburbService
     * @param postalCodeService @throws com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException
     */
    public SuburbPatternMatcher(State state, EntityManager em, CountryService countryService, StateService stateService, SuburbService suburbService, PostalCodeService postalCodeService) throws PatternMatcherInitialisationException {
        super(new SuburbScoringStrategy());
        this.state = state;
        this.em = em;
        this.countryService = countryService;
        this.stateService = stateService;
        this.postalCodeService = postalCodeService;
        this.suburbService = suburbService;
        init();
    }

    /**
     * Create an SuburbPatternMatcher for a suburb
     *
     * NOTE: the entityManager needs to be injected for initialisation
     *
     * @param state
     * @param scoringStrategy
     * @param countryService
     *@param stateService
     * @param suburbService
     * @param postalCodeService @throws com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException
     */
    public SuburbPatternMatcher(State state, ScoringStrategy scoringStrategy, CountryService countryService, StateService stateService, SuburbService suburbService, PostalCodeService postalCodeService) throws PatternMatcherInitialisationException {
        super(scoringStrategy);
        this.state = state;
        this.countryService = countryService;
        this.stateService = stateService;
        this.postalCodeService = postalCodeService;
        this.suburbService = suburbService;
    }

    /**
     * Create an SuburbPatternMatcher
     *
     * NOTE: the entityManager needs to be injected for initialisation
     *
     * @param countryAbbr
     * @param scoringStrategy
     * @param countryService
     *@param stateService
     * @param suburbService
     * @param postalCodeService @throws com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException
     */
    public SuburbPatternMatcher(String countryAbbr, ScoringStrategy scoringStrategy, CountryService countryService, StateService stateService, SuburbService suburbService, PostalCodeService postalCodeService) throws PatternMatcherInitialisationException {
        super(scoringStrategy);
        this.countryAbbr = countryAbbr;
        if (this.countryAbbr == null) {
            throw new PatternMatcherInitialisationException("ISO Country Code cannot be null");
        }
        this.countryService = countryService;
        this.stateService = stateService;
        this.postalCodeService = postalCodeService;
        this.suburbService = suburbService;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the SuburbPatternMatcher with default attributes - creates all of the standard pattern
     * matching bins for processing a suburb
     *
     */
    private void init() throws PatternMatcherInitialisationException {
        setupBins();
    }

    /** Initialises the bins */
    public void setupBins() throws PatternMatcherInitialisationException {
        reset();
        if (country == null) {
            if (state == null) {
                country = countryService.lookupCountry(countryAbbr);
            } else {
                // use a state
                state = em.merge(state);
            }
        } else {
            // merge the persistence instance into the session
            country = em.merge(country);
        }

        if ((country == null) && (state == null)) {
            LOG.error("Country/State have not been initialized. Could not lookup country "+ countryAbbr);
            throw new PatternMatcherInitialisationException("Countries have not been properly initialised.  Could not lookup country/state");
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        if ((country != null) && (state == null)) {
            // when working with a country, enable the match tate, state and postcode bins
            CountryBin countryBin = new CountryBin(country);
            StateBin stateBin = new StateBin(country, stateService);
            PostCodeBin postCodeBin = new PostCodeBin(country, postalCodeService);
            SuburbNameBin suburbBin = new SuburbNameBin(country, suburbService);

            // broad matching street bins
            addBin(countryBin);
            addBin(stateBin);
            addBin(suburbBin);
        } else {
            // state-specific suburb matching bin
            SuburbNameBin suburbBin = new SuburbNameBin(state, suburbService);
            addBin(suburbBin);
        }

        stopWatch.stop();
        LOG.info("initTime: "+stopWatch.toString());
    }

    /**
     * This post-processor is used to apply a second pass over the scored candidates to ensure there's no conflicts
     *  introduced by the allocations.
     *
     * Conflicts detected:
     *    suburb must be in the state
     *    allocated suburb must contain the allocated street
     *
     */
    protected void postProcessCandidates(Collection<CandidateAllocation> candidateAllocations) throws PatternMatcherException {
        Set<CandidateAllocation> allocationsToRemove = new HashSet<CandidateAllocation>();
        boolean removed;

        for (CandidateAllocation allocation : candidateAllocations) {
            removed = false;
            // ensure there isn't a conflict between the suburb and the state
            PhraseToBinAllocation suburbAllocation = allocation.getAllocationForBin(SuburbNameBin.class);

            if (suburbAllocation != null) {
                Suburb suburb = (Suburb) suburbAllocation.getPattern().getMetadata();

                if ((suburb != null) && (suburb.isIdSet())) {
                    suburb = em.merge(suburb);


                    // ensure the state allocation is consistent with the state
                    PhraseToBinAllocation stateAllocation = allocation.getAllocationForBin(StateBin.class);
                    if (stateAllocation != null) {
                        State state = (State) stateAllocation.getPattern().getMetadata();
                        if ((state != null) && (state.isIdSet())) {
                            state = em.merge(state);
                        }

                        if (!suburb.getState().equals(state)) {
                            allocationsToRemove.add(allocation);
                            removed = true;
                        }
                    }

                    if (!removed) {
                        // ensure the street allocation is consistent with the state
                        PhraseToBinAllocation streetNameAllocation = allocation.getAllocationForBin(StreetNameBin.class);
                        if (streetNameAllocation != null) {
                            Object metadata = streetNameAllocation.getPattern().getMetadata();
                            if ((metadata != null) && (metadata instanceof Street)) {
                                Street street = (Street) metadata;
                                if (street.isIdSet()) {
                                    street = em.merge(street);
                                }
                                if (!suburb.contains(street)) {
                                    // this is a conflict - the street isn't known to exist in this state
                                    allocationsToRemove.add(allocation);
                                }
                            }
                        }
                    }
                }
            } else {
                allocationsToRemove.add(allocation);
            }
        }
        candidateAllocations.removeAll(allocationsToRemove);
    }

    /** Extract the suburb from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the suburb, or null if the allocation is not valid or there's no underlying suburb metadata
     */
    private Suburb extractSuburb(PhraseToBinAllocation allocation) {
        return extractMetadata(Suburb.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extract the state from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the state, or null if the allocation is not valid or there's no underlying state metadata
     */
    private State extractState(PhraseToBinAllocation allocation) {
        return extractMetadata(State.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extract the Country from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the country specified in the address, or the country specified by the constructor of
     * this pattern matcher if htere's no country defined in the allocation
     */
    private Country extractCountry(PhraseToBinAllocation allocation) {
        Country value = extractMetadata(Country.class, allocation);
        if (value == null) {
            // use the country specified by the constructor
            return country;
        } else {
            // the country was specified
            return value;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Perform some initial filtering on the input string */
    private String prefilterAddressText(String addressText) {
        String filteredText = addressText.toLowerCase().trim();
        // replace spaces near '/'
        filteredText = filteredText.replaceAll("\\s+/\\s+", "/");
        // replace spaces near '-'
        filteredText = filteredText.replaceAll("\\s+\\-\\s+", "-");

        // replace ',' in between numbers with '/' numbers
        filteredText = filteredText.replaceAll("(\\d)\\s*,\\s*(\\d)", "$1/$2");

        return filteredText;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Parses the input and extracts the based-candidate Address */
    public Suburb extractBest(String inputString) throws PatternMatcherException {
        return super.extractBest(inputString);
    }

     /** Parses the input and extracts the based maxMatches results */
    public List<Suburb> extractMatches(String inputString, int maxMatches) throws PatternMatcherException {
        return super.extractMatches(prefilterAddressText(inputString), maxMatches);
    }

    /** Creates an SuburbHandle from a candidate allocation */
    protected Suburb extractCandidate(CandidateAllocation candidate) throws PatternMatcherException {

        // extract all of the components
        Suburb suburb = extractSuburb(candidate.getAllocationForBin(SuburbNameBin.class));
        return suburb;
    }



    // ------------------------------------------------------------------------------------------------------

    /** Convert the input plaintext address into a structured SuburbHandle */
    public Suburb extract(PlainTextAddress address) throws PatternMatcherException {
        return extractBest(address.getAddress());
    }

    // ------------------------------------------------------------------------------------------------------

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    public void setPostalCodeService(PostalCodeService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    public void setSuburbService(SuburbService suburbService) {
        this.suburbService = suburbService;
    }

    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }
}