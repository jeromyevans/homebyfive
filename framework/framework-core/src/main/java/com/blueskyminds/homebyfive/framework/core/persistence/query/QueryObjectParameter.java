package com.blueskyminds.homebyfive.framework.core.persistence.query;

/**
 * Parameters:
 *  Q is the Query implementation
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class QueryObjectParameter extends QueryParameter {

    private Object object;

    public QueryObjectParameter(String name, Object object) {
        super(name);
        this.object = object;
    }

    public QueryObjectParameter(int index, Object object) {
        super(index);
        this.object = object;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the QueryObjectParameter with default attributes
     */
    private void init() {
    }


    public Object getObject() {
        return object;
    }
}
