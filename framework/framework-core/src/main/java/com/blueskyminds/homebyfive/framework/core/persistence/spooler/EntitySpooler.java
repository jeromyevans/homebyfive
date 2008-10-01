package com.blueskyminds.homebyfive.framework.core.persistence.spooler;

import com.blueskyminds.homebyfive.framework.core.persistence.paging.Page;
import com.blueskyminds.homebyfive.framework.core.persistence.paging.QueryPager;
import com.blueskyminds.homebyfive.framework.core.persistence.paging.QueryPagerWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Query;

/**
 * A specialzed PageSpooler that uses....
 *
 * An abstract class that:
 *   retrieves pages of domain objects from persistence;
 *   tiggers processing on the domain objects by spooling them out for processing one {@link Page} at a time
 *
 * Typically used for database intensive operations
 *
 * This is a Template
 *
 * T: the class of the domain object being queried
 * T: the class of the result, which may be Object eg. for Object[] results
 *
 * Date Started: 27/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class EntitySpooler<T> extends PageSpooler<T> implements Spooler {

    private static final Log LOG = LogFactory.getLog(EntitySpooler.class);

    /**
     * Spool domain objects from the query specified pager
     * A query needs to be supplied.  The query will be passed to the pager to retrieve each page
     **/
    public EntitySpooler(QueryPager pager, Query query, SpoolerTask<T> spoolerTask) {
        super(new QueryPagerWrapper(pager, query), spoolerTask);
    }

    /**
     * Spool entities from the specified pager
     **/
    public EntitySpooler(QueryPager pager, SpoolerTask<T> spoolerTask) {
        super(pager, spoolerTask);
    }

    /**
     * Spool domain objects from the query specified pager
     * A query needs to be supplied.  The query will be passed to the pager to retrieve each page
     **/
    public EntitySpooler(QueryPager pager, Query query, SpoolerTask<T> spoolerTask, int maxResults) {
        super(new QueryPagerWrapper(pager, query),spoolerTask, maxResults);
    }

    /** Get the query being used by this EntitySpooler */
    public Query getQuery() {
        return ((QueryPagerWrapper) pager).getQuery();
    }

    protected void setQuery(Query query) {
        ((QueryPagerWrapper) pager).setQuery(query);
    }

}
