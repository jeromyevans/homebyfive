package com.blueskyminds.homebyfive.business.party.dao;

import com.blueskyminds.homebyfive.business.party.Party;
import com.blueskyminds.homebyfive.business.address.Address;
import com.blueskyminds.homebyfive.business.contact.ContactAddress;
import com.blueskyminds.homebyfive.business.contact.POCType;

import javax.persistence.Query;
import java.util.List;
import java.util.LinkedList;

/**
 * Common methods for the Party DAO implementations
 *
 * Date Started: 20/03/2009
 */
class PartyDAOSupport {

    public static final String PARAM_TAG_NAME = "name";
    public static final String PARAM_ADDRESSES = "addresses";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_TAGS = "tags";

    /**
     * Adds the addresses of a Party as a paramaeter to a query if an address is defined
     * If the party has multiple addresses then all addresses are considered
     *
     * @param party
     * @param query
     * @return true if one or more addresses were added
     */
    protected static boolean setAddressesParameter(Party party, Query query) {
        List<Address> addresses = new LinkedList<Address>();
        for (ContactAddress address : party.getAddresses()) {
            if (address.isPersistent()) {
                addresses.add(address.getAddress());
            }
        }
        if (addresses.size() > 0) {
            query.setParameter(PARAM_TYPE, POCType.Address);

            query.setParameter(PARAM_ADDRESSES, addresses);
            return true;
        } else {
            return false;
        }
    }
}
