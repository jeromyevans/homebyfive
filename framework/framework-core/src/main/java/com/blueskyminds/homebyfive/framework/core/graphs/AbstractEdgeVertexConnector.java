package com.blueskyminds.homebyfive.framework.core.graphs;

import com.blueskyminds.homebyfive.framework.core.ManyToManyMap;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.*;

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
@MappedSuperclass
@ManyToManyMap
public class AbstractEdgeVertexConnector<V extends Vertex, E extends Edge> extends AbstractDomainObject implements EdgeVertexConnector {

    private V vertex;
    private E edge;
    private ConnectorRole role;

    public AbstractEdgeVertexConnector(V vertex, E edge, ConnectorRole role) {
        this.vertex = vertex;
        this.edge = edge;
        this.role = role;
    }

    /** Default constructor for ORM */
    protected AbstractEdgeVertexConnector() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractEdgeVertexConnector with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** The vertex this connector is for */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "VertexId")
    public V getVertex() {
        return vertex;
    }

    public void setVertex(V vertex) {
        this.vertex = vertex;
    }

    /** The edge the connector is attached to */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EdgeId")
    public E getEdge() {
        return edge;
    }

    public void setEdge(E edge) {
        this.edge = edge;
    }

    /** The role of this connection WRT the edge.  In order words, which end of the Edge it is */
    @Enumerated
    @Column(name="Role")
    public ConnectorRole getRole() {
        return role;
    }

    public void setRole(ConnectorRole role) {
        this.role = role;
    }
}
