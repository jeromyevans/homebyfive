package com.blueskyminds.homebyfive.framework.core.tasks;

/**
 * Status of a LongIterativeTask that works in pages
 *
 * Date Started: 06/06/2009
 */
public class PagedTaskStatus extends LongIterativeTaskStatus {

    private int pageNo;
    private int pageSize;

    public PagedTaskStatus(long iterations, boolean paused, boolean shuttingDown, int pageNo, int pageSize) {
        super(iterations, paused, shuttingDown);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
