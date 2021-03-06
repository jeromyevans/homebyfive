package com.blueskyminds.homebyfive.framework.core.tools.filters;

import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.homebyfive.framework.core.transformer.Transformer;

import java.util.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * Tools for filtering collections
 *
 * Date Started: 2/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class FilterTools {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the FilterTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the first item found in a collection that's accepted by the filter
     *
     * Handles nulls
     *
     * @param collection
     * @param filter to accept items from the collection
     * @return the match if found, or null
     */
    public static <O extends Named> O getFirstNamed(Collection<O> collection, StringFilter filter) {
        O match = null;
        if (collection != null) {
            for (O named : collection) {
                if (filter.accept(named.getName())) {
                    match = named;
                    break;
                }
            }
        }
        return match;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the first item found in a collection with with specified name (exact match)
     *
     * Handles nulls
     *
     * @param collection
     * @param name
     * @return the match if found, or null
     */
    public static <O extends Named> O getFirstNamed(Collection<O> collection, String name) {
        return getFirstNamed(collection, new EqualsFilter(name));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the first item found in a collection with with specified name (exact match ignoring case)
     *
     * Handles nulls
     *
     * @param collection
     * @param name
     * @return the match if found, or null
     */
    public static <O extends Named> O getFirstNamedIgnoreCase(Collection<O> collection, String name) {
        return getFirstNamed(collection, new EqualsFilter(name, true));
    }

    /**
     * Get the first object from a collection that is accepted by the filter
     *
     * @param collection
     * @param filter
     * @return
     */
    public static <O> O getFirstMatching(Collection<O> collection, Filter<O> filter) {
        O match = null;
        if (collection != null) {
            for (O object : collection) {
                if (filter.accept(object)) {
                    match = object;
                    break;
                }
            }
        }
        return match;
    }

     /**
     * Get the list of objects from an iterator that are accepted by the filter
     *
     * @param iterator
     * @param filter
     * @return
     */
    public static <O> List<O> getMatching(Iterator<O> iterator, Filter<O> filter) {
        List<O> matches = new LinkedList<O>();
        O object;
        if (iterator != null) {
            while (iterator.hasNext()) {
                object = iterator.next();
                if (filter.accept(object)) {
                    matches.add(object);
                }
            }
        }
        return matches;
    }

    /**
     * Get the list of objects in a collection that are accepted by the filter
     *
     * @param collection
     * @param filter
     * @return
     */
    public static <O> List<O> getMatching(Collection<O> collection, Filter<O> filter) {
        return getMatching((collection != null ? collection.iterator() : null), filter);
    }

    /**
     * Get the list of objects in an array that are accepted by the filter
     *
     * @param array
     * @param filter
     * @return
     */
    public static <O> List<O> getMatching(O[] array, Filter<O> filter) {
        return getMatching((array != null ? Arrays.asList(array) : null), filter);        
    }

    /**
     * Get the list of transformed objects in a collection that are accepted by the filter
     *
     * Transformation is performed after the match
     *
     * @param collection
     * @param filter
     * @return
     */
    public static <T,O> List<T> getMatchingTransformed(Collection<O> collection, Filter<O> filter, Transformer<O, T> transformer) {
        if (collection != null) {
            return getMatchingTransformed(collection.iterator(), filter, transformer);
        } else {
            return new LinkedList<T>();
        }
    }

    /**
     * Get the list of transformed objects in a collection that are accepted by the filter
     *
     * Transformation is performed after the match
     *
     * @param iterator
     * @param filter
     * @return
     */
    public static <T,O> List<T> getMatchingTransformed(Iterator<O> iterator, Filter<O> filter, Transformer<O, T> transformer) {
        List<T> transformedMatches = new LinkedList<T>();
        if (iterator != null) {
            while (iterator.hasNext()) {
                O object = iterator.next();
                if (filter.accept(object)) {
                    transformedMatches.add(transformer.transform(object));
                }
            }
        }
        return transformedMatches;
    }

    /**
     * Get the list of transformed objects in a collection
     *
     * Transformation is performed on all items
     *
     * @param collection    Collection of items
     * @param transformer   Transforms the items in thecollection
     * @return a list of the transformed items
     */
    public static <T,O> List<T> getTransformed(Collection<O> collection, Transformer<O, T> transformer) {
        return getMatchingTransformed(collection, new AllPassFilter<O>(), transformer);
    }

    /**
     * Get the list of transformed objects in an array
     *
     * Transformation is performed on all items
     *
     * @param array    Collection of items
     * @param transformer   Transforms the items in the collection
     * @return a list of the transformed items
     */
    @SuppressWarnings({"unchecked"})
    public static <T,O> List<T> getTransformed(O[] array, Transformer<O, T> transformer) {
        return getMatchingTransformed(Arrays.asList(array), new AllPassFilter<O>(), transformer);
    }

    /**
     * Filters the collection removing non-null objects
     *
     * @param collection
     * @return list of non-null bojects in the collection
     */
    public static <T> List<T> getNonNull(Collection<T> collection) {
        return getMatching(collection, new NonNullFilter<T>());
    }

    /**
     * Determines whether any element in the collection is accepted by the filter
     *
     * @param collection
     * @param filter
     * @return
     */
    public static <T> boolean matchesAny(Collection<T> collection, Filter<T> filter) {
        return getFirstMatching(collection, filter) != null;
    }
}
