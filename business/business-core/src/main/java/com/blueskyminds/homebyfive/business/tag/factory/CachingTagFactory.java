package com.blueskyminds.homebyfive.business.tag.factory;

import com.blueskyminds.homebyfive.business.tag.Tag;

import java.util.HashMap;

/**
 * Stores retrieved tags in a cache for faster lookup
 *
 * Date Started: 20/03/2009
 */
public class CachingTagFactory implements TagFactory {

    private TagFactory tagFactory;
    private HashMap<String, Tag> cache;

    public CachingTagFactory(TagFactory tagFactory) {
        this.tagFactory = tagFactory;
        cache = new HashMap<String, Tag>();
    }

    public Tag create(String name) {
        Tag tag = cache.get(name);
        if (tag == null) {
            tag = tagFactory.create(name);
            if (tag != null) {
                cache.put(name, tag);
            }
        }
        return tag;
    }
}
