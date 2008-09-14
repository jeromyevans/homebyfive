package com.blueskyminds.framework.persistence.jpa.query;

import com.blueskyminds.framework.persistence.query.PersistenceNamedQuery;

import javax.persistence.Query;
import javax.persistence.EntityManager;

/**
 * A named query.
 * The query must be known by the EntityManager
 *
 * A Jpa named query can be defined either by:
 *   - attaching an annotation @PersistenceNamedQuery to an entity (not recommended)
 *   - defining the named query in an orm.xml file (recommended so its separate from the code)
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JpaNamedQuery extends JpaQuery implements PersistenceNamedQuery {

    private String name;

    // ------------------------------------------------------------------------------------------------------

    public JpaNamedQuery(String name) {
        this.name = name;
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the JpaNamedQuery with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Creates the underlying PersistenceNamedQuery instance using the EntityManager */
    public void prepareQuery(EntityManager entityManager) {
        Query query = entityManager.createNamedQuery(name);
        applyParameters(query);
        setQuery(query);
    }

    // ------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }
}
