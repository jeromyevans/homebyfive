package com.blueskyminds.enterprise.tag.service;

import com.blueskyminds.enterprise.tag.Tag;

import java.util.Set;

/**
 * The Tag Service is used to access tags
 *
 * Tags are just simple words used to classify and group entities
 *
 * Date Started: 6/08/2007
 * <p/>
 * History:
 */
public interface TagService {

    public static final String SYSTEM_CONTACT = "system:contact";
    
    /** Lookup the tag with the specified name, or create it if it doesn't exist
     * @return*/
    Tag lookupOrCreateTag(String name);

    /**
     * Lookup the tag with the specified key
     * @param key
     * @return
     */
    Tag lookupTag(String key);

    /** List the full set of tags */
    Set<Tag> listTags();

}
