package com.blueskyminds.homebyfive.framework.framework.tools;

import java.lang.reflect.Array;

/**
 * Methods for managing arrays
 *
 * Date Started: 3/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class ArrayTools {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the ArrayTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns a subset of an array
     *
     * @param firstIndex
     * @param lastIndex
     * @param fullArray
     * @return an array containing a copy of the the full aray from firstIndex to lastIndex
     */
    public static <O> O[] subArray(Class<O> componentType, int firstIndex, int lastIndex, O[] fullArray) {
        int length = lastIndex-firstIndex+1;
        O[] subArray;

        if (length < 0) {
            length = 0;
        }

        subArray = (O[]) Array.newInstance(componentType, length);
        System.arraycopy(fullArray, firstIndex, subArray, 0, length);

        return subArray;
    }
}
