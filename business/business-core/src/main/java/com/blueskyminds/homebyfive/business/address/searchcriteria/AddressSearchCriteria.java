package com.blueskyminds.homebyfive.business.address.searchcriteria;

//import com.blueskyminds.homebyfive.framework.core.persistence.hibernate.query.HibernateCriteriaImpl;
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Expression;

/**
 * Implements factory methods that create search criteria for searching for an address matching the input parameters
 *
 * Date Started: 25/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class AddressSearchCriteria {

    // ------------------------------------------------------------------------------------------------------

  /*  *//** Creates a QueryBuilder that searches for an address.
     *
     * Presently supports :
     *   Address
     *   StreetAddress
     *   UnitAddress
     *   LotAddress
     *
     **//*
    public PersistenceQuery search(Address address) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Address.class);

        if (address.getSuburb() != null) {
            criteria.add(Expression.eq("suburb", address.getSuburb()));
        }

        if (address instanceof StreetAddress) {
            StreetAddress streetAddress = (StreetAddress) address;

            if (streetAddress.getStreet() != null) {
                criteria.add(Expression.eq("street", streetAddress.getStreet()));
            }

            if (streetAddress.getStreetNumber() != null) {
                criteria.add(Expression.eq("streetNumber", streetAddress.getStreetNumber()));
            }

            if (address instanceof UnitAddress) {
                UnitAddress unitAddress = (UnitAddress) streetAddress;
                if (unitAddress.getUnitNumber() != null) {
                    criteria.add(Expression.eq("unitNumber", unitAddress.getUnitNumber()));
                }
            } else {
                if (address instanceof LotAddress) {
                    LotAddress lotAddress = (LotAddress) address;
                    if (lotAddress.getLotNumber() != null) {
                       criteria.add(Expression.eq("lotNumber", lotAddress.getLotNumber()));
                    }
                }
            }
        }

        return new HibernateCriteriaImpl(criteria);
    }
*/
    // ------------------------------------------------------------------------------------------------------
}
