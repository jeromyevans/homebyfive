package com.blueskyminds.business.address.searchcriteria;

//import com.blueskyminds.homebyfive.framework.core.persistence.hibernate.query.HibernateCriteriaImpl;
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Expression;

/**
 * Implements factory methods to generate PersistenceQuery to find post codes
 *
 * Date Started: 14/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class PostCodeSearchCriteria {

    /** The name of the property used in a query to get the Country */
    private static final String COUNTRY_PROPERTY_NAME = "parents";

    // ------------------------------------------------------------------------------------------------------

    /** Creates a QueryBuilder that searches for a state by country (case insensitive) */
//    public PersistenceQuery searchByCountry(Country country) {
//        DetachedCriteria criteria = DetachedCriteria.forClass(PostCode.class);
//        criteria.add(Expression.eq(COUNTRY_PROPERTY_NAME, country));
//        return new HibernateCriteriaImpl(criteria);
//    }

    // ------------------------------------------------------------------------------------------------------
}