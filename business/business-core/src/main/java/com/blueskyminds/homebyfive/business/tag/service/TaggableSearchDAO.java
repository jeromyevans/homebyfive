package com.blueskyminds.homebyfive.business.tag.service;

import com.blueskyminds.homebyfive.business.tag.Tag;

import java.util.Set;
import java.util.Collection;

/**
 * Date Started: 20/03/2009
 */
public interface TaggableSearchDAO<T> {

    /** List Taggables that have any one of the specified tags.  If tags is empty, all Taggables are matched */
    Collection<T> listByTags(Set<Tag> tags);
}
