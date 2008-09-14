package com.blueskyminds.enterprise.ejb.party;

import com.blueskyminds.enterprise.party.Organisation;
import com.blueskyminds.enterprise.party.Individual;
import com.blueskyminds.enterprise.party.service.PartyServiceException;
import com.blueskyminds.framework.tools.selector.SingleSelector;

import javax.ejb.Local;

/**
 * Local interface to the party service bean
 *
 * Date Started: 29/06/2007
 * <p/>
 * History:
 */
@Local
public interface PartySessionBeanLocal {

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
}
