package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.business.tag.factory.TagFactory;
import com.blueskyminds.homebyfive.business.tag.factory.CachingTagFactory;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.google.inject.Provider;
import com.google.inject.Inject;

/**
 * Provides the TagService implementation of the TagFactory wrapped by a tag cache
 *
 * Date Started: 21/03/2009
 */
public class CachingTagFactoryProvider implements Provider<TagFactory> {

    private TagService tagService;

    public TagFactory get() {
        return new CachingTagFactory(tagService);
    }

    @Inject
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }
    
}
