package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;

/**
 * Date Started: 5/01/2008
 * <p/>
 * History:
 */
public class StateDAO extends AbstractDAO<StateHandle> {

    public StateDAO(EntityManager em) {
        super(em, StateHandle.class);
    }
}