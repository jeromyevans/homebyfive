package com.blueskyminds.business.party.service;

import com.blueskyminds.business.party.Organisation;
import com.blueskyminds.business.party.Individual;
import com.blueskyminds.business.party.Party;
import com.blueskyminds.business.contact.PartyPOC;
import com.blueskyminds.business.contact.POCType;
import com.blueskyminds.homebyfive.framework.core.tools.selector.SingleSelector;

import java.util.Set;

/**
 * A Service for managing parties and their contact details
 *
 * Date Started: 3/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface PartyService {

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
    Organisation createOrMergeOrganisation(Organisation exampleOrganisation) throws PartyServiceException;

    /**
     * Creates a new organisation if it doesn't already exist.
     *
     * The exampleOrganisation is used to find existing candidates.  A Selector is used to select the best
     *  candidate.  If a candidate exists the properties of the exampleOrganisation are merged into the
     *  candidate.
     *
     * @param exampleOrganisation   used to find an existing organisation, or create a new one
     * @param selector              used to select the best candidate if multiple organisations match the example
     * @return Organisation         a persistent organisation (new, existing or updated)
     */
    Organisation createOrMergeOrganisation(Organisation exampleOrganisation, SingleSelector<Organisation> selector) throws PartyServiceException;

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
    Individual createOrMergeIndividual(Individual exampleIndividual) throws PartyServiceException;

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
    Individual createOrMergeIndividual(Individual exampleIndividual, SingleSelector<Individual> selector) throws PartyServiceException;

    /** Find a party with the specified tag */
    Set<Party> findPartiesByTag(String tag) throws PartyServiceException;

    /** Find an individual with the specified tag */
    Set<Party> findIndividualsByTag(String tag) throws PartyServiceException;

    /** Find an organisation with the specified tag */
    Set<Party> findOrganisationsByTag(String tag) throws PartyServiceException;

    /**
     * Finds a PointOfContact with the specified tag. The PointOfContacts are returned as PartyPOC to provide
     *  a reference to the Party and type.
     *
     * The same party may be listed twice.
     *
     * @param tag
     * @return
     * @throws PartyServiceException
     */
    Set<PartyPOC> findPointOfContactsByTag(String tag) throws PartyServiceException;

    /**
     * Finds a PointOfContact with the specified tag.  The PointOfContacts are returned as PartyPOC to provide
     *  a reference to the and type and to be consistent with the other methods of the same name.
     *
     * @param tag
     * @return set of PointOfContact's with the specified tag
     * @throws PartyServiceException
     */
    Set<PartyPOC> findPointOfContactsByTag(Party party, String tag) throws PartyServiceException;

    /**
     * Finds a PointOfContact with the specified tag and type. The PointOfContacts are returned as PartyPOC to provide
     *  a reference to the Party and type.
     *
     * The same party may be listed twice.
     *
     * @param tag
     * @return
     * @throws PartyServiceException
     */
    Set<PartyPOC> findPointOfContactsByTag(String tag, POCType type) throws PartyServiceException;

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
    void recordInteraction(PartyPOC fromParty, Set<PartyPOC> toParties, String message, String mimeType) throws PartyServiceException;
}
