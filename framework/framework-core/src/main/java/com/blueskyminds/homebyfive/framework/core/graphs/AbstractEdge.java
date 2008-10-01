package com.blueskyminds.homebyfive.framework.core.graphs;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.*;
import java.util.*;

/**
 * An Edge in a Graph
 *
 * An edge is between two Connectors - connector with role A to vertex A and connector with role B to vertex B
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@MappedSuperclass
public class AbstractEdge<C extends EdgeVertexConnector, E extends Edge> extends AbstractDomainObject implements Edge<C> {

    /** The set of connectors attached to this edge, each with a different role */
    private Set<C> connectors;
    private EdgeDirection direction;

    // ------------------------------------------------------------------------------------------------------


    public AbstractEdge(EdgeDirection direction) {
        this.direction = direction;
        init();
    }

    public AbstractEdge(C connectorA, C connectorB, EdgeDirection direction) {
        this.direction = direction;
        init();
        setConnectorA(connectorA);
        setConnectorB(connectorB);
    }

    protected AbstractEdge() {
    }

    /** Returnst he connector with the specified role */
    private C getConnectorWithRole(ConnectorRole role) {
        C match = null;

        for (C connector : connectors) {
            if (connector.getRole().equals(role)) {
                match = connector;
                break;
            }
        }
        return match;
    }

    /** Returns the connector with Role A (the A end of the Edge) */
    @Transient
    public C getConnectorA() {
        return getConnectorWithRole(ConnectorRole.A);
    }

    public void setConnectorA(C connectorA) {
        connectors.add(connectorA);
    }

    /** Returns the connector with Role B (the B end of the Edge) */
    @Transient
    public C getConnectorB() {
        return getConnectorWithRole(ConnectorRole.B);
    }

    public void setConnectorB(C connectorB) {
        connectors.add(connectorB);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AbstractEdge with default attributes
     */
    private void init() {
        connectors = new HashSet<C>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the connectors for this edge */
    @OneToMany(mappedBy = "edge", cascade = CascadeType.ALL)
    protected Set<C> getConnectors() {
        return this.connectors;
    }

    protected void setConnectors(Set<C> connectors) {
        this.connectors = connectors;
    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name = "Direction")
    public EdgeDirection getDirection() {
        return direction;
    }

    public void setDirection(EdgeDirection direction) {
        this.direction = direction;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determine if this Edge is traversible from the specified Vertex.
     *
     * @param vertex
     * @return true if this Edge is connected to the Vertex (directly) and the direction permits traversal
     */
    @Transient
    public boolean isTraverseable(Vertex vertex) {
        boolean traversible = false;

        switch (direction) {
            case AtoB:
                traversible = getConnectorA().getVertex().equals(vertex);
                break;
            case BtoA:
                traversible = getConnectorB().getVertex().equals(vertex);
                break;
            case Undirected:
                traversible = ((getConnectorA().getVertex().equals(vertex)) || (getConnectorB().getVertex().equals(vertex)));
                break;
        }
        return traversible;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the Vertex connected to this edge that is not hte one specified (the one specified must be a
     *  vertex though) 
     * */
    @Transient
    public Vertex getOtherVertex(Vertex vertex) {
        if (getConnectorA().getVertex().equals(vertex)) {
            return getConnectorB().getVertex();
        } else {
            if (getConnectorB().getVertex().equals(vertex)) {
                return getConnectorA().getVertex();
            }
            else {
                return null;
            }
        }
    }
}
