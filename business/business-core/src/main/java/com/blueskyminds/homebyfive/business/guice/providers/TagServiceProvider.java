package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.tag.service.TagServiceImpl;
import com.blueskyminds.homebyfive.business.tag.dao.TagDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Provides an instance of a TagService setup with the current entity manager reference
 *
 * Date Started: 7/08/2007
 * <p/>
 * History:
 */
public class TagServiceProvider implements Provider<TagService> {

    private Provider<TagDAO> tagDAO;

    @Inject
    public TagServiceProvider(Provider<TagDAO> tagDAO) {
        this.tagDAO = tagDAO;
    }

    public TagService get() {
        return new TagServiceImpl(tagDAO.get());
    }
}
