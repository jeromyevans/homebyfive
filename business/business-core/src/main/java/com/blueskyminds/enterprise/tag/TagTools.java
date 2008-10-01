package com.blueskyminds.enterprise.tag;

import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import com.blueskyminds.homebyfive.framework.core.transformer.Transformer;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

/**
 * Helper methods for managing Tags
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
public class TagTools {

    /**
     * Get the tags from a collection of TagMaps
     *
     * @param collection    collection of TagMaps
     * @return
     */
    public static <T extends TagMap> Set<Tag> extractTags(Collection<T> collection) {
        return new HashSet<Tag>(FilterTools.getTransformed(collection, new Transformer<T, Tag>() {
            public Tag transform(TagMap tagMap) {
                return tagMap.getTag();
            }
        }));
    }

    /**
     * Determines if the collection of tag maps contains the Tag
     * @param tagMaps
     * @param tag
     * @return
     * */
    public static <T extends TagMap> boolean contains(Collection<T> tagMaps, final Tag tag) {
        return FilterTools.matchesAny(tagMaps, new Filter<T>(){
            public boolean accept(T tagMap) {
                return tagMap.getTag().equals(tag);
            }
        });
    }

    /**
     * Determines if the collection of tag maps contain the Tag specified by name
     * @param tagMaps
     * @param name
     * @return true if an exact match
     * */
    public static <T extends TagMap> boolean contains(Collection<T> tagMaps, final String name) {
        return FilterTools.matchesAny(tagMaps, new Filter<T>(){
            public boolean accept(T tagMap) {
                return tagMap.getTag().getName().equals(name);
            }
        });
    }
}
