package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionServiceImpl;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.framework.core.patterns.*;
import com.blueskyminds.homebyfive.framework.core.patterns.scoring.ScoringStrategy;
import com.blueskyminds.enterprise.address.*;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.region.graph.SuburbHandle;
import com.blueskyminds.enterprise.region.graph.*;

import javax.persistence.EntityManager;
import java.util.*;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Extends a PatternMatcher for decomposing street addresses into its components
 *
 * The AddressPatternMatcher is heavy-weight so it's desirable if it's only created once for each region
 *
 * todo: the current implementation performs a lot of processing each time an instance is created.
 * This needs to be factored out now that it's used in the stateless context of the AddressService
 *
 * Date Started: 18/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class AddressPatternMatcher extends PatternMatcher<Address> implements AddressParserFactory, AddressParser {

    private static final Log LOG = LogFactory.getLog(AddressPatternMatcher.class);
    
    private String iso3CountryCode;

    private EntityManager em;

    private AddressDAO addressDAO;
    private SubstitutionService substitutionService;
    private CountryHandle country;
    private SuburbHandle suburb;

    /**
     * Create an AddressPatternMatcher for the specified country with default settings.  Sets up all the bins for
     * matching
     *
     * The AddressPatternMatcher is used to decompose an address into its components
     *
     * @param iso3CountryCode   3 digit ISO code for the country
     * @param em                the entity manager
     * @throws PatternMatcherInitialisationException
     */
     public AddressPatternMatcher(String iso3CountryCode, EntityManager em) throws PatternMatcherInitialisationException {
        super(new AddressScoringStrategy());

        if (iso3CountryCode == null) {
            throw new PatternMatcherInitialisationException("ISO Country Code cannot be null");
        }
        this.em = em;
        this.iso3CountryCode = iso3CountryCode;
        init();
    }

    /**
     * Create an AddressPatternMatcher for a suburb
     *
     * @param suburbHandle
     * @param em
     * @throws PatternMatcherInitialisationException
     */
    public AddressPatternMatcher(SuburbHandle suburbHandle, EntityManager em) throws PatternMatcherInitialisationException {
        super(new AddressScoringStrategy());
        this.suburb = suburbHandle;
        this.em = em;
        init();
    }

    /**
     * Create an AddressPatternMatcher for a suburb
     *
     * NOTE: the entityManager needs to be injected for initialisation
     *
     * @param suburbHandle
     * @param scoringStrategy
     * @throws PatternMatcherInitialisationException
     */
    public AddressPatternMatcher(SuburbHandle suburbHandle, ScoringStrategy scoringStrategy) throws PatternMatcherInitialisationException {
        super(scoringStrategy);
        this.suburb = suburbHandle;
    }

    /**
     * Create an AddressPatternMatcher
     *
     * NOTE: the entityManager needs to be injected for initialisation
     *
     * @param iso3DigitCode
     * @param scoringStrategy
     * @throws PatternMatcherInitialisationException
     */
    public AddressPatternMatcher(String iso3DigitCode, ScoringStrategy scoringStrategy) throws PatternMatcherInitialisationException {
        super(scoringStrategy);
        this.iso3CountryCode = iso3DigitCode;
        if (iso3CountryCode == null) {
            throw new PatternMatcherInitialisationException("ISO Country Code cannot be null");
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AddressPatternMatcher with default attributes - creates all of the standard pattern
     * matching bins for processing a street address
     *
     */
    private void init() throws PatternMatcherInitialisationException {
        addressDAO = new AddressDAO(em);
        substitutionService = new SubstitutionServiceImpl(new SubstitutionDAO(em));
        setupBins();
    }

    /** Initialises the bins */
    public void setupBins() throws PatternMatcherInitialisationException {
        reset();
        if (country == null) {
            if (suburb == null) {
                if (addressDAO != null) {
                    country = addressDAO.lookupCountry(iso3CountryCode);
                } else {
                    LOG.error("AddressDAO has not been injected");
                    throw new PatternMatcherInitialisationException("AddressDAO has not been injected");
                }
            } else {
                // use a suburb
                suburb = em.merge(suburb);
            }
        } else {
            // merge the persistence instance into the session
            country = em.merge(country);
        }

        if ((country == null) && (suburb == null)) {
            LOG.error("Countries/Suburbs have not been initialized.  Country: "+iso3CountryCode+" suburb: "+suburb);
            throw new PatternMatcherInitialisationException("Countries have not been properly initialised.  Could not lookup country/suburb");
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        if (country != null) {
            // when working with a country, enable the match tate, suburb and postcode bins
            CountryBin countryBin = new CountryBin(country);
            stopWatch.split();
            LOG.info("   split1: "+stopWatch.toString());
            stopWatch.unsplit();
            StateBin stateBin = new StateBin(country, addressDAO);
            stopWatch.split();
            LOG.info("   split2: "+stopWatch.toString());
            stopWatch.unsplit();
            PostCodeBin postCodeBin = new PostCodeBin(country, addressDAO);
            stopWatch.split();
            LOG.info("   split3: "+stopWatch.toString());
            stopWatch.unsplit();
            SuburbNameBin suburbBin = new SuburbNameBin(country, addressDAO);
            stopWatch.split();
            LOG.info("   split4: "+stopWatch.toString());
            stopWatch.unsplit();

            // broad matching streetname bin
            StreetNameBin streetNameBin = new StreetNameBin(country, addressDAO, substitutionService);
            stopWatch.split();
            LOG.info("   split5: "+stopWatch.toString());
            stopWatch.unsplit();
            addBin(countryBin);
            addBin(stateBin);
            addBin(postCodeBin);
            addBin(suburbBin);
            addBin(streetNameBin);
        } else {
            // suburb-specific street matching bin
            StreetNameBin streetNameBin = new StreetNameBin(suburb, addressDAO, substitutionService);
            addBin(streetNameBin);
            stopWatch.split();
            LOG.info("   split6: "+stopWatch.toString());
            stopWatch.unsplit();
        }

        StreetTypeBin streetTypeBin = new StreetTypeBin(substitutionService);
        StreetSectionBin streetSectionBin = new StreetSectionBin(substitutionService);
        UnitNumberBin unitNumberBin = new UnitNumberBin(substitutionService);
        StreetNumberBin streetNumberBin = new StreetNumberBin(substitutionService);
        LotNumberBin lotNumberBin = new LotNumberBin(substitutionService);

        stopWatch.stop();
        LOG.info("initTime: "+stopWatch.toString());

        addBin(streetTypeBin);
        addBin(streetSectionBin);
        addBin(unitNumberBin);
        addBin(streetNumberBin);
        addBin(lotNumberBin);
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
            // ensure there isn't a conflict between the suburb  and the state
            PhraseToBinAllocation suburbAllocation = allocation.getAllocationForBin(SuburbNameBin.class);

            if (suburbAllocation != null) {
                SuburbHandle suburb = (SuburbHandle) suburbAllocation.getPattern().getMetadata();

                if ((suburb != null) && (suburb.isIdSet())) {
                    suburb = em.merge(suburb);


                    // ensure the suburb allocation is consistent with the state
                    PhraseToBinAllocation stateAllocation = allocation.getAllocationForBin(StateBin.class);
                    if (stateAllocation != null) {
                        StateHandle state = (StateHandle) stateAllocation.getPattern().getMetadata();
                        if ((state != null) && (state.isIdSet())) {
                            state = em.merge(state);
                        }

                        if (!suburb.getState().equals(state)) {
                            allocationsToRemove.add(allocation);
                            removed = true;
                        }
                    }

                    if (!removed) {
                        // ensure the street allocation is consistent with the suburb
                        PhraseToBinAllocation streetNameAllocation = allocation.getAllocationForBin(StreetNameBin.class);
                        if (streetNameAllocation != null) {
                            Object metadata = streetNameAllocation.getPattern().getMetadata();
                            if ((metadata != null) && (metadata instanceof StreetHandle)) {
                                StreetHandle street = (StreetHandle) metadata;
                                if (street.isIdSet()) {
                                    street = em.merge(street);
                                }
                                if (!suburb.contains(street)) {
                                    // this is a conflict - the street isn't known to exist in this suburb
                                    allocationsToRemove.add(allocation);
                                }
                            }
                        }
                    }
                }
            } else {
                // a suburb allocation is mandatory
                if (suburb == null) {
                    allocationsToRemove.add(allocation);
                }
            }
        }
        candidateAllocations.removeAll(allocationsToRemove);
    }
 
    /** Extract the suburb from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the suburb, or null if the allocation is not valid or there's no underlying suburb metadata
     */
    private SuburbHandle extractSuburb(PhraseToBinAllocation allocation) {
        return extractMetadata(SuburbHandle.class, allocation);
    }

    /**
     * Extract the street from the metadata underlying the given allocation if a known street was matched
     *
     * @param allocation
     * @return the street, or null if a known street was not matched
     */
    private StreetHandle extractStreet(PhraseToBinAllocation allocation) {
        return extractMetadata(StreetHandle.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extract the StreetType from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the StreetType, or null if the allocation is not valid or there's no underlying streettype metadata
     */
    private StreetType extractStreetType(PhraseToBinAllocation allocation) {
        return extractMetadata(StreetType.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extract the StreetSection from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the StreetSection, or null if the allocation is not valid or there's no underlying streetsection metadata
     */
    private StreetSection extractStreetSection(PhraseToBinAllocation allocation) {
        return extractMetadata(StreetSection.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extract the state from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the state, or null if the allocation is not valid or there's no underlying state metadata
     */
    private StateHandle extractState(PhraseToBinAllocation allocation) {
        return extractMetadata(StateHandle.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extract the PostCode from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the postcode, or null if the allocation is not valid or there's no underlying postcode metadata
     */
    private PostCodeHandle extractPostCode(PhraseToBinAllocation allocation) {
        return extractMetadata(PostCodeHandle.class, allocation);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extract the Country from the metadata underlying the given allocation.
     *
     * @param allocation
     * @return the country specified in the address, or the country specified by the constructor of
     * this pattern matcher if htere's no country defined in the allocation
     */
    private CountryHandle extractCountry(PhraseToBinAllocation allocation) {
        CountryHandle value = extractMetadata(CountryHandle.class, allocation);
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
    public Address extractBest(String inputString) throws PatternMatcherException {
        return super.extractBest(inputString);
    }
 
     /** Parses the input and extracts the based maxMatches results */
    public List<Address> extractMatches(String inputString, int maxMatches) throws PatternMatcherException {
        return super.extractMatches(prefilterAddressText(inputString), maxMatches);
    }

    /** Creates an Address from a candidate allocation */
    protected Address extractCandidate(CandidateAllocation candidate) throws PatternMatcherException {
        StreetAddress streetAddress = null;
        String streetName = null;

        // extract all of the components
        String unitNumber = extractValue(candidate.getAllocationForBin(UnitNumberBin.class));
        String streetNumber = extractValue(candidate.getAllocationForBin(StreetNumberBin.class));
        String lotNumber = extractValue(candidate.getAllocationForBin(LotNumberBin.class));
        StreetType streetType = extractStreetType(candidate.getAllocationForBin(StreetTypeBin.class));
        StreetHandle street = extractStreet(candidate.getAllocationForBin(StreetNameBin.class));
        if (street == null) {
            streetName = extractValue(candidate.getAllocationForBin(StreetNameBin.class));
        }
        StreetSection streetSection = extractStreetSection(candidate.getAllocationForBin(StreetSectionBin.class));

        SuburbHandle suburb = extractSuburb(candidate.getAllocationForBin(SuburbNameBin.class));
        StateHandle state = extractState(candidate.getAllocationForBin(StateBin.class));
        PostCodeHandle postCode = extractPostCode(candidate.getAllocationForBin(PostCodeBin.class));
        CountryHandle country = extractCountry(candidate.getAllocationForBin(CountryBin.class));
        
        if (streetSection == null) {
            streetSection = StreetSection.NA;
        }

        if (street == null) {
            if (streetName != null) {
                // lookup the street
                street = new AddressDAO(em).getStreetByName(streetName, streetType, streetSection, suburb);

                if (street == null) {
                    // the street doesn't exist - create a new instance
                    street = new StreetHandle(streetName, streetType, streetSection);
                }
            }
        }

        if (street != null) {
            if (unitNumber == null) {
                if (lotNumber == null) {
                    // create the street address
                    streetAddress = new StreetAddress(streetNumber, street, suburb, postCode);
                } else {
                    // create the lot address
                    streetAddress = new LotAddress(lotNumber, streetNumber, street, suburb, postCode);
                }
            } else {
                // create the unit address
                streetAddress = new UnitAddress(unitNumber, streetNumber, street, suburb, postCode);
            }
        } else {
            if ((suburb != null) || (postCode != null)) {
                // create a street address that only references the suburb/postcode
                streetAddress = new StreetAddress(null, suburb, postCode);
            }
        }

        return streetAddress;
    }



    // ------------------------------------------------------------------------------------------------------

    /** Convert the input plaintext address into a structured/formatted address */
    public Address extract(PlainTextAddress address) throws PatternMatcherException {
        return extractBest(address.getAddress());
    }

    /**
     * Parse a plain-text address into its components
     *
     * @param plainTextAddress
     * @return
     */
    public Address parseAddress(String plainTextAddress) throws PatternMatcherException {
        return extractBest(plainTextAddress);
    }

    public List<Address> parseAddress(String plainTextAddress, int maxCandidates) throws PatternMatcherException {
        return extractMatches(plainTextAddress, maxCandidates);
    }

    // ------------------------------------------------------------------------------------------------------

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void setSubstitutionService(SubstitutionService substitutionService) {
        this.substitutionService = substitutionService;
    }

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    /**
     * Just for backwards compatibility - pretty stupid really
     *
     * @param iso3DigitCode
     * @return
     */
    public AddressParser create(String iso3DigitCode) {
        return this;
    }

    public AddressParser create(SuburbHandle suburb) {
        return this;
    }
}
