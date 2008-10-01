package com.blueskyminds.homebyfive.framework.core.persistence.jpa.query;

import com.blueskyminds.homebyfive.framework.core.persistence.query.QueryParameter;
import com.blueskyminds.homebyfive.framework.core.persistence.query.QueryObjectParameter;
import com.blueskyminds.homebyfive.framework.core.persistence.query.QueryTemporalParameter;

import javax.persistence.Query;
import java.util.Collection;

/**
 * Provides methods to apply a vendor-independent QueryParameter to a JpaQuery
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JpaQueryParameterMapper {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the JpaQueryParameterMapper with default attributes
     */
    private void init() {
    }

     /** Apply a parameter to the query */ 
    public static void applyParamater(Query query, QueryParameter queryParameter) {
        if (queryParameter instanceof QueryObjectParameter) {
            if (queryParameter.isNamed()) {
                query.setParameter(queryParameter.getName(), ((QueryObjectParameter) queryParameter).getObject());
            } else {
                query.setParameter(queryParameter.getIndex(), ((QueryObjectParameter) queryParameter).getObject());
            }
        } else {

            QueryTemporalParameter temporalParameter = (QueryTemporalParameter) queryParameter;
            if (queryParameter.isNamed()) {
                if (temporalParameter.isDate()) {
                    query.setParameter(temporalParameter.getName(), temporalParameter.getDate(), temporalParameter.getTemporalType());
                } else {
                    query.setParameter(temporalParameter.getName(), temporalParameter.getCalendar(), temporalParameter.getTemporalType());
                }
            } else {
                if (temporalParameter.isDate()) {
                    query.setParameter(temporalParameter.getIndex(), temporalParameter.getDate(), temporalParameter.getTemporalType());
                } else {
                    query.setParameter(temporalParameter.getIndex(), temporalParameter.getCalendar(), temporalParameter.getTemporalType());
                }
            }
        }
    }


    /**
     * Apply the vendor-independent parameters to a Query object
     *
     * */
    public static void applyParameters(Query query, Collection<QueryParameter> parameters) {
        for (QueryParameter parameter : parameters) {
            applyParamater(query, parameter);
        }
    }

    // ------------------------------------------------------------------------------------------------------
}
