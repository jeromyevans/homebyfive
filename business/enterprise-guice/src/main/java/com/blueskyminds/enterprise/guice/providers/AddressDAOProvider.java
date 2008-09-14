package com.blueskyminds.enterprise.guice.providers;

import com.blueskyminds.enterprise.address.dao.AddressDAO;
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
