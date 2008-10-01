package com.blueskyminds.homebyfive.framework.core.tools.selector;

import java.util.Collection;

/**
 * Selects the first item in a List
 *
 * Date Started: 6/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class FirstSelector<T> implements SingleSelector<T> {
    
    /** Returns the first item in the collection as determined by the default iterator
     * @param collection of T
     * @return the first item in T returned by an iterator, or null if the collection is null or empty
     **/
    public T select(Collection<T> collection) {
        if (collection != null) {
            if (collection.iterator().hasNext()) {
                return collection.iterator().next();
            }
        }
        return null;
    }

}
