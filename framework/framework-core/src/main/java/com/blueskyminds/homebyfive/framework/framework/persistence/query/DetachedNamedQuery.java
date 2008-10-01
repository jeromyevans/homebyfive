package com.blueskyminds.homebyfive.framework.framework.persistence.query;

/**
 * A vendor-independent named query
 *
 * Date Started: 13/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class DetachedNamedQuery extends DetachedQuery implements PersistenceNamedQuery {

    private String name;

    public DetachedNamedQuery(String name) {
        this.name = name;
        init();
    }

    /**
     * Initialise the DetachedNamedQuery with default attributes
    */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------
}
