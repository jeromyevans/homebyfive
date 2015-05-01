package com.blueskyminds.homebyfive.business.tag.factory;

import com.blueskyminds.homebyfive.business.tag.Tag;

/**
 * A TagFactory provides instances of tags by name
 *
 * Date Started: 20/03/2009
 */
public interface TagFactory {

    Tag create(String name);
        
}
