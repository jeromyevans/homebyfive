package com.blueskyminds.enterprise.accounting.service;

import com.blueskyminds.enterprise.taxpolicy.TaxPolicy;

/**
 * Date Started: 8/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface AccountingService {
    
    /** Lookup a tax policy by its name */
    TaxPolicy lookupTaxPolicy(String name);

}
