package com.blueskyminds.homebyfive.framework.framework.tools;

import com.blueskyminds.homebyfive.framework.framework.tools.collections.CollectionTools;
import com.blueskyminds.homebyfive.framework.framework.transformer.NamedTransformer;

import java.util.Collection;

/**
 * Tools for handling objects that implement Named
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public class NamedTools {

    /**
     * Returns an array containing the name of all the elements in the collection
     * The array will be empty if the collection is null or empty
     *
     * @param collection
     * @return
     */
    public static <N extends Named> String[] toArray(Collection<N> collection) {
        String[] array = null;
        if (collection != null) {
            array = new String[collection.size()];
            CollectionTools.toArray(collection, array, new NamedTransformer());
        }
        return array;
    }

     /**
     * Returns an array containing the name of all the elements in the collection as well
     *  as the extra names provided as parameters
     *
     * The extra names are included first
     * The array will be empty if the collection is null or empty
     *
     * @param collection
     * @return
     */
    public static <N extends Named> String[] toArray(Collection<N> collection, String... extraNames) {
        String[] array;
        if (collection != null) {
            array = new String[collection.size()+extraNames.length];
            System.arraycopy(extraNames, 0, array, 0, extraNames.length);
            CollectionTools.toArray(collection, array, extraNames.length, new NamedTransformer());
        } else {
            array = new String[extraNames.length];
            System.arraycopy(extraNames, 0, array, 0, extraNames.length);
        }
        return array;
    }
}
