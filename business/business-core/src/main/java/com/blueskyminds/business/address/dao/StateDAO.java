package com.blueskyminds.business.address.dao;

import com.blueskyminds.business.region.graph.State;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;

/**
 * Date Started: 5/01/2008
 * <p/>
 * History:
 */
public class StateDAO extends AbstractDAO<State> {

    public StateDAO(EntityManager em) {
        super(em, State.class);
    }
}