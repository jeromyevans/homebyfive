package com.blueskyminds.homebyfive.framework.core.graphs;

/**
 * Connects a single Edge to a Vertex in a graph.  The role of the Connector identifies which end of the edge is
 *  is connecting (A or B).
 *
 * An Edge belongs to exactly two Vertices
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface EdgeVertexConnector {

    Vertex getVertex();

    Edge getEdge();

    ConnectorRole getRole();

}
