package com.blueskyminds.enterprise.guice.providers;

import com.blueskyminds.enterprise.party.service.PartyService;
import com.blueskyminds.enterprise.party.service.PartyServiceImpl;
import com.blueskyminds.enterprise.tag.service.TagService;
import com.blueskyminds.enterprise.tag.service.TagServiceImpl;
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

    private Provider<EntityManager> em;

    @Inject
    public TagServiceProvider(Provider<EntityManager> em) {
        this.em = em;
    }

    public TagService get() {
        return new TagServiceImpl(em.get());
    }
}
