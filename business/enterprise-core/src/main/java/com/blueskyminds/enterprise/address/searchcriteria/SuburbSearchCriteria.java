package com.blueskyminds.enterprise.address.searchcriteria;

//import com.blueskyminds.framework.persistence.hibernate.query.HibernateCriteriaImpl;
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Expression;

/**
 * Implements factory methods that create search QueryBuilder for the Suburb object
 *
 * Date Started: 12/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class SuburbSearchCriteria {

     /** The name of the property used in a query to get the State */
    private static final String STATE_PROPERTY_NAME = "parent";

    // ------------------------------------------------------------------------------------------------------

    /** Creates a QueryBuilder that searches for a suburb by name (case insensitive) */
    /*public PersistenceQuery searchByName(String name, State state) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Suburb.class);
        criteria.add(Expression.eq(STATE_PROPERTY_NAME, state));
        criteria.add(Expression.ilike("name", name));
        return new HibernateCriteriaImpl(criteria);
    }*/

    // ------------------------------------------------------------------------------------------------------

    /** Creates a QueryBuilder that searches for suburb by the state */
    /*public PersistenceQuery searchByState(State state) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Suburb.class);
        criteria.add(Expression.eq(STATE_PROPERTY_NAME, state));
        return new HibernateCriteriaImpl(criteria);
    }*/

    // ------------------------------------------------------------------------------------------------------
}
