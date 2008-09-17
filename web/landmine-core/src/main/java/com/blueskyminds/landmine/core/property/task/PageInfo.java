package com.blueskyminds.landmine.core.property.task;

/**
 * Date Started: 25/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class PageInfo extends Object {

    private int pageNo;
    private int pageSize;

    public PageInfo(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }
}
