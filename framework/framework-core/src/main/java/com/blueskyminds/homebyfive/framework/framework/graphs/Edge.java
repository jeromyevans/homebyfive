package com.blueskyminds.homebyfive.framework.framework.graphs;

/**
 * An Edge in a graph that has direction (directed from Primary to Secondary)
 *
 * An edge connects two Nodes
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface Edge<C extends EdgeVertexConnector> {

    C getConnectorA();

    void setConnectorA(C connectorA);

    C getConnectorB();

    void setConnectorB(C connectorB);

    EdgeDirection getDirection();

    /**
     * True if this edge is traversible from the specified vertex
     *
     * The edge must be connected to the vertex and have appropriate direction
     *
     * @param vertex from which to traverse this edge
     * @return true if it can be traversed
     **/
    boolean isTraverseable(Vertex vertex);

    /**
     * Returns the Vertex that's not the given vertex.
     *
     * (ignores traversability)
     *
     * return the othe Vertex, or null if vertex is not connected to this Edge
     */
    public Vertex getOtherVertex(Vertex vertex);
}
