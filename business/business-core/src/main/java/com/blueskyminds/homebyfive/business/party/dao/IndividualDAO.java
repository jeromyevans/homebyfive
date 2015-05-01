package com.blueskyminds.homebyfive.business.party.dao;

import com.blueskyminds.homebyfive.business.party.Individual;
import com.blueskyminds.homebyfive.business.party.Party;
import com.blueskyminds.homebyfive.business.tag.service.TaggableSearchDAO;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.contact.POCType;
import com.blueskyminds.homebyfive.business.contact.PointOfContact;
import com.blueskyminds.homebyfive.business.contact.PointOfContactTools;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Set;

/**
 * Methods to access Individual data
 *
 * Date Started: 20/03/2009 from PartyDAO
 * 
 */
public class IndividualDAO extends AbstractDAO<Individual> implements TaggableSearchDAO<Individual> {

    private static final String QUERY_INDIVIDUAL_BY_NAME  = "individual.byName";
    private static final String QUERY_INDIVIDUAL_BY_NAME_AND_ADDRESSES = "individual.byNameAndAddresses";
    private static final String PARAM_FIRST_NAME = "firstName";
    private static final String PARAM_LAST_NAME = "lastName";
    private static final String QUERY_INDIVIDUAL_BY_EMAIL_ADDRESSES = "individual.byEmailAddress";
    private static final String QUERY_INDIVIDUAL_BY_TAG = "individual.byTag";

    private static final String QUERY_ALL_INDIVIDUALS = "individual.all";
    private static final String QUERY_INDIVIDUAL_BY_TAGS = "individual.byTags";

    public IndividualDAO(EntityManager em) {
        super(em, Individual.class);
    }

    /**
     * Lookup the best-matching individual based on the available attributes
     *
     * @param individual with attributes of interest (by example) nulls ignored
     * @return list of matches
     */
    public Collection<Individual> findByExample(Individual individual) {
        Collection<Individual> individuals;

        // first try the email address
        if (individual.hasPointOfContactOfType(POCType.EmailAddress)) {
            individuals = findByEmailAddress(individual);
        } else {
            // otherwise use name and/or address
            Query query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_NAME_AND_ADDRESSES);

            if (!PartyDAOSupport.setAddressesParameter(individual, query)) {
                // switch to a different query because there's no addresses in the example
                query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_NAME);
            }

            //setLikeParameter(query, PARAM_FIRST_NAME, individual.getFirstName());
            //setLikeParameter(query, PARAM_LAST_NAME, individual.getLastName());
            query.setParameter(PARAM_FIRST_NAME, individual.getFirstName());
            query.setParameter(PARAM_LAST_NAME, individual.getLastName());

            individuals = query.getResultList();
        }

        return individuals;
    }

    /** Find individual(s) matching the given email address(es) in the example */
    public Collection<Individual> findByEmailAddress(Individual individual) {
        Collection<Individual> individuals;

        Query query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_EMAIL_ADDRESSES);
        Set<PointOfContact> pocs = individual.getPointsOfContactOfType(POCType.EmailAddress);
        query.setParameter(PartyDAOSupport.PARAM_EMAIL, PointOfContactTools.extractValues(pocs));

        individuals = query.getResultList();

        return individuals;
    }

     public Collection<Individual> findByTag(String tag) {
        Collection<Individual> individuals;

        Query query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_TAG);
        query.setParameter(PartyDAOSupport.PARAM_TAG_NAME, tag);

        individuals = query.getResultList();

        return individuals;
    }

    /**
     * List all Individual.  Checks status
     * @return
     */
    public Collection<Individual> listAll() {
        Collection<Individual> organisations;
        Query query = em.createNamedQuery(QUERY_ALL_INDIVIDUALS);
        organisations = query.getResultList();
        return organisations;
    }

    /**
     *
     * @param tags   if non-empty, lists parties with any of these tags.  If empty, list all parties
     * @return
     */
    public Collection<Individual> listByTags(Set<Tag> tags) {
        Collection<Individual> individuals;

        if (tags.size() > 0) {
            Query query = em.createNamedQuery(QUERY_INDIVIDUAL_BY_TAGS);
            query.setParameter(PartyDAOSupport.PARAM_TAGS, tags);

            individuals = query.getResultList();
        } else {
            individuals = listAll();
        }

        return individuals;
    }

    public void persist(Individual individual) {
        em.persist(individual);
    }
}
