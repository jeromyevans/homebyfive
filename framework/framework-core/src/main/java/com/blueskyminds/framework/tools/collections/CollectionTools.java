package com.blueskyminds.framework.tools.collections;

import com.blueskyminds.framework.transformer.Transformer;

import java.util.Collection;

/**
 * Extra methods for manipulating collections
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public class CollectionTools {

    /**
     * Extract values from a collection via a transformer and store them in the target array.
     * The target array must be preallocated
     *
     * @return the target array with the values set
     **/
    public static <F, T> T[] toArray(Collection<F> collection, T[] targetArray, Transformer<F, T> transformer) {
        return toArray(collection, targetArray, 0, transformer);
    }

     /**
     * Extract values from a collection via a transformer and store them in the target array.
     * The target array must be preallocated.  Entries are inserted from the initial index
     *
     * @return the target array with the values set
     **/
    public static <F, T> T[] toArray(Collection<F> collection, T[] targetArray, int initialIndex, Transformer<F, T> transformer) {

        if (collection != null) {
            int index = initialIndex;
            for (F element : collection) {
                targetArray[index++] = transformer.transform(element);
            }
        }

        return targetArray;
    }
}
