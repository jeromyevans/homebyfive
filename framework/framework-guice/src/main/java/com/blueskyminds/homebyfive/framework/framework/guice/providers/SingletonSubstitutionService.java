package com.blueskyminds.homebyfive.framework.framework.guice.providers;

import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.dao.SubstitutionDAO;
import com.blueskyminds.homebyfive.framework.framework.tools.substitutions.service.SubstitutionService;
import com.google.inject.Provider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

import org.apache.commons.collections.FastHashMap;

/**
 * Reads the substitutions once and stores them in a FastHashMap.
 * A Provider<SubstitutionDAO> is required to get the EntityManager for the current thread
 * then a non-cached group is encountered
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
@Singleton
public class SingletonSubstitutionService implements SubstitutionService {

    private FastHashMap cachedGroups;
    private Provider<SubstitutionDAO> substitutionDAOProvider;

    public SingletonSubstitutionService() {
        cachedGroups = new FastHashMap();
    }

    /**
     * Gets the substitutions for the named group from the cache or delegate
     *
     * @param groupName
     * @return
     */
    public List<Substitution> getSubstitutionsForGroup(String groupName) {
        List<Substitution> result = (List<Substitution>) cachedGroups.get(groupName);
        if (result == null) {
            result = substitutionDAOProvider.get().getSubstitutionsForGroup(groupName);
            cachedGroups.put(groupName, result);
        }
        return result;
    }

    public Substitution createSubstitution(Substitution substitution) {
        return substitutionDAOProvider.get().createSubstitution(substitution);
    }

    public void deleteSubstitution(Long id) {
        substitutionDAOProvider.get().deleteSubstitution(id);
    }

    public Substitution updateSubstitution(Long id, Substitution changes) {
        return substitutionDAOProvider.get().updateSubstitution(id, changes);
    }

    @Inject
    public void setSubstitutionDAOProvider(Provider<SubstitutionDAO> substitutionDAOProvider) {
        this.substitutionDAOProvider = substitutionDAOProvider;
    }
}
