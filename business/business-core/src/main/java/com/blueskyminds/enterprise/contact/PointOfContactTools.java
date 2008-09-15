package com.blueskyminds.enterprise.contact;

import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.framework.transformer.Transformer;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

/**
 * Useful methods for handling PointOfContacts
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
public class PointOfContactTools {

    /**
     * Get the values from a collection of PointOfContacts
     *
     * @param collection    collection of PointOfContacts
     * @return set of string values
     */
    public static <T extends PointOfContact> Set<String> extractValues(Collection<T> collection) {
        return new HashSet<String>(FilterTools.getTransformed(collection, new Transformer<T, String>() {

            public String transform(T pointOfContact) {
                return pointOfContact.getValue();
            }
        }));
    }

    /**
     * Get the values from a collection of PointOfContacts
     *
     * @param collection    collection of PointOfContacts
     * @return set of string values
     */
    public static <T extends POCMap> Set<String> extractValuesFromMap(Collection<T> collection) {
        return new HashSet<String>(FilterTools.getTransformed(collection, new Transformer<T, String>() {

            public String transform(T pointOfContact) {
                return pointOfContact.getPointOfContact().getValue();
            }
        }));
    }

    /**
     * Get the values from a collection of PointOfContacts
     *
     * @param collection    collection of PointOfContacts
     * @return array of string values
     */
    public static <T extends PointOfContact> String[] extractValueArray(Collection<T> collection) {
        Set<String> values = extractValues(collection);
        String[] array = new String[values.size()];
        return values.toArray(array);
    }

    /**
     * Get the values from a collection of PointOfContacts
     *
     * @param collection    collection of PointOfContacts
     * @return array of string values
     */
    public static <T extends POCMap> String[] extractValueArrayFromMap(Collection<T> collection) {
        Set<String> values = extractValuesFromMap(collection);
        String[] array = new String[values.size()];
        return values.toArray(array);
    }
}
