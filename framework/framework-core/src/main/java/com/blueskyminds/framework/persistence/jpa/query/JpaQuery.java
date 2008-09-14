package com.blueskyminds.framework.persistence.jpa.query;

import com.blueskyminds.framework.persistence.query.AbstractQuery;
import com.blueskyminds.framework.persistence.query.QueryParameter;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Abstract superclass for a EJB-QL query. Provides a generic interface to define parameters and attach
 *  to an EntityManager.
 * <p/>
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JpaQuery extends AbstractQuery<EntityManager, Query> {

    private String queryString;

    protected JpaQuery() {
        init();
    }

    public JpaQuery(String queryString) {
        this.queryString = queryString;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractQuery with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Apply a parameter to the query */
    protected void applyParamater(Query query, QueryParameter queryParameter) {
        JpaQueryParameterMapper.applyParamater(query, queryParameter);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Associate the query with an EntityManager - most likely, the implementation will use the
     *  EntityManager to create an instance of the underlying Query implementation
     *
     * @param entityManager
     */
    public void prepareQuery(EntityManager entityManager) {
        Query query = entityManager.createQuery(queryString);
        applyParameters(query);
        setQuery(query);
    }
    
    // ------------------------------------------------------------------------------------------------------

    public String getQueryString() {
        return queryString;
    }
}
