package com.blueskyminds.enterprise.ejb.address;

import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.address.service.AddressProcessingException;
import com.blueskyminds.enterprise.address.Address;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * A stateless session bean providing access to the Address functions
 *
 * This implemenetation extends the non-EJB AddressServiceImpl
 * 
 * Date Started: 29/06/2007
 * <p/>
 * History:
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AddressSessionBean extends AddressServiceImpl implements AddressSessionBeanLocal {

    public AddressSessionBean(EntityManager em) {
        super(em);
    }

    public AddressSessionBean() {
    }

    /**
     * Creates an Address from a plain-text address string
     * <p/>
     * The address returned is not persisted.
     *
     * @param addressString   plain text address
     * @param iso3CountryCode
     * @return an Address derived from the address string.  In the worst case, this will simply be a
     *         PlainTextAddress
     * @throws com.blueskyminds.enterprise.address.service.AddressProcessingException
     *          if a critical error occurs trying to process the address.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Address parseAddress(String addressString, String iso3CountryCode) throws AddressProcessingException {
        return super.parseAddress(addressString, iso3CountryCode);
    }

    /**
     * Persists an address.  If the address already exists the existing address is returned
     *
     * @param address to persist, or lookup if it already exists
     * @return the new or existing Address
     * @throws com.blueskyminds.enterprise.address.service.AddressProcessingException
     *          if a persistence problem occurs
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Address lookupOrCreateAddress(Address address) throws AddressProcessingException {
        return super.lookupOrCreateAddress(address);
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }
}
