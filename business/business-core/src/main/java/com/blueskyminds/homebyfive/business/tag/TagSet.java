package com.blueskyminds.homebyfive.business.tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Date Started: 20/03/2009
 */
public class TagSet {

    private Map<String, Tag> tags;

    public TagSet() {
        tags = new HashMap<String, Tag>();
    }

    public Tag getTag(String name) {
        Tag tag = tags.get(name);
        if (tag == null) {
            return new Tag(name);
        } else {
            return tag;
        }
    }
}
