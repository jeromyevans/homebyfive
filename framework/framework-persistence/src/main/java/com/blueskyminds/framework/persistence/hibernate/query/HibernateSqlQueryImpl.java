package com.blueskyminds.framework.persistence.hibernate.query;

import com.blueskyminds.framework.persistence.query.PersistenceQuery;
import org.hibernate.Session;
import org.hibernate.SQLQuery;
import org.hibernate.Query;

/**
 *
 * An implementation of PersistenceQuery that wraps an instance of hibernate's SQLQuery object
 *
 * The SQLQuery object is used to implement native SQL
 *
 * Date Started: 28/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class HibernateSqlQueryImpl extends HibernateQuery<SQLQuery> implements PersistenceQuery {

    private String queryString;

    // ------------------------------------------------------------------------------------------------------

    public HibernateSqlQueryImpl(String queryString) {
        this.queryString = queryString;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create the query from the session and apply the parameters to it */
    public void prepareQuery(Session session) {
       SQLQuery query = session.createSQLQuery(queryString);
       applyParameters(query);
       setQuery(query);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------


    public String getQueryString() {
        return queryString;
    }
}
