package com.blueskyminds.homebyfive.business.address.service;

import com.blueskyminds.homebyfive.business.address.*;
import com.blueskyminds.homebyfive.business.address.dao.*;
import com.blueskyminds.homebyfive.business.address.patterns.*;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.RegionFactory;
import com.blueskyminds.homebyfive.business.region.service.RegionGraphService;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.framework.core.patterns.LevensteinDistanceTools;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherException;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * Implementation of an AddressService
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class AddressServiceImpl implements AddressService {

    private static final Log LOG = LogFactory.getLog(AddressServiceImpl.class);

    /** Maximum number of addresses to match when parsing a plain text string */
    private static final int MAX_MATCHES = 10;

    private AddressParserFactory addressParserFactory;
    private SuburbPatternMatcherFactory suburbPatternMatcherFactory;
    private EntityManager em;
    private RegionGraphService regionGraphService;

        /** Allow AddressPatternMatcher's created in this instance to be reused */
    private Map<String, AddressParser> matcherCache;

    /** Allow AddressParser's created in this instance to be reused */
    private Map<Suburb, AddressParser> matcherCacheBySuburb;

    private Map<State, SuburbPatternMatcher> suburbMatcherCache;

    /** Cache of recently used suburb names */
    private Map<String, Suburb> suburbNameCache;

    /** Cache of recently used state names */
    private Map<String, List<State>> stateNameCache;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the address service
     *
     **/
    public AddressServiceImpl(EntityManager em) {    
        this.em = em;
        init();
    }

    public AddressServiceImpl(AddressParserFactory addressParserFactory, EntityManager em, RegionGraphService regionGraphService) {
        this.addressParserFactory = addressParserFactory;
        this.em = em;
        this.regionGraphService = regionGraphService;
        init();
    }

    public AddressServiceImpl() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AddressServiceImpl with default attributes
     */
    private void init() {
        matcherCache = new HashMap<String, AddressParser>();
        matcherCacheBySuburb = new HashMap<Suburb, AddressParser>();
        suburbMatcherCache = new HashMap<State, SuburbPatternMatcher>();
        suburbNameCache = new HashMap<String, Suburb>();
        stateNameCache = new HashMap<String, List<State>>();
    }

    // ------------------------------------------------------------------------------------------------------   

    /**
     * Creates an Address from a plain-text address string
     *
     * The address returned is not persisted.
     *
     * @param addressString plain text address
     * @param countryAbbr
     * @return an Address derived from the address string.  In the worst case, this will simply be a
     *  PlainTextAddress
     * @throws AddressProcessingException if a critical error occurs trying to process the address.
     */
    public Address parseAddress(String addressString, String countryAbbr) throws AddressProcessingException {

        Address address;
        try {
            if (addressParserFactory == null) {
                addressParserFactory = new AddressPatternMatcher(countryAbbr, em);
            }

            // reuse matches created in this instance
            AddressParser matcher = selectMatcher(countryAbbr);
            address = matcher.parseAddress(addressString);
        } catch (PatternMatcherException e) {
            throw new AddressProcessingException(e);
        }

        return address;
    }

    /** Select an AddressParser from the cache or create a new one */
    private AddressParser selectMatcher(String iso3CountryCode) throws PatternMatcherInitialisationException {
        AddressParser matcher = matcherCache.get(iso3CountryCode);
        if (matcher == null) {
            matcher = addressParserFactory.create(iso3CountryCode);
            //matcher.setEntityManager(em);
            matcherCache.put(iso3CountryCode, matcher);
        } else {
            //matcher.reset();
        }
        return matcher;
    }


    /** Select an AddressParser from the cache or create a new one */
    private AddressParser selectMatcher(Suburb suburbHandle) throws PatternMatcherInitialisationException {
        AddressParser matcher = matcherCacheBySuburb.get(suburbHandle);
        if (matcher == null) {
            if (addressParserFactory == null) {
                addressParserFactory = new AddressPatternMatcher(suburbHandle, em);
            }

            matcher = addressParserFactory.create(suburbHandle);
            //matcher.setEntityManager(em);
            matcherCacheBySuburb.put(suburbHandle, matcher);
        } else {
            //matcher.reset();
        }
        return matcher;
    }

    /**
     * Creates an Address from a plain-text address string
     *
     * The address returned is not persisted.
     *
     * @param addressString plain text address
     * @param iso3CountryCode
     * @return an Address derived from the address string.  In the worst case, this will simply be a
     *  PlainTextAddress
     * @throws AddressProcessingException if a critical error occurs trying to process the address.
     */
    public Address parseAddress(String addressString, String suburbString, String stateString, String iso3CountryCode) throws AddressProcessingException {

        Address address = null;
        try {
            List<Suburb> suburbCandidates = null;

            if (StringUtils.isNotBlank(suburbString)) {

                Suburb suburbHandle = suburbNameCache.get(iso3CountryCode+":"+stateString+":"+suburbString);
                if (suburbHandle == null) {
                    LOG.info("suburbName "+iso3CountryCode+":"+stateString+":"+suburbString+" is not cached");
                    // lookup the suburb candidate(s)
                    if (StringUtils.isNotBlank(stateString)) {
                        List<State> stateCandidates = findState(stateString, iso3CountryCode);
                        if (stateCandidates.size() > 0) {
                            suburbCandidates = findSuburb(suburbString, stateCandidates);
                        }
                    } else {
                        // look for suburb in country (slower!)
                        suburbCandidates = findSuburb(suburbString, iso3CountryCode);
                    }

                    if ((suburbCandidates != null) && (suburbCandidates.size() > 0)) {
                        // suburb has been constrained to a set of candidates.  The matching algorithm can be limited
                            // to a simple street address match
                        suburbHandle = suburbCandidates.iterator().next();
                    }

                    if (suburbHandle != null) {
                        suburbNameCache.put(iso3CountryCode+":"+stateString+":"+suburbString, suburbHandle);
                    } else {
                        // special-case: record an invalid entry so we don't look again
                        suburbNameCache.put(iso3CountryCode+":"+stateString+":"+suburbString, Suburb.INVALID);
                    }
                }

                if ((suburbHandle != null) && (!suburbHandle.isInvalid())) {
                    if (StringUtils.isNotBlank(addressString)) {
                        AddressParser matcher = selectMatcher(suburbHandle);
                        if (matcher != null) {
                            address = matcher.parseAddress(addressString);

                            if (address != null) {
                                address.setSuburb(suburbHandle);
                            } else {
                                LOG.info("Failed to parse address "+addressString+" in suburb "+suburbHandle.getName());
                            }
                        } 
                    } else {
                        address = new StreetAddress(null, suburbHandle, null);
                    }
                }

                if (address == null) {
                    // the suburb/state information was not helpful - try using the information in a broader search
                    addressString = StringUtils.join(new String[]{addressString, suburbString, stateString}, " ");
                }
            }

            if (address == null) {                
                if (StringUtils.isNotBlank(addressString)) {
                    AddressParser matcher = selectMatcher(iso3CountryCode);
                    LOG.info("Performing wide search for "+addressString+" in country "+iso3CountryCode);
                    address = matcher.parseAddress(addressString);
                    if (address == null) {
                        LOG.info("Failed to parse address "+addressString+" in country "+iso3CountryCode);
                    }
                }
            }
        } catch (PatternMatcherException e) {
            throw new AddressProcessingException(e);
        }

        return address;
    }

   

    /**
     * Creates Addresses matching the plain-text address string
     *
     * The addresses returned are not persistent, they're just parsed.
     *
     * @param addressString plain text address
     * @param iso3CountryCode
     * @param maxMatches            maximum number of results to return
     *
     * @return Addresses derived from the address string.  In the worst case, this will simply be a
     *  PlainTextAddress
     * @throws AddressProcessingException if a critical error occurs trying to process the address.
     */
    public List<Address> parseAddressCandidates(String addressString, String iso3CountryCode, int maxMatches) throws AddressProcessingException {

        List<Address> addresses;
        try {
            LOG.info(addressString);

            AddressParser matcher = selectMatcher(iso3CountryCode);
            addresses = matcher.parseAddress(addressString, maxMatches);
            
        } catch (PatternMatcherException e) {
            throw new AddressProcessingException(e);
        }

        return addresses;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Adds a new Street to a Suburb.  The street and the suburb are persisted if necessary  */
    private Street persistNewStreet(Suburb suburb, Street street) throws PersistenceServiceException {
        if (street != null) {
            if (!street.isIdSet()) {
                if (suburb != null) {
                    suburb.addStreet(street);
                    em.persist(suburb);
                }
                em.persist(street);
            }
        }
        return street;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Persists an address.  If the address already exists the existing address is returned
     *
     * @param address to persist, or lookup if it already exists
     * @return the new or existing Address
     * @throws AddressProcessingException if a persistence problem occurs
     */
    public Address lookupOrCreateAddress(Address address) throws AddressProcessingException {
        Address addressFound;

        try {
            // we need to handle detached suburbs, status and postcodes here
            if ((address.getSuburb() != null) && (address.getSuburb().isIdSet())) {
                address.setSuburb(em.merge(address.getSuburb()));
            }
//            if ((address.getState() != null) && (address.getState().isIdSet())) {
//                em.merge(address.getState());
//            }
            if ((address.getPostCode() != null) && (address.getPostCode().isIdSet())) {
                address.setPostCode(em.merge(address.getPostCode()));
            }

            // find this address
            addressFound = new AddressDAO(em).lookupAddressByExample(address);

            if (addressFound == null) {

                if (address instanceof StreetAddress) {
                    // persist the new street  
                    StreetAddress streetAddress = (StreetAddress) address;
                    if (streetAddress.getStreet() != null) {
                        if (!streetAddress.getStreet().isIdSet()) {
                            Street street = streetAddress.getStreet();
                            // ensure the name is capitalized before persistence
                            street.setName(StringUtils.capitalize(street.getName()));

                            persistNewStreet(streetAddress.getSuburb(), streetAddress.getStreet());
                        } else {
                            streetAddress.setStreet(em.merge(address.getStreet()));
                        }
                    }
                }

                em.persist(address);
                // use the new address
                addressFound = address;
            }

        } catch (PersistenceServiceException e) {
            throw new AddressProcessingException(e);
        }
        return addressFound;
    }


    /**
     * Persists an address.  If the address already exists the existing address is returned
     *
     * The address is first parsed and then looked up/persisted
     *
     * @param addressString
     * @return the new or existing Address
     * @throws com.blueskyminds.homebyfive.business.address.service.AddressProcessingException
     *          if a persistence problem occurs
     */
    public Address lookupOrCreateAddress(String addressString, String countryAbbr) throws AddressProcessingException {
        Address address = parseAddress(addressString, countryAbbr);
        if (address != null) {
            return lookupOrCreateAddress(address);
        } else {
            throw new AddressProcessingException("Could not create or lookup and address.  Address is invalid/could not be parsed ('"+addressString+"','"+countryAbbr+"')");
        }
    }

    public Address lookupOrCreateAddress(String addressString, String suburbString, String stateString, String countryAbbr) throws AddressProcessingException {
        Address address = parseAddress(addressString, suburbString, stateString, countryAbbr);
        if (address != null) {
            return lookupOrCreateAddress(address);
        } else {
            throw new AddressProcessingException("Could not create or lookup and address.  Address is invalid/could not be parsed ('"+addressString+"','"+suburbString+"','"+stateString+"','"+countryAbbr+"')");
        }
    }

    /**
     * Find a suburb in the specified country with a name like the one provided.  The closest matches
     * are returned in order of precedence
     *
     * @param name
     * @param iso3CountryCode
     * @return
     */
    @Deprecated
    public List<Suburb> findSuburbLike(String name, String iso3CountryCode) {
        AddressDAO addressDAO = new AddressDAO(em);

        Country country = addressDAO.lookupCountry(iso3CountryCode);
        Set<Suburb> suburbs = new HashSet<Suburb>(addressDAO.listSuburbsInCountry(country));

        return LevensteinDistanceTools.matchName(name, suburbs);
    }

    /**
     * Generate a list of possible addresses matching the given addressString. The results include known addresses as
     *  well as unknown potential addresses
     *
     * @param addressString
     * @param iso3CountryCode
     * @return
     */
    public List<Address> findAddress(String addressString, String iso3CountryCode) {

        List<Address> addresses = null;
        List<Address> matchedAddresses = new LinkedList<Address>();
        AddressDAO addressDAO = new AddressDAO(em);
        Address knownAddress;
        try {
            addresses = parseAddressCandidates(addressString, iso3CountryCode, MAX_MATCHES);

            for (Address address : addresses) {

                try {
                    knownAddress = addressDAO.lookupAddressByExample(address);
                } catch(PersistenceServiceException e) {
                    knownAddress = null;
                }

                if (knownAddress != null) {
                    // address is known
                    matchedAddresses.add(knownAddress);
                } else {
                    // unknown but appears to be valid
                    matchedAddresses.add(address);
                }
            }
        } catch(AddressProcessingException e) {
            LOG.error("Error processing address", e);
        }
        return matchedAddresses;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    /**
     * Creates an association between the child and parent region and persists them
     *
     * @param child
     * @param parent
     * @return
     */
    private Region createChildRegion(Region child, Region parent) {
        if (parent != null) {
            parent.addChildRegion(child);
            em.persist(parent);
        }
        em.persist(child);
        return child;
    }


    /** Create and persist a new country */
    public Country createCountry(String name, String iso2CountryCode, String iso3CountryCode, String currencyCode) {
        RegionFactory regionFactory = new RegionFactory();
        Region handle = regionFactory.createCountry(name, iso2CountryCode, iso3CountryCode, currencyCode);
        em.persist(handle);
        return (Country) handle;
    }

    /** Create and persist a new state */
    public State createState(String name, String abbreviation, Country parent) {
        RegionFactory stateFactory = new RegionFactory();
        State handle = stateFactory.createState(name, abbreviation, parent);
        em.persist(handle);
        em.persist(parent);
        return handle;
    }

    /** Create and persist a new PostCode */
    public PostalCode createPostCode(String name, State parent) {
        PostalCode handle = new RegionFactory().createPostCode(name, parent);
        em.persist(handle);
        em.persist(parent);
        return handle;
    }

    /** Create and persist a new Suburb
     *
     * A new SuburbHandle is created that references a new Suburb implementation
     * The SuburbHandle is added as a child of the StateHandle
     *
     *  */
    public Suburb createSuburb(String name, State parent) {
        Suburb handle = new RegionFactory().createSuburb(name, parent);
        return (Suburb) createChildRegion(handle, parent);
    }

    /**
     * Find the country with the specified name
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Country> findCountry(String name) {
        Set<Country> countries = new AddressDAO(em).listCountries();
        return LevensteinDistanceTools.matchName(name, countries);
    }

    public Country lookupCountry(String iso2DigitCode) {
        AddressDAO addressDAO = new AddressDAO(em);
        return addressDAO.findCountryByISO2DigitCode(iso2DigitCode);
    }

    /**
     * Find a suburb in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Suburb> findSuburb(String name, String iso3DigitCountryCode) {
        AddressDAO addressDAO = new AddressDAO(em);

        Country country = addressDAO.lookupCountry(iso3DigitCountryCode);
        Set<Suburb> suburbs = addressDAO.listSuburbsInCountry(country);

        return LevensteinDistanceTools.matchName(name, suburbs);
    }

    /**
     * Find a suburb in the specified state(s)
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Suburb> findSuburb(String name, List<State> states) {

        List<Suburb> suburbs = new LinkedList<Suburb>();

        AddressDAO addressDAO = new AddressDAO(em);

        List<State> statesNotMatched = new LinkedList<State>();

        // find using the cache first
        for (State state : states) {
            Suburb suburbHandle = suburbNameCache.get(state.getName()+":"+name);
            if (suburbHandle != null) {
                if (!suburbHandle.isInvalid()) {  // when it's not null but invalid we should discard it as we know not to search
                    suburbs.add(suburbHandle);
                }
            } else {
                statesNotMatched.add(state);
            }
        }

        if (statesNotMatched.size() > 0) {
            List<State> statesStillNotMatched = new LinkedList<State>();

            // find using exact match
            for (State state : statesNotMatched) {

                Suburb suburbHandle;

                if (LOG.isInfoEnabled()) {
                    LOG.info("Finding suburb '"+name+"' using exact pattern matching in "+state.getName());
                }
                suburbHandle = addressDAO.lookupSuburb(name, state);
                if (suburbHandle != null) {
                    suburbs.add(suburbHandle);
                } else {
                    statesStillNotMatched.add(state);
                }
            }

            // find using fuzzy matching
            if (statesStillNotMatched.size() > 0) {
                if (suburbPatternMatcherFactory != null) {
                    for (State state : statesStillNotMatched) {
                        try {
                            if (LOG.isInfoEnabled()) {
                                LOG.info("Finding suburb '"+name+"' using fuzzy pattern matching in "+state.getName());
                            }
                            SuburbPatternMatcher patternMatcher = selectSuburbMatcher(state);
                            Suburb suburb = patternMatcher.extractBest(name);
                            if (suburb != null) {
                                suburbs.add(suburb);
                            }
                        } catch (PatternMatcherException e) {
                            LOG.error("Could not initialize SuburbPatternMatcher", e);
                        }
                    }
                } else {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Finding suburb '"+name+"' using fuzzy word matching across all states");
                    }

                    Set<Suburb> suburbSet = new HashSet<Suburb>();
                    for (State state : statesNotMatched) {
                        suburbSet.addAll(addressDAO.listSuburbsInState(state));
                    }

                    suburbs.addAll(LevensteinDistanceTools.matchName(name, suburbSet));
                }
            }

            if (suburbs.size() > 0) {
                // put suburb into the cache
                for (State state : states) {
                    for (Suburb suburb : suburbs) {
                        if (suburb.getState().equals(state)) {
                            suburbNameCache.put(state.getName()+":"+name, suburb);
                        }
                    }
                }
            } else {
                // store invalid suburb entries in the cache so we don't look again
                for (State state : states) {
                    suburbNameCache.put(state.getName()+":"+name, Suburb.INVALID);
                }
            }
        }

        return suburbs;
    }

    private SuburbPatternMatcher selectSuburbMatcher(State state) throws PatternMatcherInitialisationException {
        SuburbPatternMatcher patternMatcher = suburbMatcherCache.get(state);
        if (patternMatcher == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("SuburbMatcher for state "+state.getName()+" is not cached");
            }
            patternMatcher = suburbPatternMatcherFactory.create(state);
            suburbMatcherCache.put(state, patternMatcher);
        } else {
            patternMatcher.reset();
        }
        return patternMatcher;
    }

    /**
     * Find a suburb in the specified state(s)
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Suburb> findSuburb(String name, State state) {
        LinkedList<State> states = new LinkedList<State>();
        states.add(state);
        return findSuburb(name, states);
    }

    /**
    * Find a state in the specified country.  Pulls the result from a cache if recently used
    *
    * <p/>
    * Performs a fuzzy match and returns the matches in order of rank
    */
    public List<State> findState(String name, String iso3DigitCountryCode) {
        List<State> states = stateNameCache.get(iso3DigitCountryCode + ":" + name);
        if (states == null) {
            AddressDAO addressDAO = new AddressDAO(em);

            Country country = addressDAO.lookupCountry(iso3DigitCountryCode);
            Set<State> allStates = addressDAO.listStatesInCountry(country);

            states = LevensteinDistanceTools.matchName(name, allStates);

            stateNameCache.put(iso3DigitCountryCode + ":" + name, states);
        }

        return states;
    }


    /**
     * Get a suburb by its name in the specified state
     *
     * @return Country instance, or null if not found
     */
    public Suburb lookupSuburb(String name, State state) {
        AddressDAO addressDAO = new AddressDAO(em);
        return addressDAO.lookupSuburb(name, state);
    }

    /**
     * Get a state by its abbreviation in the specified country
     *
     * @return Country instance, or null if not found
     */
    public State lookupStateByAbbr(String abbr, Country country) {
        AddressDAO addressDAO = new AddressDAO(em);
        return addressDAO.lookupStateByAbbr(abbr, country);
    }

    /**
     * Find a postcode in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<PostalCode> findPostCode(String name, String iso3DigitCountryCode) {
        AddressDAO addressDAO = new AddressDAO(em);

        Country country = addressDAO.lookupCountry(iso3DigitCountryCode);
        Set<PostalCode> suburbs = addressDAO.listPostCodesInCountry(country);

        return LevensteinDistanceTools.matchName(name, suburbs);
    }

    /**
     * Get a postcode by its name in the specified state
     *
     * @return PostCodeHandloe instance, or null if not found
     */
    public PostalCode lookupPostCode(String name, State state) {
        AddressDAO addressDAO = new AddressDAO(em);
        return addressDAO.lookupPostCode(name, state);
    }

     /**
     * Find a street in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Street> findStreet(String name, String iso3DigitCountryCode) {
        AddressDAO addressDAO = new AddressDAO(em);

        Country country = addressDAO.lookupCountry(iso3DigitCountryCode);
        Set<Street> streets = addressDAO.listStreetsInCountry(country);

        return LevensteinDistanceTools.matchName(name, streets);
    }

    public List<Street> findStreet(String name, final StreetType streetType, final StreetSection streetSection, Suburb suburb) {
        AddressDAO addressDAO = new AddressDAO(em);
        Set<Street> streets = addressDAO.listStreetsInSuburb(suburb);
        List<Street> filteredStreets = FilterTools.getMatching(streets, new Filter<Street>() {
            public boolean accept(Street street) {
                return ((streetType == null || streetType.equals(street.getStreetType())) && (streetSection == null || streetSection == StreetSection.NA || streetSection.equals(street.getSection())));
            }
        });
        return LevensteinDistanceTools.matchName(name, filteredStreets);        
    }

    /**
     * List all known addresses in a suburb
     *
     * @param suburb
     * @return
     */
    public Set<Address> listAddresses(Suburb suburb) {
        AddressDAO addressDAO = new AddressDAO(em);

        return addressDAO.listAddressesInSuburb(suburb);
    }

    /**
     * List all known addresses in a postCode
     *
     * @param postCode
     * @return
     */
    public Set<Address> listAddresses(PostalCode postCode) {
        AddressDAO addressDAO = new AddressDAO(em);

        return addressDAO.listAddressesInPostCode(postCode);
    }

     /**
     * List all known addresses on a street
     *
     * @param street
     * @return
     */
    public Set<Address> listAddresses(Street street) {
        AddressDAO addressDAO = new AddressDAO(em);

        return addressDAO.listAddressesInStreet(street);
     }

    public Country getCountryById(long id) {
        CountryDAO suburbDAO = new CountryDAO(em);
        return suburbDAO.findById(id);
    }


    /**
     * Lookup a state by its unique ID
     * @param id
     * @return
     */
    public State getStateById(long id) {
        StateDAO stateDAO = new StateDAO(em);
        return stateDAO.findById(id);
    }

    /**
     * Lookup a suburb by its unique ID
     * @param id
     * @return
     */
    public Suburb getSuburbById(long id) {
        SuburbDAO suburbDAO = new SuburbDAO(em);
        return suburbDAO.findById(id);
    }

    /**
     * Lookup a postcode by its unique ID
     * @param id
     * @return
     */
    public PostalCode getPostCodeById(long id) {
        PostCodeDAO postCodeDAO = new PostCodeDAO(em);
        return postCodeDAO.findById(id);
    }

    public Set<Country> listCountries() {
        AddressDAO addressDAO = new AddressDAO(em);
        return addressDAO.listCountries();
    }

    /** List the states in the specified country */
    public Set<State> listStates(Country country) {
        AddressDAO addressDAO= new AddressDAO(em);
        return addressDAO.listStatesInCountry(country);
    }

    /** List the suburbs in the specified state */
    public Set<Suburb> listSuburbs(State state) {
        AddressDAO addressDAO= new AddressDAO(em);
        return addressDAO.listSuburbsInState(state);
    }

     /** List the postCodes in the specified state */
    public Set<PostalCode> listPostCodes(State state) {
        AddressDAO addressDAO= new AddressDAO(em);
        return addressDAO.listPostCodesInState(state);
    }

    /**
     * Permanently merge two regions into one.
     * <p/>
     * The target region inherits the parents of the source
     * The target region inherits the children of the source
     * The target region inherits the aliases of the source
     * <p/>
     * This merge operation cannot be undone
     *
     * @param target
     * @param source
     * @return
     */
    public Region mergeRegions(Region target, Region source) {

        target.mergeWith(source);
        em.persist(source);
        em.persist(target);

        return target;
    }

    /**
     * Delete the specified Region by Id, setting its status to Deleted
     * @param id
     * @return
     */
    public void deleteRegionById(Long id) {
        regionGraphService.deleteRegionById(id);
    }

    public List<Region> autocompleteRegion(String name) {
        return regionGraphService.autocompleteRegion(name);
    }


    public void setSuburbPatternMatcherFactory(SuburbPatternMatcherFactory suburbPatternMatcherFactory) {
        this.suburbPatternMatcherFactory = suburbPatternMatcherFactory;
    }

    public void setAddressParserFactory(AddressParserFactory addressParserFactory) {
        this.addressParserFactory = addressParserFactory;
    }

    public void setRegionGraphService(RegionGraphService regionGraphService) {
        this.regionGraphService = regionGraphService;
    }
}
