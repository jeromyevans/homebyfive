package com.blueskyminds.homebyfive.business.accounting.dao;

import com.blueskyminds.homebyfive.business.taxpolicy.TaxPolicy;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Date Started: 8/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TaxPolicyDAO extends AbstractDAO<TaxPolicy> {

    private static final String QUERY_TAX_POLCY_BY_NAME = "taxPolicy.byName";
    private static final String PARAM_NAME = "name";

    public TaxPolicyDAO(EntityManager em) {
        super(em, TaxPolicy.class);
    }

    /** Looks up a tax policy by its unique name */
    public TaxPolicy findTaxPolicyByName(String name) {
        Query query = em.createNamedQuery(QUERY_TAX_POLCY_BY_NAME);
        query.setParameter(PARAM_NAME, name);
        return findOne(query);
    }
}
