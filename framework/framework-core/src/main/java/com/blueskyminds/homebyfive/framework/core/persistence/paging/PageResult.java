package com.blueskyminds.homebyfive.framework.core.persistence.paging;

import java.io.Serializable;
import java.util.List;

/**
 * A simple page of entities of type T
 *
 * Date Started: 12/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PageResult<T> extends AbstractPage<T> implements Page<T>, Serializable {

    private static final long serialVersionUID = -3273474835415714183L;

    /**
     * Create a new page that contains the results provided
     *
     * @param pageNo
     * @param pageSize
     * @param results
     */
    public PageResult(int pageNo, int pageSize, List<T> results) {
        super(pageNo, pageSize);
        this.results = results;
    }

}
