package com.blueskyminds.enterprise.party.service;

import com.blueskyminds.enterprise.party.*;
import com.blueskyminds.enterprise.party.dao.PartyDAO;
import com.blueskyminds.enterprise.contact.PartyPOC;
import com.blueskyminds.enterprise.contact.POCType;
import com.blueskyminds.enterprise.contact.interaction.POCInteraction;
import com.blueskyminds.homebyfive.framework.core.tools.selector.SingleSelector;
import com.blueskyminds.homebyfive.framework.core.tools.selector.FirstSelector;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * The default implementation of the PartyService
 *
 * Date Started: 4/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PartyServiceImpl implements PartyService {

    private EntityManager em;

    public PartyServiceImpl(EntityManager entityManager) {
        this.em = entityManager;
    }

    public PartyServiceImpl() {
    }

    /**
     * Create a new Party instance of the subclass appropriate for type
     *
     * The instance is not persistent
     *
     * @param name
     * @param type
     * @return
     * */
    public Party createPartyInstance(PartyTypes type, String name) {
        Party party = null;

        switch (type) {
            case Company:
                party = new Company(name);
                break;
            case Individual:
                party = new Individual(name);
                break;
            case Organisation:
                party = new Organisation(PartyTypes.Organisation, name);
                break;
            case System:
                party = new SystemParty(name);
                break;
        }

        return party;
    }

    /**
     * Creates a new organisation if it doesn't already exist.
     *
     * The exampleOrganisation is used to find existing candidates.  A Selector is used to select the best
     *  candidate.  If a candidate exists the properties of the exampleOrganisation are merged into the
     *  candidate.
     *
     * @param exampleOrganisation used to find an existing organisation, or create a new one
     * @return Organisation a persistent organisation (new, existing or updated)
     */
    public Organisation createOrMergeOrganisation(Organisation exampleOrganisation) throws PartyServiceException {
        return createOrMergeOrganisation(exampleOrganisation, null);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a new organisation if it doesn't already exist.
     *
     * The exampleOrganisation is used to find existing candidates.  A Selector is used to select the best
     *  candidate.  If a candidate exists the properties of the exampleOrganisation are merged into the
     *  candidate.
     *
     * @param exampleOrganisation used to find an existing organisation, or create a new one
     * @return a persistent organisation (new, existing or updated)
     */
    public Organisation createOrMergeOrganisation(Organisation exampleOrganisation, SingleSelector<Organisation> selector) throws PartyServiceException {
        Organisation persistentOrganisation = null;

        if (selector == null) {
            selector = new FirstSelector<Organisation>();
        }

        Collection<Organisation> candidateOrgs = new PartyDAO(em).findByExample(exampleOrganisation);

        if (candidateOrgs.size() > 0) {
            persistentOrganisation = selector.select(candidateOrgs);
        }

        if (persistentOrganisation != null) {
            // merge the properties of the example into the match
            persistentOrganisation.mergeWith(exampleOrganisation);
        } else {
            // create a new one
            persistentOrganisation = exampleOrganisation;
        }

        // commit the changes
        em.persist(persistentOrganisation);

        return persistentOrganisation;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a new individual if it doesn't already exist.
     *
     * The exampleIndividual is used to find existing candidates.  A Selector is used to select the best
     *  candidate.  If a candidate exists the properties of the exampleIndividual are merged into the
     *  candidate.
     *
     * @param exampleIndividual used to find an existing individual, or create a new one
     * @return a persistent individual (new, existing or updated)
     */
    public Individual createOrMergeIndividual(Individual exampleIndividual) throws PartyServiceException {
        return createOrMergeIndividual(exampleIndividual, null);
    }

    /**
     * Creates a new individual if it doesn't already exist.
     *
     * The exampleIndividual is used to find existing candidates.  A Selector is used to select the best
     *  candidate.  If a candidate exists the properties of the exampleIndividual are merged into the
     *  candidate.
     *
     * Individuals are found by:
     *    email address (if defined); or
     *    name and address (if defined)
     *
     * @param exampleIndividual used to find an existing individual, or create a new one
     * @return a persistent individual (new, existing or updated)
     */
    public Individual createOrMergeIndividual(Individual exampleIndividual, SingleSelector<Individual> selector) throws PartyServiceException {
        Individual persistentIndividual = null;


        if (selector == null) {
            selector = new FirstSelector<Individual>();
        }
        Collection<Individual> candidateIndividuals = new PartyDAO(em).findByExample(exampleIndividual);

        if (candidateIndividuals.size() > 0) {
            persistentIndividual = selector.select(candidateIndividuals);
        }

        if (persistentIndividual != null) {
            // merge the properties of the example into the match
            persistentIndividual.mergeWith(exampleIndividual);
        } else {
            // create a new one
            persistentIndividual = exampleIndividual;
        }

        // commit the changes
        em.persist(persistentIndividual);

        return persistentIndividual;
    }

    /**
     * Find a party with the specified tag
     **/    
    public Set<Party> findPartiesByTag(String tag) throws PartyServiceException {
        return new HashSet<Party>(new PartyDAO(em).findPartiesByTag(tag));
    }

    /**
     * Find an organisation with the specified tag
     */
    public Set<Party> findOrganisationsByTag(String tag) throws PartyServiceException {
        return new HashSet<Party>(new PartyDAO(em).findOrganisationsByTag(tag));
    }

    /**
     * Find an individual with the specified tag
     */
    public Set<Party> findIndividualsByTag(String tag) throws PartyServiceException {
        return new HashSet<Party>(new PartyDAO(em).findIndividualsByTag(tag));
    }

    public Set<PartyPOC> findPointOfContactsByTag(String tag) throws PartyServiceException {
        return new HashSet<PartyPOC>(new PartyDAO(em).findPointsOfContactByTag(tag));
    }

    public Set<PartyPOC> findPointOfContactsByTag(Party party, String tag) throws PartyServiceException {
        return new HashSet<PartyPOC>(new PartyDAO(em).findPointsOfContactByTag(party, tag));
    }


    /**
     * Finds a PointOfContact with the specified tag and type. The PointOfContacts are returned as PartyPOC to provide
     * a reference to the Party and type.
     * <p/>
     * The same party may be listed twice.
     *
     * @param tag
     * @return
     * @throws com.blueskyminds.enterprise.party.service.PartyServiceException
     *
     */
    public Set<PartyPOC> findPointOfContactsByTag(String tag, POCType type) throws PartyServiceException {
        return new HashSet<PartyPOC>(new PartyDAO(em).findPointsOfContactByTag(tag, type));
    }

    /**
     * Record an interaction between two parties.
     * The interaction identifies the type of communication, timestamp and message
     *
     * @param fromParty
     * @param toParties
     * @param message
     * @param mimeType
     * @throws PartyServiceException
     */
    public void recordInteraction(PartyPOC fromParty, Set<PartyPOC> toParties, String message, String mimeType) throws PartyServiceException {
        POCInteraction pocInteraction = new POCInteraction(fromParty, toParties, message, mimeType);
        em.persist(pocInteraction);
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

}
