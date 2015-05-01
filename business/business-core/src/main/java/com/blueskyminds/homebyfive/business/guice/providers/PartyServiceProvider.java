package com.blueskyminds.homebyfive.business.guice.providers;

import com.blueskyminds.homebyfive.business.party.service.PartyService;
import com.blueskyminds.homebyfive.business.party.service.PartyServiceImpl;
import com.blueskyminds.homebyfive.business.party.dao.PartyDAO;
import com.blueskyminds.homebyfive.business.party.dao.OrganisationDAO;
import com.blueskyminds.homebyfive.business.party.dao.IndividualDAO;
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

    private Provider<PartyDAO> partyDAOProvider;
    private Provider<IndividualDAO> individualDAOProvider;
    private Provider<OrganisationDAO> organisationDAOProvider;

    @Inject
    public PartyServiceProvider(Provider<PartyDAO> partyDAOProvider, Provider<OrganisationDAO> organisationDAOProvider, Provider<IndividualDAO> individualDAOProvider) {
        this.partyDAOProvider = partyDAOProvider;
        this.organisationDAOProvider = organisationDAOProvider;
        this.individualDAOProvider = individualDAOProvider;
    }

    public PartyService get() {
        return new PartyServiceImpl(partyDAOProvider.get(), individualDAOProvider.get(), organisationDAOProvider.get());
    }
}
