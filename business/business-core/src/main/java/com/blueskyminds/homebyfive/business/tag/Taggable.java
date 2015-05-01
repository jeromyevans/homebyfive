package com.blueskyminds.homebyfive.business.tag;

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

    /** Remove the tag with the specific name from this entity */
    void removeTag(String tagName);


    /**
     * @param tagName
     * @return true if the entity has the specified tag
     */
    boolean hasTag(String tagName);

    /**
     * @param tag
     * @return true if the entity has the specified tag
     */
    boolean hasTag(Tag tag);

}
