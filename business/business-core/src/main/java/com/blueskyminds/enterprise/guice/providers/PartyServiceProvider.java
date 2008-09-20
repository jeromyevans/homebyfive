package com.blueskyminds.enterprise.guice.providers;

import com.blueskyminds.enterprise.party.service.PartyService;
import com.blueskyminds.enterprise.party.service.PartyServiceImpl;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Provides an instance of a PartyService setup with the current entity manager reference
 *
 * Date Started: 7/08/2007
 * <p/>
 * History:
 */
public class PartyServiceProvider implements Provider<PartyService> {

    private Provider<EntityManager> em;


    @Inject
    public PartyServiceProvider(Provider<EntityManager> em) {
        this.em = em;
    }

    public PartyService get() {
        return new PartyServiceImpl(em.get());
    }
}
