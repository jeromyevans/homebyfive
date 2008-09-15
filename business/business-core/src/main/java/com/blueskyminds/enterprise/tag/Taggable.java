package com.blueskyminds.enterprise.tag;

import java.util.Set;

/**
 * Identifies an entity that can have tags
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
public interface Taggable {

    Set<Tag> getTags();

    /** Add a tag to the entity.
     *
     * If the tag already exists the request is ignored
     *
     * @param tag
     */
    void addTag(Tag tag);
}
