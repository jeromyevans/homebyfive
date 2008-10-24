package com.blueskyminds.homebyfive.framework.core.tools.substitutions.service;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.SubstitutionComparator;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.dao.SubstitutionDAO;

import java.util.*;

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
    public List<Substitution> listSubstitutionsForGroup(String groupName) {
        List<Substitution> results = cachedSubstitutions.get(groupName); // not threadsafe
        if (results == null) {
            results = new ArrayList<Substitution>(substitutionDAO.listSubstitutionsForGroup(groupName));
            Collections.sort(results, new SubstitutionComparator());
            cachedSubstitutions.put(groupName, results);  // not threadsafe
        }
        return results;
    }

    /**
     * Get all substitutions
     *
     * This method is not threadsafe.  Use one SubstitutionService instance per thread.
     *
     * @return
     */
    public List<Substitution> listSubstitutions() {
        List<Substitution> results = new ArrayList<Substitution>(substitutionDAO.listSubstitutions());
        Collections.sort(results, new SubstitutionComparator());
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
    public Substitution createOrUpdateSubstitution(Substitution substitution) {
        Substitution existing = substitutionDAO.lookupSubstitution(substitution.getGroupName(), substitution.getPattern());
        if (existing != null) {
            return substitutionDAO.updateSubstitution(existing.getId(), substitution);
        } else {
            return substitutionDAO.createSubstitution(substitution);
        }
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
