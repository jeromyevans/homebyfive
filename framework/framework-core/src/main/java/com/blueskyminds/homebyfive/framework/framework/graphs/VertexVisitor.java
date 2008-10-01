package com.blueskyminds.homebyfive.framework.framework.graphs;

/**
 * Interface to a visitor that can be used to visit (traverse) the connected set of a vertex
 *
 * Date Started: 30/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface VertexVisitor<V extends Vertex> {

    void visit(V vertex);
}
