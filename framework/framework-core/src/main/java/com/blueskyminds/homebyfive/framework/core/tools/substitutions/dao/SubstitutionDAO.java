package com.blueskyminds.homebyfive.framework.core.tools.substitutions.dao;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

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

    private static final String QUERY_BY_GROUPNAME = "substitution.byGroup";
    private static final String GROUP_NAME = "groupName";

    public SubstitutionDAO(EntityManager em) {
        super(em, Substitution.class);
    }

    /**
     * Get an ordered list of all the substitutions defined in the specified groupName
     *
     * @param groupName
     * @return Country instance, or null if not found
     */
    public List<Substitution> getSubstitutionsForGroup(String groupName) {
        List<Substitution> substitutions;

        Query query = em.createNamedQuery(QUERY_BY_GROUPNAME);
        query.setParameter(GROUP_NAME, groupName);

        substitutions = (List<Substitution>) query.getResultList();

        return substitutions;
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
}
