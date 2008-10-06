package com.blueskyminds.business.accounting.service;

import com.blueskyminds.business.taxpolicy.TaxPolicy;
import com.blueskyminds.business.accounting.dao.TaxPolicyDAO;

import javax.persistence.EntityManager;

/**
 * Default implementation of the accounting service
 *
 * Date Started: 8/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AccountingServiceImpl implements AccountingService {

    private EntityManager em;

    public AccountingServiceImpl(EntityManager em) {
        this.em = em;
    }

    public AccountingServiceImpl() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AccountingServiceImpl with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Lookup a tax policy by its unique name
     */
    public TaxPolicy lookupTaxPolicy(String name) {
        return new TaxPolicyDAO(em).findTaxPolicyByName(name);
    }
}
