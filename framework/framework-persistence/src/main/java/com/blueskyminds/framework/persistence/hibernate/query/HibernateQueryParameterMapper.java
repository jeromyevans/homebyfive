package com.blueskyminds.framework.persistence.hibernate.query;

import com.blueskyminds.framework.persistence.query.QueryObjectParameter;
import com.blueskyminds.framework.persistence.query.QueryTemporalParameter;
import com.blueskyminds.framework.persistence.query.QueryParameter;
import org.hibernate.Query;
import org.hibernate.Criteria;

/**
 * Provides methods to apply a vendor-independent QueryParameter to a Query or Criteria
 *
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class HibernateQueryParameterMapper {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the HibernateQueryParameterMapper with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

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

            if (temporalParameter.isNamed()) {
                switch (temporalParameter.getTemporalType()) {
                    case DATE:
                        query.setDate(temporalParameter.getName(), temporalParameter.getDate());
                        break;
                    case TIME:
                        query.setTime(temporalParameter.getName(), temporalParameter.getDate());
                        break;
                    case TIMESTAMP:
                        query.setTimestamp(temporalParameter.getName(), temporalParameter.getDate());
                        break;
                }
            } else {
                switch (temporalParameter.getTemporalType()) {
                    case DATE:
                        query.setDate(temporalParameter.getIndex(), temporalParameter.getDate());
                        break;
                    case TIME:
                        query.setTime(temporalParameter.getIndex(), temporalParameter.getDate());
                        break;
                    case TIMESTAMP:
                        query.setTimestamp(temporalParameter.getIndex(), temporalParameter.getDate());
                        break;
                }
            }
        }
    }
}
