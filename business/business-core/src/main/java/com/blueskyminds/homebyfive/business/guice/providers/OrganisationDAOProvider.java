package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.party.dao.OrganisationDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

/**
 * Date Started: 20/03/2009
 * <p/>
 * History:
 */
public class OrganisationDAOProvider implements Provider<OrganisationDAO> {

    private Provider<EntityManager> em;

    public OrganisationDAO get() {
        return new OrganisationDAO(em.get());
    }

    @Inject
    public void setEntityManager(Provider<EntityManager> em) {
        this.em = em;
    }
}