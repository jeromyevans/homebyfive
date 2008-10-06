package com.blueskyminds.homebyfive.business.address.dao;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;

/**
 * Suburb-specific queries
 *
 * Date Started: 30/10/2007
 * <p/>
 * History:
 */
public class SuburbDAO extends AbstractDAO<Suburb> {

    public SuburbDAO(EntityManager em) {
        super(em, Suburb.class);
    }
}
