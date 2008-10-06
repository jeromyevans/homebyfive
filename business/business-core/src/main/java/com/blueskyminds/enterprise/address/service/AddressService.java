package com.blueskyminds.enterprise.address.service;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.enterprise.address.StreetSection;
import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.enterprise.region.graph.*;
import com.blueskyminds.enterprise.region.graph.Suburb;

import java.util.List;
import java.util.Set;

/**
 * A service for manipulating Addresses
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface AddressService {

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
    Address parseAddress(String addressString, String iso3CountryCode) throws AddressProcessingException;

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
    Address parseAddress(String addressString, String suburbString, String stateString, String iso3CountryCode) throws AddressProcessingException;

    /**
     * Creates Addresses matching the plain-text address string
     *
     * The addresses returned are not persistent, they're just parsed.
     *
     * @param addressString plain text address
     * @param countryAbbr
     * @param maxMatches            maximum number of results to return
     *
     * @return Addresses derived from the address string.  In the worst case, this will simply be a
     *  PlainTextAddress
     * @throws AddressProcessingException if a critical error occurs trying to process the address.
     */
    List<Address> parseAddressCandidates(String addressString, String countryAbbr, int maxMatches) throws AddressProcessingException;

    /**
     * Persists an address.  If the address already exists the existing address is returned
     *
     * @param addressString
     * @return the new or existing Address
     * @throws AddressProcessingException if a persistence problem occurs
     */
    Address lookupOrCreateAddress(String addressString, String countryAbbr) throws AddressProcessingException;

    /**
     * Persists an address in a known suburb.  If the address already exists the existing address is returned
     *
     * @param addressString
     * @return the new or existing Address
     * @throws AddressProcessingException if a persistence problem occurs
     */
    Address lookupOrCreateAddress(String addressString, String suburbString, String stateString, String countryAbbr) throws AddressProcessingException;

    /**
     * Persists an address.  If the address already exists the existing address is returned
     *
     * @param address to persist, or lookup if it already exists
     * @return the new or existing Address
     * @throws AddressProcessingException if a persistence problem occurs
     */
    Address lookupOrCreateAddress(Address address) throws AddressProcessingException;


    /** I don't know about this... - i should wait until its needed */
//    Set<Region> lookupSubregions(Region region);
//    Country lookupCountry(Region region);

    @Deprecated
    List<Suburb> findSuburbLike(String name, String iso3CountryCode);

    /**
     * Lookup an address in the specified country.  Returns possible matches
     *
     * @param addressString
     * @return
     */
    List<Address> findAddress(String addressString, String iso3CountryCode);

    /** Create and persist a new country */
    Country createCountry(String name, String iso2CountryCode, String iso3CountryCode, String currencyCode);

    /** Create and persist a new state */
    State createState(String name, String abbreviation, Country parent);

    /** Create and persist a new PostCode */
    PostalCode createPostCode(String name, State parent);

    /** Create and persist a new Suburb */
    Suburb createSuburb(String name, State parent);

    /**
     * Find the country with the specified name
     *
     * Performs a fuzzy match and returns the matches in order of rank
     **/
    List<Country> findCountry(String name);

    /**
     * Get an instance of a country matching the 2 digit country code  eg. AU
     *
     * @param iso2DigitCode
     * @return Country instance, or null if not found
     */
    Country lookupCountry(String iso2DigitCode);

    /**
     * Get a state by its abbreviation in the specified country
     *
     * @return Country instance, or null if not found
     */
    State lookupStateByAbbr(String abbr, Country country);

    /**
     * Find a suburb in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<Suburb> findSuburb(String name, String iso3DigitCountryCode);

     /**
     * Find a suburb in the specified state
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<Suburb> findSuburb(String name, State state);

     /**
     * Get a suburb by its name in the specified state
     *
     * @return Country instance, or null if not found
     */
    Suburb lookupSuburb(String name, State state);

    /**
     * Find a postcode in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<PostalCode> findPostCode(String name, String iso3DigitCountryCode);

    /**
     * Get a postcode by its name in the specified state
     *
     * @return PostCodeHandloe instance, or null if not found
     */
    PostalCode lookupPostCode(String name, State state);

    /**
     * Find a street in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<Street> findStreet(String name, String iso3DigitCountryCode);

    List<Street> findStreet(String name, StreetType streetType, StreetSection streetSection, Suburb suburb);

    /**
     * List all known addresses in a suburb
     *
     * @param suburb
     * @return
     */
    Set<Address> listAddresses(Suburb suburb);

    /**
     * List all known addresses in a postCode
     *
     * @param postCode
     * @return
     */
    Set<Address> listAddresses(PostalCode postCode);

    /**
     * List all known addresses on a street
     *
     * @param street
     * @return
     */
    Set<Address> listAddresses(Street street);

    /**
     * Lookup a country by its unique ID
     * @param id
     * @return
     */
    Country getCountryById(long id);

    /**
     * Lookup a state by its unique ID
     * @param id
     * @return
     */
    State getStateById(long id);

    /**
     * Lookup a suburb by its unique ID
     * @param id
     * @return
     */
    Suburb getSuburbById(long id);

     /**
     * Lookup a postcode by its unique ID
     * @param id
     * @return
     */
    PostalCode getPostCodeById(long id);

    /**
     * Get the set of Countries
     *
     * @return
     */
    Set<Country> listCountries();

    Set<State> listStates(Country country);

    /** List the suburbs in the specified state */
    Set<Suburb> listSuburbs(State state);

    /** List the postCodes in the specified state */
    Set<PostalCode> listPostCodes(State state);


    /**
     * Permanently merge two regions into one.
     *
     * The target region inherits the parents of the source
     * The target region inherits the children of the source
     * The target region inherits the aliases of the source
     *
     * This merge operation cannot be undone
     *
     * @param target
     * @param source
     * @return
     */
    Region mergeRegions(Region target, Region source);

    /**
     * Delete the specified Region by Id, setting its status to Deleted
     * @param id
     * @return
     */
    void deleteRegionById(Long id);

    /**
     * Get a list of regions matching the incomplete name provided.
     * Unlike the fuzzy matchers, this algorithm matches from left to right.
     *
     * @param name
     * @return
     */
    List<Region> autocompleteRegion(String name);


}
