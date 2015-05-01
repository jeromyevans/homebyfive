package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;
import java.util.Collection;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class StateEAO extends AbstractRegionDAO<State> {

    private static final String QUERY_ALL_STATES_BY_PARENT_PATH = "hp.states.byParentPath";
    private static final String QUERY_STATE_BY_PATH = "hp.state.byPath";
    private static final String PARAM_PATH = "path";
    private static final String QUERY_STATE_BY_HANDLE = "hp.state.byHandle";
    private static final String PARAM_HANDLE = "handle";
    private static final String QUERY_ALL_STATES = "state.all";
    private static final String QUERY_BY_TAGS = "state.byTags";
    private static final String QUERY_BY_PARENTPATH_AND_TAGS = "state.byParentPathAndTags";

    @Inject   
    public StateEAO(EntityManager entityManager) {
        super(entityManager, State.class);
    }

    /**
     * Get a list of all the states in the specified country (eg. /au)
     *
     * @return States, or empty set
     */
    public Set<State> list(String parentPath) {

        Query query = em.createNamedQuery(QUERY_ALL_STATES_BY_PARENT_PATH);
        query.setParameter(PARAM_PATH, parentPath);

        return setOf(query.getResultList());
    }

    /**
     * Get the state with the specified path (eg. /au/nsw)
     *
     * @return State, or null if not found
     */
    public State lookup(String path) {

        Query query = em.createNamedQuery(QUERY_STATE_BY_PATH);
        query.setParameter(PARAM_PATH, path);

        return firstIn(query.getResultList());
    }

    public Set<State> list() {
        Query query = em.createNamedQuery(QUERY_ALL_STATES);
        return setOf(query.getResultList());
    }
    
     /**
     * @param tags   if non-empty, lists regions with any of these tags.
     * If the set is empty, list all regions are listed
     * @return
     */
    @Override
    public Collection<State> listByTags(Set<Tag> tags) {
        return super.default_listByTags(QUERY_BY_TAGS, tags);
    }

    /**
     * List regions in the parent path with any of the specified tags.
     * If the set is emply, all regions are listed
     */
    @Override
    public Collection<State> listByTags(String parentPath, Set<Tag> tags) {
        return super.default_listByTags(QUERY_BY_PARENTPATH_AND_TAGS, parentPath, tags);
    }
  
}