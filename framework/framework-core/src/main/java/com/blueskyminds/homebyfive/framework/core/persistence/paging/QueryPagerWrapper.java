package com.blueskyminds.homebyfive.framework.core.persistence.paging;

import javax.persistence.Query;

/**
 * Holds a reference to a Query that will be passed to QueryPager when requested
 *
 * Date Started: 11/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class QueryPagerWrapper implements Pager {

    private QueryPager pager;
    private Query query;

    public QueryPagerWrapper(QueryPager pager, Query query) {
        this.pager = pager;
        this.query = query;
    }

    public Page findPage(int pageNo, int pageSize) {
        return pager.findPage(query, pageNo, pageSize);
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
