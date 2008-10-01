package com.blueskyminds.homebyfive.framework.core.persistence.query;

import javax.persistence.Query;
import javax.persistence.EntityManager;

/**
 * Date Started: 11/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class QueryFactory {

    /** Create a query that finds all entities of the specified class */
    public static Query createFindAllQuery(EntityManager entityManager, Class entityClass) {
        if (entityManager != null) {
            return entityManager.createQuery("select o from "+entityClass.getName()+ " o");
        } else {
            return null;
        }
    }
}
