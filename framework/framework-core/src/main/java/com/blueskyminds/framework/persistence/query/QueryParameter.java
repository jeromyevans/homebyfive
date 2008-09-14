package com.blueskyminds.framework.persistence.query;

import javax.persistence.Query;

/**
 * Represents a parameter for a vendor independent query
 *
 * Parameters:
 *  Q is the Query implementation
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class QueryParameter {

    private String name;
    private int index;
    private boolean isNamed;

    protected QueryParameter(String name) {
        this.name = name;
        isNamed = true;
    }

    protected QueryParameter(int index) {
        this.index = index;
        isNamed = false;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the QueryParameter with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------


    public boolean isNamed() {
        return isNamed;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
