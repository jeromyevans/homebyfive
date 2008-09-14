package com.blueskyminds.enterprise.ejb.address;

import com.blueskyminds.enterprise.address.service.AddressProcessingException;
import com.blueskyminds.enterprise.address.Address;

import javax.ejb.Local;

/**
 * Local interface to the AddressSessionBean
 *
 * Date Started: 29/06/2007
 * <p/>
 * History:
 */
@Local
public interface AddressSessionBeanLocal {

    /**
     * Creates an Address from a plain-text address string
     *
     * The address returned is not persisted.
     *
     * @param addressString plain text address
     * @param iso3CountryCode
     * @return an Address derived from the address string.  In the worst case, this will simply be a
     *  PlainTextAddress
     * @throws com.blueskyminds.enterprise.address.service.AddressProcessingException if a critical error occurs trying to process the address.
     */
    Address parseAddress(String addressString, String iso3CountryCode) throws AddressProcessingException;

    /**
     * Persists an address.  If the address already exists the existing address is returned
     *
     * @param address to persist, or lookup if it already exists
     * @return the new or existing Address
     * @throws AddressProcessingException if a persistence problem occurs
     */
    Address lookupOrCreateAddress(Address address) throws AddressProcessingException;

}
