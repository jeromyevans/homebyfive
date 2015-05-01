package com.blueskyminds.homebyfive.business.address.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.query.ParameterisedQuery;
import com.blueskyminds.homebyfive.framework.core.persistence.query.QueryParameterMixin;
import com.blueskyminds.homebyfive.framework.core.persistence.query.QueryParameter;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.query.JpaQueryParameterMapper;

import javax.persistence.TemporalType;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.util.Calendar;

/**
 * A simple implementation of a programmatic interface for building a query
 * <p/>
 * Date Started: 12/06/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */

public class QueryBuilder implements ParameterisedQuery<QueryBuilder> {

    private Class entityClass;
    private String alias;
    private List<String> conditions;
    private QueryParameterMixin queryParameterMixin;
    private String select;

    public QueryBuilder(Class entityClass, String alias) {
        this.entityClass = entityClass;
        this.alias = alias;
        init();
    }

    public QueryBuilder(String select) {
        this.select = select;
        init();
    }

    private void init() {
        conditions = new LinkedList<String>();
        queryParameterMixin = new QueryParameterMixin();
    }

    public void addCondition(String condition) {
        conditions.add(condition);
    }

    public QueryBuilder setParameter(String parameterName, Object object) {
        queryParameterMixin.setParameter(parameterName, object);
        return this;
    }

    public QueryBuilder setParameter(String parameterName, Date date, TemporalType temporalType) {
        queryParameterMixin.setParameter(parameterName, date, temporalType);
        return this;
    }

    public QueryBuilder setParameter(String parameterName, Calendar calendar, TemporalType temporalType) {
        queryParameterMixin.setParameter(parameterName, calendar, temporalType);
        return this;
    }

    public QueryBuilder setParameter(int index, Object object) {
        queryParameterMixin.setParameter(index, object);
        return this;
    }

    public QueryBuilder setParameter(int index, Date date, TemporalType temporalType) {
        queryParameterMixin.setParameter(index, date, temporalType);
        return this;
    }

    public QueryBuilder setParameter(int index, Calendar calendar, TemporalType temporalType) {
        queryParameterMixin.setParameter(index, calendar, temporalType);
        return this;
    }

    // ------------------------------------------------------------------------------------------------------

    public List<QueryParameter> getParameters() {
        return queryParameterMixin.getParameters();
    }

    public void setParameters(List<QueryParameter> parameters) {
        queryParameterMixin.setParameters(parameters);
    }

    /**
     * Generates a detached PersistenceQuery using the QueryBuilder conditions and parameters
     *
     * @return a detached PersistenceQuery
     */
    public Query generateQuery(EntityManager em) {
        Query query = em.createQuery(getQueryString());
        JpaQueryParameterMapper.applyParameters(query, getParameters());

        return query;
    }

    /**
     * Get the JQL as a string.
     *
     * @return
     */
    public String getQueryString() {
        StringBuilder queryString;
        boolean first = true;
        if (select != null) {
            queryString = new StringBuilder(select);
        } else {
            String entityName = entityClass.getSimpleName();
            queryString = new StringBuilder("select " + alias + " from " + entityName + " " + alias);
        }

        for (String condition : conditions) {
            if (first) {
                queryString.append(" where ");
                first = false;
            } else {
                queryString.append(" and ");
            }

            queryString.append(condition);
        }
        return queryString.toString();
    }

    @Override
    public String toString() {
        return getQueryString();
    }
}