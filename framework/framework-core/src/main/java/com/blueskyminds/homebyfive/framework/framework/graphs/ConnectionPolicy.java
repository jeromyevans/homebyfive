package com.blueskyminds.homebyfive.framework.framework.graphs;

/**
 * A policy that determines whether an Edge can be connected to a Vertex
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface ConnectionPolicy<V extends Vertex, E extends Edge> {

     /**
     * Returns true of the specified edge is allowed to to be connected to the vertex
     * @param vertex that is being connected to
     * @param edge that is being connected
     * @return true if the connection is permitted
     */
    boolean connectionPermitted(V vertex, E edge);
}
