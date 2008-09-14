package com.blueskyminds.framework.tools.filters;

/**
 * Accepts the object if its not null
 *
 * Date Started: 13/10/2007
 * <p/>
 * History:
 */
public class NonNullFilter<T> implements Filter<T> {

    /**
     * Accepts the object if its not null
     *
     * @param object object to check
     * @return true if the object is accepted by the filter
     */
    public boolean accept(T object) {
        return object != null;
    }
}
