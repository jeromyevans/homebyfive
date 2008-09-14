package com.blueskyminds.framework.persistence.query;

import javax.persistence.TemporalType;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.util.Calendar;

/**
 * A mixin that provides support for Query Parameters
 *
 * Date Started: 2/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class QueryParameterMixin {

    private List<QueryParameter> parameters;

    public QueryParameterMixin() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the QueryParameterMixin with default attributes
     */
    private void init() {
        parameters = new LinkedList<QueryParameter>();
    }

    // ------------------------------------------------------------------------------------------------------

    public void setParameter(String parameterName, Object object) {
        addParameter(new QueryObjectParameter(parameterName, object));
    }

    public void setParameter(String parameterName, Date date, TemporalType temporalType) {
        addParameter(new QueryTemporalParameter(parameterName, date, temporalType));
    }

    public void setParameter(String parameterName, Calendar calendar, TemporalType temporalType)  {
        addParameter(new QueryTemporalParameter(parameterName, calendar, temporalType));
    }

    public void setParameter(int index, Object object) {
        addParameter(new QueryObjectParameter(index, object));
    }

    public void setParameter(int index, Date date, TemporalType temporalType) {
        addParameter(new QueryTemporalParameter(index, date, temporalType));
    }

    public void setParameter(int index, Calendar calendar, TemporalType temporalType) {
        addParameter(new QueryTemporalParameter(index, calendar, temporalType));
    }

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
}
