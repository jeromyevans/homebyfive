package com.blueskyminds.homebyfive.framework.core.persistence.paging;

import java.util.List;

/**
 * A pagination interface to the persistance layer.
 * <p/>
 * Date Started: 27/08/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface Page<T> {

    /**
     * Returns true if theres another page after this one
     */
    boolean hasNextPage();

    /**
     * Returns true if there's a page before this one
     */
    boolean hasPreviousPage();

    /**
     * Get the list of results contained in this page<p/>
     * Use this list to determine how many results are actually contained in this page.
     */
    List<T> getPageResults();

    /**
     * Get the page number of this page
     */
    int getPageNo();

    /**
     * Get the page size - the number of records per page
     * <p/>
     * Note this may be greater than the ACUTAL number of records on the page
     */
    int getPageSize();

    /**
     * Duplicate the page.
     */
    Page asCopy();
}
