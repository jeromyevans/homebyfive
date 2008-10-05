package com.blueskyminds.enterprise.region.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.enterprise.region.index.StateBean;
import com.blueskyminds.enterprise.region.graph.State;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class StateEAO extends AbstractDAO<State> {

    private static final String QUERY_ALL_STATES_BY_PARENT_PATH = "hp.states.byParentPath";
    private static final String QUERY_STATE_BY_PATH = "hp.state.byPath";
    private static final String PARAM_PATH = "path";
    private static final String QUERY_STATE_BY_HANDLE = "hp.state.byHandle";
    private static final String PARAM_HANDLE = "handle";

    @Inject   
    public StateEAO(EntityManager entityManager) {
        super(entityManager, State.class);
    }

    /**
     * Get a list of all the states in the specified country (eg. /au)
     *
     * @return States, or empty set
     */
    public Set<State> listStates(String parentPath) {

        Query query = em.createNamedQuery(QUERY_ALL_STATES_BY_PARENT_PATH);
        query.setParameter(PARAM_PATH, parentPath);

        return setOf(query.getResultList());
    }

    /**
     * Get the state with the specified path (eg. /au/nsw)
     *
     * @return State, or null if not found
     */
    public State lookupState(String path) {

        Query query = em.createNamedQuery(QUERY_STATE_BY_PATH);
        query.setParameter(PARAM_PATH, path);

        return firstIn(query.getResultList());
    }
  
}