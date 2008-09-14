package com.blueskyminds.enterprise.ejb.party;

import com.blueskyminds.enterprise.party.service.PartyServiceImpl;
import com.blueskyminds.enterprise.party.service.PartyServiceException;
import com.blueskyminds.enterprise.party.Organisation;
import com.blueskyminds.enterprise.party.Individual;
import com.blueskyminds.framework.tools.selector.SingleSelector;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Stateless session-bean implementation of the PartyService
 *
 * Date Started: 29/06/2007
 * <p/>
 * History:
 */
@Stateless
public class PartySessionBean extends PartyServiceImpl implements PartySessionBeanLocal {

    public PartySessionBean(EntityManager em) {
        super(em);
    }

    public PartySessionBean() {
    }

    /**
     * Creates a new organisation if it doesn't already exist.
     * <p/>
     * The exampleOrganisation is used to find existing candidates.  A Selector is used to select the best
     * candidate.  If a candidate exists the properties of the exampleOrganisation are merged into the
     * candidate.
     *
     * @param exampleOrganisation used to find an existing organisation, or create a new one
     * @return Organisation a persistent organisation (new, existing or updated)
     */
    public Organisation createOrMergeOrganisation(Organisation exampleOrganisation) throws PartyServiceException {
        return super.createOrMergeOrganisation(exampleOrganisation);
    }

    /**
     * Creates a new organisation if it doesn't already exist.
     * <p/>
     * The exampleOrganisation is used to find existing candidates.  A Selector is used to select the best
     * candidate.  If a candidate exists the properties of the exampleOrganisation are merged into the
     * candidate.
     *
     * @param exampleOrganisation used to find an existing organisation, or create a new one
     * @return a persistent organisation (new, existing or updated)
     */
    public Organisation createOrMergeOrganisation(Organisation exampleOrganisation, SingleSelector<Organisation> selector) throws PartyServiceException {
        return super.createOrMergeOrganisation(exampleOrganisation, selector);
    }

    /**
     * Creates a new individual if it doesn't already exist.
     * <p/>
     * The exampleIndividual is used to find existing candidates.  A Selector is used to select the best
     * candidate.  If a candidate exists the properties of the exampleIndividual are merged into the
     * candidate.
     *
     * @param exampleIndividual used to find an existing individual, or create a new one
     * @return a persistent individual (new, existing or updated)
     */
    public Individual createOrMergeIndividual(Individual exampleIndividual) throws PartyServiceException {
        return super.createOrMergeIndividual(exampleIndividual);
    }

    /**
     * Creates a new individual if it doesn't already exist.
     * <p/>
     * The exampleIndividual is used to find existing candidates.  A Selector is used to select the best
     * candidate.  If a candidate exists the properties of the exampleIndividual are merged into the
     * candidate.
     *
     * @param exampleIndividual used to find an existing individual, or create a new one
     * @return a persistent individual (new, existing or updated)
     */
    public Individual createOrMergeIndividual(Individual exampleIndividual, SingleSelector<Individual> selector) throws PartyServiceException {
        return super.createOrMergeIndividual(exampleIndividual, selector);
    }

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}
