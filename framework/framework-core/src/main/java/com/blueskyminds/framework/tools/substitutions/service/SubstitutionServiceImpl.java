package com.blueskyminds.framework.tools.substitutions.service;

import com.blueskyminds.framework.tools.substitutions.Substitution;
import com.blueskyminds.framework.tools.substitutions.dao.SubstitutionDAO;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * This implementation reads the substations via the DAO and caches the results for each group.  Subsequent lookups
 * use the group.
 *
 * This is not thread-safe.  Use one instance per thread.
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
public class SubstitutionServiceImpl implements SubstitutionService {

    private final Map<String, List<Substitution>> cachedSubstitutions;
    private SubstitutionDAO substitutionDAO;

    public SubstitutionServiceImpl(SubstitutionDAO substitutionDAO) {
        this.substitutionDAO = substitutionDAO;
        cachedSubstitutions = new HashMap<String, List<Substitution>>();
    }

    /**
     * Get the substitutions for the group, reading from the cache if available
     *
     * This method is not threadsafe.  Use one SubstitutionService instance per thread.
     *
     * @param groupName
     * @return
     */
    public List<Substitution> getSubstitutionsForGroup(String groupName) {
        List<Substitution> results = cachedSubstitutions.get(groupName); // not threadsafe
        if (results == null) {
            results = substitutionDAO.getSubstitutionsForGroup(groupName);
            cachedSubstitutions.put(groupName, results);  // not threadsafe
        }
        return results;
    }

    public void setSubstitutionDAO(SubstitutionDAO substitutionDAO) {
        this.substitutionDAO = substitutionDAO;
    }

    /**
     * Note: updates are not applied to the cache
     * @param substitution
     * @return
     */
    public Substitution createSubstitution(Substitution substitution) {
        return substitutionDAO.createSubstitution(substitution);
    }

    /**
     * Note: updates are not applied to the cache
     * @return
     */
    public void deleteSubstitution(Long id) {
        substitutionDAO.deleteSubstitution(id);
    }

    /**
     * Note: updates are not applied to the cache
     * @return
     */
    public Substitution updateSubstitution(Long id, Substitution changes) {
        return substitutionDAO.updateSubstitution(id, changes);
    }
}
