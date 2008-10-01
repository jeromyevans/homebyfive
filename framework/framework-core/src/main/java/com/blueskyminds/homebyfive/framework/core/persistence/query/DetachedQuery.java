package com.blueskyminds.homebyfive.framework.core.persistence.query;

import javax.persistence.TemporalType;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.util.Calendar;

/**
 * A vendor technology independent query.  It needs to be transformed before it can be used.
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class DetachedQuery implements PersistenceQuery {

    private List<QueryParameter> parameters;
    private String queryString;

    // ------------------------------------------------------------------------------------------------------

    protected DetachedQuery() {
        init();
    }

    public DetachedQuery(String queryString) {
        init();
        this.queryString = queryString;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractQuery with default attributes
     */
    private void init() {
        parameters = new LinkedList<QueryParameter>();
    }

    // ------------------------------------------------------------------------------------------------------

    protected void addParameter(QueryParameter queryParameter) {
        parameters.add(queryParameter);
    }

    // ------------------------------------------------------------------------------------------------------

    public List<QueryParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<QueryParameter> parameters) {
        this.parameters = parameters;
    }

    public DetachedQuery setParameter(String parameterName, Object object) {
        addParameter(new QueryObjectParameter(parameterName, object));
        return this;
    }

    public DetachedQuery setParameter(String parameterName, Date date, TemporalType temporalType) {
        addParameter(new QueryTemporalParameter(parameterName, date, temporalType));
        return this;
    }

    public DetachedQuery setParameter(String parameterName, Calendar calendar, TemporalType temporalType)  {
        addParameter(new QueryTemporalParameter(parameterName, calendar, temporalType));
        return this;
    }

    public DetachedQuery setParameter(int index, Object object) {
        addParameter(new QueryObjectParameter(index, object));
        return this;
    }

    public DetachedQuery setParameter(int index, Date date, TemporalType temporalType) {
        addParameter(new QueryTemporalParameter(index, date, temporalType));
        return this;
    }

    public DetachedQuery setParameter(int index, Calendar calendar, TemporalType temporalType) {
        addParameter(new QueryTemporalParameter(index, calendar, temporalType));
        return this;
    }

    public String getQueryString() {
        return queryString;
    }
}