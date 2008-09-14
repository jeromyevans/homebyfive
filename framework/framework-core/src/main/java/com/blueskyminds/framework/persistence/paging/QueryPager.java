package com.blueskyminds.framework.persistence.paging;

import javax.persistence.Query;

/**
 * Performs queries one page at a time
 *
 * Date Started: 11/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface QueryPager extends Pager {

     /**
     * Search for entities that match the given query, returning the requested page
     *
     * @param query     query to execute
     * @param pageNo    required page number
     * @param pageSize  number of entities per page
     * @return the requested page, if defined
     */
    Page findPage(Query query, int pageNo, int pageSize);
}
