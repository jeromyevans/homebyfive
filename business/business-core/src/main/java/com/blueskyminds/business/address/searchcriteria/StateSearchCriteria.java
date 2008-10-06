package com.blueskyminds.business.address.searchcriteria;

//import com.blueskyminds.homebyfive.framework.core.persistence.hibernate.query.HibernateCriteriaImpl;
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Expression;
//import org.hibernate.criterion.Restrictions;

/**
 * Implements factory methods that create search QueryBuilder for the State object
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class StateSearchCriteria {

    /** The name of the property used in a query to get the Country */
    private static final String COUNTRY_PROPERTY_NAME = "parents";

    // ------------------------------------------------------------------------------------------------------

    /** Creates a QueryBuilder that searches for a state by country (case insensitive) */
    /*public PersistenceQuery searchByCountry(Country country) {
        DetachedCriteria criteria = DetachedCriteria.forClass(State.class);
        //criteria.add(Expression.eq(COUNTRY_PROPERTY_NAME, country));
        criteria.createCriteria("parents").add(Restrictions.idEq(country));

        return new HibernateCriteriaImpl(criteria);
    }

    // ------------------------------------------------------------------------------------------------------

    *//** Creates a QueryBuilder that searches for a state by name (case insensitive) *//*
    public PersistenceQuery searchByName(String name, Country country) {
        DetachedCriteria criteria = DetachedCriteria.forClass(State.class);
        criteria.add(Expression.eq(COUNTRY_PROPERTY_NAME, country));
        criteria.add(Expression.ilike("name", name));
        return new HibernateCriteriaImpl(criteria);
    }

    // ------------------------------------------------------------------------------------------------------

    *//** Creates a QueryBuilder that searches for a state by its abbreviation (case insensitive) *//*
    public PersistenceQuery searchByAbbr(String name, Country country) {
        DetachedCriteria criteria = DetachedCriteria.forClass(State.class);
        criteria.add(Expression.eq(COUNTRY_PROPERTY_NAME, country));
        criteria.add(Expression.ilike("abbreviation", name));
        return new HibernateCriteriaImpl(criteria);
    }
*/
    // ------------------------------------------------------------------------------------------------------
}
