package com.blueskyminds.homebyfive.framework.core.tools.selector;

import java.util.Collection;

/**
 * Select zero or more items from a Collection
 *
 * Similiar to a Filter but the entire collection is available to make a selection.  The result is L.
 *
 * A null return value implies NONE are selected
 *
 * Date Started: 6/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface Selector<T,L> {

    /**
     *
     * @param collection        objects to make a selection from
     * @return the selection.  NULL implies no selection
     */
    L select(Collection<T> collection);
}
