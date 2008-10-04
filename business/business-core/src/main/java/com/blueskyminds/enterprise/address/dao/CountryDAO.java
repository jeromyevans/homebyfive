package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.enterprise.region.graph.Country;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;

/**
 * Date Started: 5/01/2008
 * <p/>
 * History:
 */
public class CountryDAO extends AbstractDAO<Country> {

    public CountryDAO(EntityManager em) {
        super(em, Country.class);
    }

}
