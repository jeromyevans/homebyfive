package com.blueskyminds.homebyfive.framework.core.tools.substitutions.service;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;

import java.util.List;
import java.util.Set;

/**
 * Provides access to text substitutions commonly used in the framework's pattern matching
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
public interface SubstitutionService {

    /**
     * Get an ordered list of all the substitutions defined in the specified groupName
     *
     * @param groupName
     * @return
     */
    List<Substitution> listSubstitutionsForGroup(String groupName);

    /**
     * Get all substitutions
     *
     * This method is not threadsafe.  Use one SubstitutionService instance per thread.
     *
     * @return
     */
    List<Substitution> listSubstitutions(); 

    /**
     * Persist a new substitution
     *
     * @param substitution
     * @return the Substitution instance created
     */
    Substitution createOrUpdateSubstitution(Substitution substitution);

    /**
     * Delete the substitution identified by id
     * @param id
     */
    void deleteSubstitution(Long id);

    /**
     * Apply changes to an existing substitution.
     * Null values will be ignored
     *
     * @param id
     * @param changes
     * @return
     */
    Substitution updateSubstitution(Long id, Substitution changes);
}
