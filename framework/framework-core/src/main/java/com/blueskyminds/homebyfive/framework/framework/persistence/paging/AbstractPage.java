package com.blueskyminds.homebyfive.framework.framework.persistence.paging;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Abstract superclass for a Page of results.
 * <p/>
 * The most signicant aspect of this Page implementation is that the list of results should contain
 * pageSize+1 elements if there's another.
 * If a Query is implemented to request a page it should request pageSize+1 entities even though
 * the last entitty will not be accessible.
 * <p/>
 * Date Started: 12/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AbstractPage<T> implements Page<T>, Serializable {

    private static final long serialVersionUID = -2580933138488917224L;

    protected int pageSize;
    protected int pageNo;
    protected List<T> results;

    public AbstractPage(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractPage with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if there's another pageNo after this one
     */
    public boolean hasNextPage() {
        return results.size() > pageSize;
    }

    /**
     * Returns true if there's a pageNo before this one
     */
    public boolean hasPreviousPage() {
        return pageNo > 0;
    }

    /**
     * Get the list of domain objects contained in this pageNo
     * <p/>
     * Note we get a sublit of pageSize-1 (0 to PageSize exclusive) elements because the results contain
     * one extra result to detect the next page
     */
    public List<T> getPageResults() {
        return hasNextPage() ?
                results.subList(0, pageSize) :
                results;
    }

    /**
     * Get the page number of this page
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * Get the page size - the number of records per page
     * Note this may be greater than the actual number of records on the page
     */
    public int getPageSize() {
        return pageSize;
    }


    /**
     * Duplicate the page.
     * Creates a new PageResult copied from this page
     */
    public Page asCopy() {
        List<T> copiedResults = new ArrayList<T>(results);
        PageResult copy = new PageResult(pageNo, pageSize, copiedResults);
        return copy;
    }
}
