package com.blueskyminds.homebyfive.framework.core.tools.selector;

import java.util.Collection;

/**
 * A specialised Selector that selects a single value in Collection
 *
 * Date Started: 6/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface SingleSelector<T> extends Selector<T, T> {

    /**
     * @param collection        objects to make a selection from
     * @return the single selection. NULL implies no selection
     */
    T select(Collection<T> collection);
}
