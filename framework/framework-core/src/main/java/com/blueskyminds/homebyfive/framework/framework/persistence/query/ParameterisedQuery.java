package com.blueskyminds.homebyfive.framework.framework.persistence.query;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Interface to a Query that accepts parameters
 *
 * Date Started: 2/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface ParameterisedQuery<Q> {

    Q setParameter(String parameterName, Object object);
    Q setParameter(String parameterName, Date date, TemporalType temporalType);
    Q setParameter(String parameterName, Calendar calendar, TemporalType temporalType);
    Q setParameter(int index, Object object);
    Q setParameter(int index, Date date, TemporalType temporalType);
    Q setParameter(int index, Calendar calendar, TemporalType temporalType);

    /** Get the list of query parameters */
    List<QueryParameter> getParameters();

    void setParameters(List<QueryParameter> parameters);
}
