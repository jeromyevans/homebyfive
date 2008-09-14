package com.blueskyminds.framework.persistence.hibernate.query;

import org.hibernate.Session;
import org.hibernate.Query;
import com.blueskyminds.framework.persistence.query.AbstractQuery;
import com.blueskyminds.framework.persistence.query.QueryParameter;

/**
 * Contains common code for hibernate's Queries, SqlQueries and Criteria for use with the PersistenceService
 *
 * Parameters:
 *  Q the underlying query implementation
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class HibernateQuery<Q> extends AbstractQuery<Session, Q> {

    protected HibernateQuery() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the HibernateQuery with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    protected void applyParamater(Q query, QueryParameter queryParameter) {
        if (query instanceof Query) {
            HibernateQueryParameterMapper.applyParamater((Query) query, queryParameter);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Associate the query with a Session - implementation depends on the query implementation
     *  (detached or attached)
    *
    * @param session
    */
    public abstract void prepareQuery(Session session);

}
