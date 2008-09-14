package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;

/**
 * Suburb-specific queries
 *
 * Date Started: 30/10/2007
 * <p/>
 * History:
 */
public class SuburbDAO extends AbstractDAO<SuburbHandle> {

    public SuburbDAO(EntityManager em) {
        super(em, SuburbHandle.class);
    }
}