package com.blueskyminds.homebyfive.framework.core.hierarchy;

/**
 * A class that maps a parent child association
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface ParentChildRelationship<P, C> {

    /**
     * @return the Parent instance
     */
    P getParent();

    /**
     * @return the Child instance
     */
    C getChild();
}
