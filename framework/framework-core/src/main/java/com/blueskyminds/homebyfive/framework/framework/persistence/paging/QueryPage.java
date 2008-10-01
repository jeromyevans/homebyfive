package com.blueskyminds.homebyfive.framework.framework.persistence.paging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Common code for implementing a Paging query mechanism
 * A query is performed at construction time.
 *
 * PageNo starts at 0
 *
 * Parameters:
 *   T is the class of the result
 *   Q is the query implementation
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class QueryPage<T, Q> extends AbstractPage<T> {

    private static Log LOG = LogFactory.getLog(QueryPage.class);

    // ------------------------------------------------------------------------------------------------------

    /** Note: the query is executed immediately */
    protected QueryPage(Q query, int pageNo, int pageSize) {
        super(pageNo, pageSize);
        executeQuery(query, pageNo, pageSize);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Performs a paginated query.
     * Results the first result based on the pageNumber and pageNo size, and results pageSize results
     *  (plus one, to determine if three's another pageNo available)
     *
     * @param query
     * @param pageNo starts at 0
     * @param pageSize
     */
    public void executeQuery(Q query, int pageNo, int pageSize) {

        results = doQuery(query, pageNo * pageSize, pageSize+1);

        if (LOG.isInfoEnabled()) {
            LOG.info("Loading page: "+pageNo+ "(pageSize="+pageSize+", resultsInThisPage="+results.size()+")");
        }
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Performs the paginated query.
     * Return the results from firstResult and up to maxResults
     **/
    public abstract List<T> doQuery(Q query, int firstResult, int maxResults);


}