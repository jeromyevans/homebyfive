package com.blueskyminds.homebyfive.framework.core.persistence.spooler;

/**
 * This listener is notified of events from a PageSpooler
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public interface PageSpoolerListener {

    /** Thie method is called prior to any processing. */
    void onStart();

     /**
     * Thie method is called when all entities have been paged out
     * @param aborted true if an error occurred during processing and was processing aborted
     **/
    void onComplete(boolean aborted);

    /**     
     *  @return if true, the spooler will abort processing, otherwise it will start processing the next page
     */
    boolean onError(SpoolerException e);
}
