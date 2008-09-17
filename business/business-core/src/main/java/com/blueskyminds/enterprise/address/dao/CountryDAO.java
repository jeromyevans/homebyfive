package com.blueskyminds.enterprise.address.dao;

import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;

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