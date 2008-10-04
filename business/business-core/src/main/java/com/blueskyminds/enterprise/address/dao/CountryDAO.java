package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.enterprise.region.graph.CountryHandle;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;

/**
 * Date Started: 5/01/2008
 * <p/>
 * History:
 */
public class CountryDAO extends AbstractDAO<CountryHandle> {

    public CountryDAO(EntityManager em) {
        super(em, CountryHandle.class);
    }

}
