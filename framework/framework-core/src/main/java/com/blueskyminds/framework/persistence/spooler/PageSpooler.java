package com.blueskyminds.framework.persistence.spooler;

import com.blueskyminds.framework.persistence.paging.Page;
import com.blueskyminds.framework.persistence.paging.Pager;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * An Spooler that spools objects from a Pager source.
 *
 * T: the class of the object being paged
 *
 * Date Started: 27/08/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PageSpooler<T> implements Spooler {

    private static final Log LOG = LogFactory.getLog(PageSpooler.class);

    private static final int DEFAULT_PAGE_SIZE = 20;

    protected Pager pager;
    private int pageSize;
    private int maxResults;
    private SpoolerTask<T> task;
    private List<PageSpoolerListener> listeners;

    public PageSpooler(Pager pager, SpoolerTask<T> task) {
        this.pager = pager;
        maxResults = -1;
        this.task = task;
        init();
    }

    public PageSpooler(Pager pager, SpoolerTask<T> task, int maxResults) {
        this.pager = pager;
        this.maxResults = maxResults;
        this.task = task;
        init();
    }

    protected PageSpooler() {        
        init();
    }

    /**
     * Initialise the PageSpooler with default attributes
     */
    private void init() {
        pageSize = DEFAULT_PAGE_SIZE;
        listeners = new LinkedList<PageSpoolerListener>();
    }

    protected void setPager(Pager pager) {
        this.pager = pager;
    }

    protected void setTask(SpoolerTask<T> task) {
        this.task = task;
    }

    protected void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    protected int getPageSize() {
        return pageSize;
    }

    protected Pager getPager() {
        return pager;
    }

    public void addListener(PageSpoolerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PageSpoolerListener listener) {
        listeners.remove(listener);
    }

    /** Thie method is called prior to any processing. */
    private void fireOnStart() {
        for (PageSpoolerListener listener : listeners) {
            listener.onStart();
        }
    }

    /** Process the collection of domain objects that have been paged out of persistence
     * The persistence session is open */
    //protected abstract void process(List<T> queryResults) throws SpoolerException;

    /** Thie method is called when all entities have been paged out
     * @param aborted true if an error occurred during processing and was aborted */
    private void fireOnComplete(boolean aborted) {
        for (PageSpoolerListener listener : listeners) {
            listener.onComplete(aborted);
        }
    }

    /** Called when an exception occurs during processing
     *
     * @return if true, the spooler will abort processing, otherwise it will start processing the next page
     **/
    private boolean fireOnError(SpoolerException e) {
        boolean result = false;
        for (PageSpoolerListener listener : listeners) {
            result |= listener.onError(e);
        }
        return result;
    }

    /**
     * Retrieves the objects from the pager and commences processing them.
     *
     * Each page's collection of objects is passed to the abstract process method
     **/
    public void start() {
        boolean abort = false;
        int pageNo = 0;
        int resultsSoFar = 0;
        Page page;
        boolean lastPage = false;

        fireOnStart();

        while ((!lastPage) && (!abort)) {
            try {
                // load the next page
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();

                page = pager.findPage(pageNo, pageSize);

                stopWatch.split();
                long splitTime = stopWatch.getSplitTime();
                stopWatch.unsplit();

                task.process(page.getPageResults());

                stopWatch.stop();
                long stopTime = stopWatch.getTime();

                resultsSoFar += page.getPageResults().size();
                if (maxResults >= 0) {
                    // determine if max results has been reached
                    if ((!page.hasNextPage()) || (resultsSoFar >= maxResults)) {
                        lastPage = true;
                    }
                } else {
                    // determine if the last page has been reached
                    lastPage = !page.hasNextPage();
                }

                LOG.info("TotalTime: "+ DurationFormatUtils.formatDurationHMS(stopTime)+" queryTime: "+DurationFormatUtils.formatDurationHMS(splitTime));
                pageNo++;
            } catch(SpoolerException e) {
                abort = fireOnError(e);
            }
        }

        fireOnComplete(abort);
    }
}
