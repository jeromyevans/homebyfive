package com.blueskyminds.homebyfive.business.tag.factory;

import com.blueskyminds.homebyfive.business.tag.Tag;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Obtains tags from an in-memory pool if available.
 * If not available from the pool, a new instance is created
 *
 * Date Started: 20/03/2009
 */
public class InMemoryTagFactory implements TagFactory {

    private Map<String, Tag> tags;

    /** Track the tags created by this factory */
    public Set<Tag> created;

    public InMemoryTagFactory() {
        init();
    }

    private void init() {
        tags = new HashMap<String, Tag>();
        created = new HashSet<Tag>();
    }

    public InMemoryTagFactory(Set<Tag> predefinedTags) {
        init();
        for (Tag tag : predefinedTags) {
            tags.put(tag.getName(), tag);
        }
    }

    public Tag create(String name) {
        Tag tag = tags.get(name);
        if (tag == null) {
            tag = new Tag(name);
            tags.put(name, tag);
            created.add(tag);
        }
        return tag;

    }

    public Set<Tag> getCreated() {
        return created;
    }
}
