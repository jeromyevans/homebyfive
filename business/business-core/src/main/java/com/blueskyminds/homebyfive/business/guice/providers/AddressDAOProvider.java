package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
public class AddressDAOProvider implements Provider<AddressDAO> {

    private Provider<EntityManager> em;

    public AddressDAO get() {
        return new AddressDAO(em.get());
    }

    @Inject
    public void setEntityManager(Provider<EntityManager> em) {
        this.em = em;
    }
}
