package com.blueskyminds.enterprise.tag;

/**
 * Maps a tag to an entity
 *
 * Date Started: 5/08/2007
 * <p/>
 * History:
 */
public interface TagMap {

    Taggable getTarget();
    Tag getTag();
}
