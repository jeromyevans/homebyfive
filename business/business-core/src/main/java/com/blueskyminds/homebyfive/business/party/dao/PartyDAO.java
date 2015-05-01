package com.blueskyminds.homebyfive.business.party.dao;

import com.blueskyminds.homebyfive.business.party.*;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.homebyfive.business.contact.*;
import com.blueskyminds.homebyfive.business.contact.interaction.POCInteraction;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.service.TaggableSearchDAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Set;

/**
 * Methods that access Party data (Individuals or Organisations)
 *
 * Date Started: 4/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd<br/>
 */
public class PartyDAO extends AbstractDAO<Party> implements TaggableSearchDAO<Party> {

    private static final String QUERY_PARTY_BY_TAG = "party.byTag";
    private static final String QUERY_PARTY_POC_BY_TAG = "partyPOC.byTag";
    private static final String QUERY_PARTY_POC_BY_TAG_AND_PARTY = "partyPOC.byTagAndParty";
    private static final String PARAM_PARTY = "party";
    private static final String QUERY_PARTY_POC_BY_TAG_AND_TYPE = "partyPOC.byTagAndType";

    private static final String QUERY_ALL_PARTIES = "party.all";
    private static final String QUERY_PARTY_BY_TAGS = "party.byTags";

    public PartyDAO(EntityManager em) {
        super(em, Party.class);
    }


    public Collection<Party> findByTag(String tag) {
        Collection<Party> organisations;

        Query query = em.createNamedQuery(QUERY_PARTY_BY_TAG);
        query.setParameter(PartyDAOSupport.PARAM_TAG_NAME, tag);

        organisations = query.getResultList();

        return organisations;
    }

    /**
     * List all Parties.  Checks status
     * @return
     */
    public Collection<Party> listAll() {
        Collection<Party> organisations;
        Query query = em.createNamedQuery(QUERY_ALL_PARTIES);
        organisations = query.getResultList();
        return organisations;
    }

    /**
     *
     * @param tags   if non-empty, lists parties with any of these tags.  If empty, list all parties
     * @return
     */
    public Collection<Party> listByTags(Set<Tag> tags) {
        Collection<Party> organisations;

        if (tags.size() > 0) {
            Query query = em.createNamedQuery(QUERY_PARTY_BY_TAGS);
            query.setParameter(PartyDAOSupport.PARAM_TAGS, tags);

            organisations = query.getResultList();
        } else {
            organisations = listAll();
        }

        return organisations;
    }   


    public Collection<PartyPOC> findPointsOfContactByTag(String tag) {
        Collection<PartyPOC> partyPOCs;

        Query query = em.createNamedQuery(QUERY_PARTY_POC_BY_TAG);
        query.setParameter(PartyDAOSupport.PARAM_TAG_NAME, tag);

        partyPOCs = query.getResultList();

        return partyPOCs;
    }

    public Collection<PartyPOC> findPointsOfContactByTag(Party party, String tag) {
        Collection<PartyPOC> partyPOCs;

        Query query = em.createNamedQuery(QUERY_PARTY_POC_BY_TAG_AND_PARTY);
        query.setParameter(PartyDAOSupport.PARAM_TAG_NAME, tag);
        query.setParameter(PARAM_PARTY, party);

        partyPOCs = query.getResultList();

        return partyPOCs;
    }

    public Collection<PartyPOC> findPointsOfContactByTag(String tag, POCType type) {
        Collection<PartyPOC> partyPOCs;

        Query query = em.createNamedQuery(QUERY_PARTY_POC_BY_TAG_AND_TYPE);
        query.setParameter(PartyDAOSupport.PARAM_TAG_NAME, tag);
        query.setParameter(PartyDAOSupport.PARAM_TYPE, type);

        partyPOCs = query.getResultList();

        return partyPOCs;
    }

    public void persist(POCInteraction pocInteraction) {
        em.persist(pocInteraction);
    }
}
