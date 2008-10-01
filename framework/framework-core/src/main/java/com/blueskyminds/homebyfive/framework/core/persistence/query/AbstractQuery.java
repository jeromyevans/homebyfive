package com.blueskyminds.homebyfive.framework.core.persistence.query;

import com.blueskyminds.homebyfive.framework.core.persistence.query.PersistenceQuery;
import com.blueskyminds.homebyfive.framework.core.persistence.query.QueryParameter;

import javax.persistence.TemporalType;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * Abstract superclass for a vendor independent object query.
 *  to an EntityManager.
 * <p/>
 * Date Started: 12/01/2007
 * <p/>
 * Parameters:
 *   F the factory class that can generate Query implementations
 *   Q the underlying query implementation
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class AbstractQuery<F, Q> implements PersistenceQuery {

    private QueryParameterMixin queryParameterMixin;
    private Q query;

    protected AbstractQuery() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractQuery with default attributes
     */
    private void init() {
        queryParameterMixin = new QueryParameterMixin();
    }

    // ------------------------------------------------------------------------------------------------------

    public AbstractQuery setParameter(String parameterName, Object object) {
        queryParameterMixin.setParameter(parameterName, object);
        return this;
    }

    public AbstractQuery setParameter(String parameterName, Date date, TemporalType temporalType) {
        queryParameterMixin.setParameter(parameterName, date, temporalType);
        return this;
    }

    public AbstractQuery setParameter(String parameterName, Calendar calendar, TemporalType temporalType)  {
        queryParameterMixin.setParameter(parameterName, calendar, temporalType);
        return this;
    }

    public AbstractQuery setParameter(int index, Object object) {
        queryParameterMixin.setParameter(index, object);
        return this;
    }

    public AbstractQuery setParameter(int index, Date date, TemporalType temporalType) {
        queryParameterMixin.setParameter(index, date, temporalType);
        return this;
    }

    public AbstractQuery setParameter(int index, Calendar calendar, TemporalType temporalType) {
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

    /** Apply the parameters to a Query object */
    protected void applyParameters(Q query) {
        for (QueryParameter parameter : getParameters()) {
            applyParamater(query, parameter);
        }
    }

    /** Apply a parameter to the query (if supported) */
    protected abstract void applyParamater(Q query, QueryParameter queryParameter);


    // ------------------------------------------------------------------------------------------------------

    /**
     * Prepare the query for execution. This is the place the underlying can be instantiated from a
     *  factory or attached to a PersistenceContext
     *
     * @param queryFactory
     */
    public abstract void prepareQuery(F queryFactory);

    // ------------------------------------------------------------------------------------------------------

    protected void setQuery(Q query) {
        this.query = query;
    }

    public Q getUnderlying() {
        return query;
    }
}
