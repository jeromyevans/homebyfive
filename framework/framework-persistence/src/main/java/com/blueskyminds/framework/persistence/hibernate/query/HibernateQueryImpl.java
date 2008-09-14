package com.blueskyminds.framework.persistence.hibernate.query;

import org.hibernate.Query;
import org.hibernate.Session;
import com.blueskyminds.framework.persistence.query.PersistenceQuery;

/**
 * Wraps a hibernate HQL query
 *
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class HibernateQueryImpl extends HibernateQuery<Query> implements PersistenceQuery {

    private String queryString;

    // ------------------------------------------------------------------------------------------------------

    /**
     * @param queryString a HQL query string
     */
    public HibernateQueryImpl(String queryString) {
        this.queryString = queryString;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create the query from the session and apply the parameters to it */
    public void prepareQuery(Session session) {
        Query query = session.createQuery(queryString);
        applyParameters(query);
        setQuery(query);
    }

    // ------------------------------------------------------------------------------------------------------

    public String getQueryString() {
        return queryString;
    }
}
