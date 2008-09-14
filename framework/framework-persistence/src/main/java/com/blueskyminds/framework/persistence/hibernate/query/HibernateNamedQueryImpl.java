package com.blueskyminds.framework.persistence.hibernate.query;

import org.hibernate.Query;
import org.hibernate.Session;
import com.blueskyminds.framework.persistence.query.PersistenceNamedQuery;

/**
 * An implementation of PersistenceQuery that wraps an instance of hibernate's Query object
 *
 * Date Started: 16/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class HibernateNamedQueryImpl extends HibernateQuery<Query> implements PersistenceNamedQuery {

    private String name;

    // ------------------------------------------------------------------------------------------------------

    public HibernateNamedQueryImpl(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create the query from the session and apply the parameters to it */
    public void prepareQuery(Session session) {
        Query query = session.getNamedQuery(name);
        applyParameters(query);
        setQuery(query);
    }

    // ------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    /** There is no query string */
    public String getQueryString() {
        return null;
    }
}
