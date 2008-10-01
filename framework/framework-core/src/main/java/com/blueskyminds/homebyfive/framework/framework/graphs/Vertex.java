package com.blueskyminds.homebyfive.framework.framework.graphs;

import java.util.Set;

/**
 * A Vertex in a Graph
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * 
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface Vertex {

    /** Get the edges connected to the Vertex */
    Set<Edge> getEdges();

    /**
     * @param vertex
     * @return true if this Vertex is directly connected to the specified vertex
     */
    boolean isAdjacentTo(Vertex vertex);

    /**
     * Returns true if this vertex is connected to the specified vertex
     * @param vertex
     * @return
     */
    boolean isConnectedTo(Vertex vertex);

    /** Get the entire set of Vertces this Vertex is connected to
     * @return 0 or more vertiex connected to this one (traversible) */
    Set<Vertex> getConnectedSet();

    /**
     * Get the set of adjacent Nodes
     * @return adjacent nodes
     */
    Set<Vertex> getAdjacentSet();

    /**
     * Get the set of adjacent Nodes that have an edge to this Node but because they're directed, they can't be
     *  traversed to from this node
     *
     * @return Set of nodes that have a directed edge to this node that can't be traversed
     */
    Set<Vertex> getReverseAdjacentSet();

    /** Get the set of Nodes that connect to this vertex in the reverse direction.  In strict graph
     *  terms they are not connected
     *
     * Performs a depth-first traversal
     *
     * @return set of vertices connected to this vertex in reverse (non-traversible but connected) */
    Set<Vertex> getReverseConnectedSet();
}
