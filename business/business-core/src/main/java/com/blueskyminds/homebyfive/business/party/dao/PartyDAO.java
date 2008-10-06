package com.blueskyminds.homebyfive.business.party.dao;

import com.blueskyminds.homebyfive.business.party.*;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.homebyfive.business.contact.*;
import com.blueskyminds.homebyfive.business.address.Address;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * Methods that access Party data
 *
 * Date Started: 4/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PartyDAO extends AbstractDAO<Party> {
    
    private static final String QUERY_ORGANISATION_BY_NAME = "organisation.byName";
    private static final String PARAM_NAME = "name";
    private static final String QUERY_ORGANISATION_BY_NAME_AND_ADDRESSES = "organisation.byNameAndAddresses";
    private static final String PARAM_ADDRESSES = "addresses";
    private static final String PARAM_TYPE = "type";
    private static final String QUERY_INDIVIDUAL_BY_NAME  = "individual.byName";
    private static final String QUERY_INDIVIDUAL_BY_NAME_AND_ADDRESSES = "individual.byNameAndAddresses";
    private static final String PARAM_FIRST_NAME = "firstName";
    private static final String PARAM_LAST_NAME = "lastName";
    private static final String QUERY_INDIVIDUAL_BY_EMAIL_ADDRESSES = "individual.byEmailAddress";
    private static final String QUERY_ORGANISATION_BY_EMAIL_ADDRESSES = "organisation.byEmailAddress";
    private static final String PARAM_EMAIL = "email";
    private static final String QUERY_PARTY_BY_TAG = "party.byTag";
    private static final String QUERY_INDIVIDUAL_BY_TAG = "individual.byTag";
    private static final String QUERY_ORGANISATION_BY_TAG = "organisation.byTag";
    private static final String PARAM_TAG_NAME = "name";
    private static final String QUERY_PARTY_POC_BY_TAG = "partyPOC.byTag";
    private static final String QUERY_PARTY_POC_BY_TAG_AND_PARTY = "partyPOC.byTagAndParty";
    private static final String PARAM_PARTY = "party";
    private static final String QUERY_PARTY_POC_BY_TAG_AND_TYPE = "partyPOC.byTagAndType";

    public PartyDAO(EntityManager em) {
        super(em, Party.class);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PartyDAO with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Finds all Organisations with name like Name
     *
     * @param name of the organsiation
     * @return list of matches
     * @throws PersistenceServiceException
     */
    public Collection<Organisation> lookupOrganisationByName(String name) {
        Collection<Organisation> organisations;

        Query query = em.createNamedQuery(QUERY_ORGANISATION_BY_NAME);
        query.setParameter(PARAM_NAME, name);
        organisations = query.getResultList();

        return organisations;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup the best-matching organisation based on the available attributes
     *
     * @param organisation with attributes of interest (by example) nulls ignored
     * @return list of matches
     * @throws PersistenceServiceException
     */
    public Collection<Organisation> findByExample(Organisation organisation) {
        Collection<Organisation> organisations;
        Query query = em.createNamedQuery(QUERY_ORGANISATION_BY_NAME_AND_ADDRESSES);

        if (!setAddressesParameter(organisation, query)) {
            // switch to a different query because there's no addresses in the example
            query = em.createNamedQuery(QUERY_ORGANISATION_BY_NAME);
        }

        setLikeParameter(query, PARAM_NAME, organisation.getName());

        organisations = query.getResultList();

        return organisations;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Adds the addresses of a Party as a paramaeter to a query if an address is defined
     * If the party has multiple addresses then all addresses are considered
     *
     * @param party
     * @param query
     * @return true if one or more addresses were added
     */
    private boolean setAddressesParameter(Party party, Query query) {
        List<Address> addresses = new LinkedList<Address>();
        for (ContactAddress address : party.getAddresses()) {
            if (address.isPersistent()) {
                addresses.add(address.getAddress());
            }
        }
        if (addresses.size() > 0) {
            query.setParameter(PARAM_TYPE, POCType.Address);

            query.setParameter(PARAM_ADDRESSES, addresses);
            return true;
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup the best-matching individual based on the available attributes
     *
     * @param individual with attributes of interest (by example) nulls ignored
     * @return list of matches
     * @throws PersistenceServiceException
     */
    public Collection<Individual> findByExample(Individual individual) {
        Collection<Individual> individuals;

        // first try the email address
        if (individual.hasPointOfContactOfType(POCType.EmailAddress)) {
            individuals = findByEmailAddress(individual);
        } else {
            // otherwise use name and/or address
            Query query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_NAME_AND_ADDRESSES);

            if (!setAddressesParameter(individual, query)) {
                // switch to a different query because there's no addresses in the example
                query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_NAME);
            }

            setLikeParameter(query, PARAM_FIRST_NAME, individual.getFirstName());
            setLikeParameter(query, PARAM_LAST_NAME, individual.getLastName());

            individuals = query.getResultList();
        }

        return individuals;
    }

    /** Find individual(s) matching the given email address(es) in the example */
    public Collection<Individual> findByEmailAddress(Individual individual) {
        Collection<Individual> individuals;

        Query query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_EMAIL_ADDRESSES);
        Set<PointOfContact> pocs = individual.getPointsOfContactOfType(POCType.EmailAddress);
        query.setParameter(PARAM_EMAIL, PointOfContactTools.extractValues(pocs));

        individuals = query.getResultList();

        return individuals;
    }

    /** Find individual(s) matching the given email address */
    public Collection<Organisation> findByEmailAddress(Organisation organisation) {
        Collection<Organisation> organisations;

        Query query = em.createNamedQuery(QUERY_ORGANISATION_BY_EMAIL_ADDRESSES);
        Set<PointOfContact> pocs = organisation.getPointsOfContactOfType(POCType.EmailAddress);         
        query.setParameter(PARAM_EMAIL, PointOfContactTools.extractValues(pocs));

        organisations = query.getResultList();

        return organisations;
    }

    public Collection<Party> findPartiesByTag(String tag) {
        Collection<Party> organisations;

        Query query = em.createNamedQuery(QUERY_PARTY_BY_TAG);
        query.setParameter(PARAM_TAG_NAME, tag);

        organisations = query.getResultList();

        return organisations;
    }

    public Collection<Individual> findIndividualsByTag(String tag) {
        Collection<Individual> individuals;

        Query query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_TAG);
        query.setParameter(PARAM_TAG_NAME, tag);

        individuals = query.getResultList();

        return individuals;
    }

    public Collection<Organisation> findOrganisationsByTag(String tag) {
        Collection<Organisation> organisations;

        Query query = em.createNamedQuery(QUERY_ORGANISATION_BY_TAG);
        query.setParameter(PARAM_TAG_NAME, tag);

        organisations = query.getResultList();

        return organisations;
    }

    public Collection<PartyPOC> findPointsOfContactByTag(String tag) {
        Collection<PartyPOC> partyPOCs;

        Query query = em.createNamedQuery(QUERY_PARTY_POC_BY_TAG);
        query.setParameter(PARAM_TAG_NAME, tag);

        partyPOCs = query.getResultList();

        return partyPOCs;
    }

    public Collection<PartyPOC> findPointsOfContactByTag(Party party, String tag) {
        Collection<PartyPOC> partyPOCs;

        Query query = em.createNamedQuery(QUERY_PARTY_POC_BY_TAG_AND_PARTY);
        query.setParameter(PARAM_TAG_NAME, tag);
        query.setParameter(PARAM_PARTY, party);

        partyPOCs = query.getResultList();

        return partyPOCs;
    }

    public Collection<PartyPOC> findPointsOfContactByTag(String tag, POCType type) {
        Collection<PartyPOC> partyPOCs;

        Query query = em.createNamedQuery(QUERY_PARTY_POC_BY_TAG_AND_TYPE);
        query.setParameter(PARAM_TAG_NAME, tag);
        query.setParameter(PARAM_TYPE, type);

        partyPOCs = query.getResultList();

        return partyPOCs;
    }
}
