package com.blueskyminds.framework.persistence.hibernate.query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SQLQuery;
import com.blueskyminds.framework.persistence.query.PersistenceQuery;

/**
 * An implementation of PersistenceQuery that wraps an instance of hibernate's Critera object
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class HibernateCriteriaImpl extends HibernateQuery<Criteria> implements PersistenceQuery {

    private DetachedCriteria criteria;

    // ------------------------------------------------------------------------------------------------------

    public HibernateCriteriaImpl(DetachedCriteria criteria) {
        this.criteria = criteria;
    }

    // ------------------------------------------------------------------------------------------------------

    public void prepareQuery(Session session) {
        Criteria query = criteria.getExecutableCriteria(session);
        applyParameters(query);
        setQuery(query);        
    }

    // ------------------------------------------------------------------------------------------------------


    public String getQueryString() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
