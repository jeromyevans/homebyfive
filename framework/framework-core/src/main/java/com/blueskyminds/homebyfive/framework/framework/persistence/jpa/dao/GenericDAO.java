package com.blueskyminds.homebyfive.framework.framework.persistence.jpa.dao;

import javax.persistence.EntityManager;

/**
 * A generic implementation of a DAO - implements some queries applicable to all entity types
 *
 * This is essentially a contrete implementation of the AbstractDAO
 * 
 * Date Started: 8/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class GenericDAO extends AbstractDAO {

    public GenericDAO(EntityManager em, Class clazz) {
        super(em, clazz);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the GenericDAO with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
