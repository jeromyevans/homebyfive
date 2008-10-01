package com.blueskyminds.homebyfive.framework.core.tools.selector;

import java.util.List;
import java.util.Collection;

/**
 * A specialised selector that selects a List of T from a collection of T
 *
 * Date Started: 6/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface ListSelector<T> extends Selector<T, List<T>> {

    /**
     * @param collection        objects to make a selection from
     * @return the selection.  NULL or an empty list imply no selection
     */
    List<T> select(Collection<T> collection);
}
