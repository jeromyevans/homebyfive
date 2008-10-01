package com.blueskyminds.homebyfive.framework.core.persistence.paging;

/**
 * Interface for a class that can perform paging of objects
 *
 * Date Started: 11/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface Pager {

    /**
     * Returns the specified page of objects
     *
     * @param pageNo    required page number
     * @param pageSize  number of entities per page
     * @return the requested page, if defined
     **/
    Page findPage(int pageNo, int pageSize);
}
