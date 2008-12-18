package com.blueskyminds.homebyfive.business.address.service;

import com.blueskyminds.homebyfive.business.address.*;
import com.blueskyminds.homebyfive.business.address.dao.*;
import com.blueskyminds.homebyfive.business.address.patterns.*;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.service.*;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherException;
import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
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
    private EntityManager em;

        /** Allow AddressPatternMatcher's created in this instance to be reused */
    private Map<String, AddressParser> matcherCache;

    /** Allow AddressParser's created in this instance to be reused */
    private Map<Suburb, AddressParser> matcherCacheBySuburb;

    /** Cache of recently used suburb names */
    private Map<String, Suburb> suburbNameCache;

    private SuburbService suburbService;
    private StateService stateService;
    private StreetService streetService;
    private CountryService countryService;
    private PostalCodeService postalCodeService;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the address service
     *
     **/
    public AddressServiceImpl(EntityManager em) {    
        this.em = em;
        init();
    }

    public AddressServiceImpl(AddressParserFactory addressParserFactory, EntityManager em, CountryService countryService, StateService stateService, PostalCodeService postalCodeService, SuburbService suburbService, StreetService streetService) {
        this.addressParserFactory = addressParserFactory;
        this.em = em;
        this.countryService = countryService;
        this.postalCodeService = postalCodeService;
        this.suburbService = suburbService;
        this.stateService = stateService;
        this.streetService = streetService;
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
        suburbNameCache = new HashMap<String, Suburb>();
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
                addressParserFactory = new AddressPatternMatcher(countryAbbr, em, countryService, stateService, postalCodeService, suburbService, streetService);
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
    private AddressParser selectMatcher(String countryAbbr) throws PatternMatcherInitialisationException {
        AddressParser matcher = matcherCache.get(countryAbbr);
        if (matcher == null) {
            if (addressParserFactory == null) {
                addressParserFactory = new AddressPatternMatcher(countryAbbr, em, countryService, stateService, postalCodeService, suburbService, streetService);
            }
            matcher = addressParserFactory.create(countryAbbr);
            //matcher.setEntityManager(em);
            matcherCache.put(countryAbbr, matcher);
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
                addressParserFactory = new AddressPatternMatcher(suburbHandle, em, suburbService, streetService);
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
     * @param countryAbbr
     * @return an Address derived from the address string.  In the worst case, this will simply be a
     *  PlainTextAddress
     * @throws AddressProcessingException if a critical error occurs trying to process the address.
     */
    public Address parseAddress(String addressString, String suburbString, String stateString, String countryAbbr) throws AddressProcessingException {

        Address address = null;
        try {
            List<Suburb> suburbCandidates = null;

            if (StringUtils.isNotBlank(suburbString)) {

                Suburb suburbHandle = suburbNameCache.get(countryAbbr+":"+stateString+":"+suburbString);
                if (suburbHandle == null) {
                    LOG.info("suburbName "+countryAbbr+":"+stateString+":"+suburbString+" is not cached");
                    // lookup the suburb candidate(s)
                    if (StringUtils.isNotBlank(stateString)) {
                        List<State> stateCandidates = stateService.find(stateString, countryAbbr);
                        if (stateCandidates.size() > 0) {
                            suburbCandidates = suburbService.find(suburbString, stateCandidates);
                        }
                    } else {
                        // look for suburb in country (slower!)
                        suburbCandidates = suburbService.find(suburbString, countryAbbr);
                    }

                    if ((suburbCandidates != null) && (suburbCandidates.size() > 0)) {
                        // suburb has been constrained to a set of candidates.  The matching algorithm can be limited
                            // to a simple street address match
                        suburbHandle = suburbCandidates.iterator().next();
                    }

                    if (suburbHandle != null) {
                        suburbNameCache.put(countryAbbr+":"+stateString+":"+suburbString, suburbHandle);
                    } else {
                        // special-case: record an invalid entry so we don't look again
                        suburbNameCache.put(countryAbbr+":"+stateString+":"+suburbString, Suburb.INVALID);
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
                    AddressParser matcher = selectMatcher(countryAbbr);
                    LOG.info("Performing wide search for "+addressString+" in country "+countryAbbr);
                    address = matcher.parseAddress(addressString);
                    if (address == null) {
                        LOG.info("Failed to parse address "+addressString+" in country "+countryAbbr);
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
        Address addressFound = null;

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

            if (address.hasNumber()) {
                // find this address
                addressFound = new AddressDAO(em).lookupAddressByExample(address);
            }

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

                if (address.hasNumber()) {
                    em.persist(address);
                    // use the new address
                    addressFound = address;
                
                    LOG.info("Created new address: "+addressFound);
                } else {
                    LOG.info("Address rejected because it doesn't have a number: "+address);
                }
            } else {
                LOG.info("Found existing address: "+addressFound);
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

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    } 

    public void setAddressParserFactory(AddressParserFactory addressParserFactory) {
        this.addressParserFactory = addressParserFactory;
    }

    public void setSuburbService(SuburbService suburbService) {
        this.suburbService = suburbService;
    }

    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    public void setPostalCodeService(PostalCodeService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    public void setStreetService(StreetService streetService) {
        this.streetService = streetService;
    }
}
