package com.blueskyminds.homebyfive.framework.core.tools.substitutions.dao;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

/**
 * A DAO to access the pattern-matching substitutions
 * <p/>
 * Date Started: 13/06/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class SubstitutionDAO extends AbstractDAO<Substitution> {

    private static final String QUERY_BY_GROUPNAME = "substitutions.byGroup";
    private static final String QUERY_BY_GROUPNAME_AND_PATTERN = "substitution.byGroupAndPattern";
    private static final String QUERY_ALL  = "substitutions.all";

    private static final String GROUP_NAME = "groupName";
    private static final String PARAM_PATTERN = "pattern";

    public SubstitutionDAO(EntityManager em) {
        super(em, Substitution.class);
    }

    /**
     * Get the set of substitutions defined in the specified groupName
     *
     * @param groupName
     * @return Country instance, or null if not found
     */
    public Set<Substitution> listSubstitutionsForGroup(String groupName) {
        List<Substitution> substitutions;

        Query query = em.createNamedQuery(QUERY_BY_GROUPNAME);
        query.setParameter(GROUP_NAME, groupName);

        substitutions = (List<Substitution>) query.getResultList();

        return setOf(substitutions);
    }

    /**
     * Get all substitutions
     *
     */
    public Set<Substitution> listSubstitutions() {
        List<Substitution> substitutions;

        Query query = em.createNamedQuery(QUERY_ALL);
        substitutions = (List<Substitution>) query.getResultList();
        return setOf(substitutions);

    }

    // ------------------------------------------------------------------------------------------------------

    public Substitution createSubstitution(Substitution substitution) {
        em.persist(substitution);
        return substitution;
    }

    public void deleteSubstitution(Long id) {
        Substitution substitution = findById(id);
        if (substitution != null) {
            em.remove(substitution);
        }
    }

    public Substitution updateSubstitution(Long id, Substitution changes) {
        Substitution existing = findById(id);
        if (existing != null) {
            existing.updateFrom(changes);
            em.persist(existing);
        }
        return existing;
    }

    public Substitution lookupSubstitution(String groupName, String pattern) {
        Query query = em.createNamedQuery(QUERY_BY_GROUPNAME_AND_PATTERN);
        query.setParameter(GROUP_NAME, groupName);
        query.setParameter(PARAM_PATTERN, pattern);
         
        return firstIn((List<Substitution>) query.getResultList());        
    }
}
