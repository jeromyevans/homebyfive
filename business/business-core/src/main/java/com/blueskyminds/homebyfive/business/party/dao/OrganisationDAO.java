package com.blueskyminds.homebyfive.business.party.dao;

import com.blueskyminds.homebyfive.business.party.Organisation;
import com.blueskyminds.homebyfive.business.party.Individual;
import com.blueskyminds.homebyfive.business.tag.service.TaggableSearchDAO;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.contact.PointOfContact;
import com.blueskyminds.homebyfive.business.contact.POCType;
import com.blueskyminds.homebyfive.business.contact.PointOfContactTools;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Set;

/**
 * Methods to access Organisations
 *
 * Date Started: 20/03/2009 from PartyDAO
 */
public class OrganisationDAO extends AbstractDAO<Organisation> implements TaggableSearchDAO<Organisation> {

    private static final String QUERY_ORGANISATION_BY_NAME = "organisation.byName";
    private static final String PARAM_NAME = "name";
    private static final String QUERY_ORGANISATION_BY_NAME_AND_ADDRESSES = "organisation.byNameAndAddresses";
    private static final String QUERY_ORGANISATION_BY_EMAIL_ADDRESSES = "organisation.byEmailAddress";
    private static final String QUERY_ORGANISATION_BY_TAG = "organisation.byTag";

    private static final String QUERY_ALL_ORGANISATIONS = "organisation.all";
    private static final String QUERY_ORGANISATION_BY_TAGS = "organisation.byTags";


    public OrganisationDAO(EntityManager em) {
        super(em, Organisation.class);
    }

    /**
     * Finds all Organisations with name like Name
     *
     * @param name of the organsiation
     * @return list of matches
     * @throws com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException
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
     * @throws com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException
     */
    public Collection<Organisation> findByExample(Organisation organisation) {
        Collection<Organisation> organisations;
        Query query = em.createNamedQuery(QUERY_ORGANISATION_BY_NAME_AND_ADDRESSES);

        if (!PartyDAOSupport.setAddressesParameter(organisation, query)) {
            // switch to a different query because there's no addresses in the example
            query = em.createNamedQuery(QUERY_ORGANISATION_BY_NAME);
        }

        //setLikeParameter(query, PARAM_NAME, organisation.getName());
        query.setParameter(PARAM_NAME, organisation.getName());

        organisations = query.getResultList();

        return organisations;
    }

    // ------------------------------------------------------------------------------------------------------



    /** Find individual(s) matching the given email address */
    public Collection<Organisation> findByEmailAddress(Organisation organisation) {
        Collection<Organisation> organisations;

        Query query = em.createNamedQuery(QUERY_ORGANISATION_BY_EMAIL_ADDRESSES);
        Set<PointOfContact> pocs = organisation.getPointsOfContactOfType(POCType.EmailAddress);
        query.setParameter(PartyDAOSupport.PARAM_EMAIL, PointOfContactTools.extractValues(pocs));

        organisations = query.getResultList();

        return organisations;
    }

    public Collection<Organisation> findByTag(String tag) {
        Collection<Organisation> organisations;

        Query query = em.createNamedQuery(QUERY_ORGANISATION_BY_TAG);
        query.setParameter(PartyDAOSupport.PARAM_TAG_NAME, tag);

        organisations = query.getResultList();

        return organisations;
    }

     /**
     * List all Individual.  Checks status
     * @return
     */
    public Collection<Organisation> listAll() {
        Collection<Organisation> organisations;
        Query query = em.createNamedQuery(QUERY_ALL_ORGANISATIONS);
        organisations = query.getResultList();
        return organisations;
    }

    /**
     *
     * @param tags   if non-empty, lists parties with any of these tags.  If empty, list all parties
     * @return
     */
    public Collection<Organisation> listByTags(Set<Tag> tags) {
        Collection<Organisation> organisations;

        if (tags.size() > 0) {
            Query query = em.createNamedQuery(QUERY_ORGANISATION_BY_TAGS);
            query.setParameter(PartyDAOSupport.PARAM_TAGS, tags);

            organisations = query.getResultList();
        } else {
            organisations = listAll();
        }

        return organisations;
    }


    public void persist(Organisation organisation) {
        em.persist(organisation);
    }

}
