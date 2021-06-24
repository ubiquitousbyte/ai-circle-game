package org.de.htw.aiforgames.circlegame;

import lenz.htw.coast.world.GraphNode;

import java.util.*;

public class UniformCostSearchStrategy implements SearchStrategy<GraphNode, float[]> {

    @Override
    public List<float[]> search(SearchProblem<GraphNode, float[]> problem) {
        Queue<RouteNode> frontier = new PriorityQueue<>();
        Set<RouteNode> explored = new HashSet<>();
        frontier.add(new RouteNode(problem.startState(), null, null, 0));

        while (! frontier.isEmpty()) {
            RouteNode current = frontier.poll();
            if (explored.contains(current)) continue;
            if (problem.isGoal(current.state)) return solution(current);
            explored.add(current);
            frontier.addAll(expand(problem, current));
        }
        return new ArrayList<>();
    }

    private static Set<RouteNode> expand(SearchProblem<GraphNode, float[]> problem, RouteNode current) {
        HashSet<RouteNode> successors = new HashSet<>();
        for (float[] action : problem.actions(current.state)) {
            RouteNode child = child(problem, current, action);
            successors.add(child);
        }
        return successors;
    }

    private static RouteNode child(SearchProblem<GraphNode, float[]> problem, RouteNode parent, float[] action) {
        GraphNode successor = problem.successor(parent.state, action);
        float pathCost = parent.pathCost + problem.stepCost(parent.state, successor);
        return new RouteNode(successor, parent, action, pathCost);
    }

    private List<float[]> solution(RouteNode target) {
        List<float[]> path = new ArrayList<>();
        while (target != null && target.action != null) {
            path.add(target.action);
            target = target.parent;
        }
        return path;
    }
}
