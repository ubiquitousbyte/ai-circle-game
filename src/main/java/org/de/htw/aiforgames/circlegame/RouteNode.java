package org.de.htw.aiforgames.circlegame;

import lenz.htw.coast.world.GraphNode;

import java.util.Objects;

public class RouteNode implements Comparable<RouteNode> {
    // The state in the state space to which this node corresponds
    GraphNode state;
    // The node in the search tree that generated this node
    RouteNode parent;
    // The action that was applied to the parent to generate this node
    float[] action;
    // The path cost from the initial state to this node
    float pathCost;

    public RouteNode(GraphNode state, RouteNode parent, float[] action, float pathCost) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.pathCost = pathCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteNode routeNode = (RouteNode) o;
        return state.equals(routeNode.state);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(state);
    }

    @Override
    public int compareTo(RouteNode o) {
        return Float.compare(pathCost, o.pathCost);
    }
}
