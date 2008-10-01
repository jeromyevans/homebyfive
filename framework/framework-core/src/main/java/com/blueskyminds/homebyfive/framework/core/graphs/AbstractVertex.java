package com.blueskyminds.homebyfive.framework.core.graphs;

import com.blueskyminds.homebyfive.framework.core.graphs.Vertex;
import com.blueskyminds.homebyfive.framework.core.graphs.Edge;
import com.blueskyminds.homebyfive.framework.core.graphs.EdgeDirection;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * An abstract implementation of a DomainObject that partipates in a Graph
 *
 * Date Started: 15/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@MappedSuperclass
public abstract class AbstractVertex<V extends Vertex, E extends Edge, C extends EdgeVertexConnector> extends AbstractDomainObject implements Vertex {

    /**
     * The edges connected to the Vertex
     */
    private Set<C> edgeConnectors;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Create a new hierarchy with default memento */
    public AbstractVertex() {
        init();
    }

     // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise with defult values
     */
    private void init() {
        edgeConnectors = new HashSet<C>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the edges connected to this Vertex */
    @Transient
    public Set<Edge> getEdges() {
        Set<Edge> edges = new HashSet<Edge>();
        for (EdgeVertexConnector connector : edgeConnectors) {
            edges.add(connector.getEdge());
        }
        return edges;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the edge connectors for this vertex. */
    @OneToMany(mappedBy = "vertex", cascade = CascadeType.ALL)
    protected Set<C> getEdgeConnectors() {
        return edgeConnectors;
    }

    protected void setEdgeConnectors(Set<C> edgeConnectors) {
        this.edgeConnectors = edgeConnectors;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * @param vertex
     * @return true if this Vertex is directly connected to the specified vertex
     */
    @Transient
    public boolean isAdjacentTo(Vertex vertex) {
        boolean connected = false;
        
        for (Edge edge : getEdges()) {
            if (edge.isTraverseable(this)) {
                if (edge.getOtherVertex(this).equals(vertex)) {
                    connected = true;
                    break;
                }
            }
        }
        return connected;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Factory method to instantiate an a new Edge */
    protected abstract E createEdge(EdgeDirection direction);

    /** Factory method to instantiate an a new EdgeVertexConnector */
    protected abstract C createEdgeConnector(V vertex, E edge, ConnectorRole role);

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a connection between this DomainObject and another DomainObject (an edge)
     *
     * A connection consists of :
     *    One Edge
     *    Two VertexEdgeConnectors pointing to the Edge from Vertex
     *
     * The request will be ignored if there's already a single edge to the specified Vertex that's
     *  traversible
     *
     * @param vertexB to be connected
     */
    public boolean addConnection(V vertexB, EdgeDirection direction) {
        boolean added = false;

        if (!isAdjacentTo(vertexB)) {
            E newEdge = createEdge(direction);
            C connectorA = createEdgeConnector((V) this, newEdge, ConnectorRole.A);
            C connectorB = createEdgeConnector(vertexB, newEdge, ConnectorRole.B);
            newEdge.setConnectorA(connectorA);
            newEdge.setConnectorB(connectorB);

            // add a reference to the new Edge to each of the Vertices (one connector for each)
            edgeConnectors.add(connectorA);
            ((AbstractVertex<V, E, C>) vertexB).edgeConnectors.add(connectorB);

            added = true;
        }

        return added;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the set of Nodes this Vertex is connected to.
     *
     * Performs a depth-first traversal  */
    @Transient
    public Set<Vertex> getConnectedSet() {
        Set<Vertex> visited = new HashSet<Vertex>();

        traverse(this, visited);

        return visited;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the set of Nodes this Vertex is connected to.
     *
     * Performs a depth-first traversal  */
    @SuppressWarnings({"unchecked"})
    @Transient
    public <T> Set<T> getConnectedSet(Class<T> clazz) {
        Set<T> connectedSet = new HashSet<T>();

        for (Vertex v : getConnectedSet()) {
            connectedSet.add((T) v);
        }
        return connectedSet;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the set of adjacent Nodes that can be traversed from this node
     *
     * @return Set of nodes that are adjacent to this node
     */
    @Transient
    public Set<Vertex> getAdjacentSet() {
        Set<Vertex> adjacents = new HashSet<Vertex>();
        for (EdgeVertexConnector connector : edgeConnectors) {
            Edge edge = connector.getEdge();
            if (edge.isTraverseable(this)) {
                adjacents.add(edge.getOtherVertex(this));
            }
        }
        return adjacents;
    }

     /**
     * Get the set of adjacent Nodes that can be traversed from this node, casting the nodes to the
     *  specified class
     *
     * @return Set of nodes that are adjacent to this node cast to clazz
     */
    @SuppressWarnings({"unchecked"})
    @Transient
    public <T> Set<T> getAdjacentSet(Class<T> clazz) {
        Set<T> adjacents = new HashSet<T>();
        for (Vertex v : getAdjacentSet()) {
            adjacents.add((T) v);
        }
        return adjacents;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Recursive depth-first traversal from a vertex to its adjacent vertices where the adjacent vertex has not already
     *  been visited
     *
     * @param vertex
     * @param visited - vertices that are visited are added to this set.  It's used to ensure vertices that have
     *   previously been visited are not visited again
     */
    private void traverse(Vertex vertex, Set<Vertex> visited) {
        Set<Vertex> adjacents = vertex.getAdjacentSet();

        for (Vertex adjacentVertex : adjacents) {
            if (!visited.contains(adjacentVertex)) {
                visited.add(adjacentVertex);
                traverse(adjacentVertex, visited);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this vertex is connected to the specified vertex
     * @param vertex
     * @return
     */
    @Transient
    public boolean isConnectedTo(Vertex vertex) {
        return getConnectedSet().contains(vertex);
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Get the set of Nodes that connect to this vertex in the reverse direction.  In strict graph
     *  terms they are not connected
     *
     * Performs a depth-first traversal  */
    @Transient
    public Set<Vertex> getReverseConnectedSet() {
        Set<Vertex> visited = new HashSet<Vertex>();

        traverseInReverse(this, visited);

        return visited;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Recursive depth-first traversal from a vertex to its adjacent vertices where the adjacent vertex has not already
     *  been visited following the edges with connections directed towards the starting vertex (ie. in reverse)
     *
     * @param vertex
     * @param visited - vertices that are visited are added to this set.  It's used to ensure vertices that have
     *   previously been visited are not visited again
     */
    private void traverseInReverse(Vertex vertex, Set<Vertex> visited) {
        Set<Vertex> adjacents = vertex.getReverseAdjacentSet();

        for (Vertex adjacentVertex : adjacents) {
            if (!visited.contains(adjacentVertex)) {
                visited.add(adjacentVertex);
                traverseInReverse(adjacentVertex, visited);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the set of adjacent Nodes that have an edge to this Node but because they're directed, they can't be
     *  traversed to from this node
     *
     * @return Set of nodes that have a directed edge to this node that can't be traversed
     */
    @Transient
    public Set<Vertex> getReverseAdjacentSet() {
        Set<Vertex> adjacents = new HashSet<Vertex>();
        for (EdgeVertexConnector connector : edgeConnectors) {
            Edge edge = connector.getEdge();
            if (!edge.isTraverseable(this)) {
                adjacents.add(edge.getOtherVertex(this));
            }
        }
        return adjacents;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Visits all of the Vertices connected to this vertex (traverses the graph).  This vertex is not included
     *
     * @param visitor
     */
    public void visitConnectedSet(VertexVisitor<V> visitor) {
        for (Vertex vertex : getConnectedSet()) {
            visitor.visit((V) vertex);
        }
    }
    
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

}
