package com.blueskyminds.homebyfive.business.address.service;

import com.blueskyminds.homebyfive.business.address.Address;
import com.blueskyminds.homebyfive.business.address.StreetType;
import com.blueskyminds.homebyfive.business.address.StreetSection;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;

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
     * @param countryAbbr
     * @return an Address derived from the address string.  In the worst case, this will simply be a
     *  PlainTextAddress
     * @throws AddressProcessingException if a critical error occurs trying to process the address.
     */
    Address parseAddress(String addressString, String countryAbbr) throws AddressProcessingException;

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
    Address parseAddress(String addressString, String suburbString, String stateString, String countryAbbr) throws AddressProcessingException;

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

    /**
     * Lookup an address in the specified country.  Returns possible matches
     *
     * @param addressString
     * @return
     */
    List<Address> findAddress(String addressString, String iso3CountryCode);

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

}
