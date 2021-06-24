package org.de.htw.aiforgames.circlegame;

import lenz.htw.coast.world.GraphNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CircleSearchProblem implements SearchProblem<GraphNode, float[]> {

    private final GraphNode from;
    private final float[] to;
    private final int bot;
    private final int player;

    public CircleSearchProblem(GraphNode from, float[] to, int bot, int player) {
        assert(to.length == 3);
        this.from = from;
        this.to = to;
        this.bot = bot;
        this.player = player;
    }

    @Override
    public GraphNode startState() { return from; }

    @Override
    public List<float[]> actions(GraphNode state) {
        List<float[]> moveDirections = new ArrayList<>(state.neighbors.length);
        for (GraphNode neighbour : state.neighbors) {
            moveDirections.add(new float[] {
                    neighbour.x - state.x,
                    neighbour.y - state.y,
                    neighbour.z - state.z
            });
        }
        return moveDirections;
    }

    @Override
    public boolean isGoal(GraphNode state) { return state.x == to[0] && state.y == to[1] && state.z == to[2]; }

    @Override
    public GraphNode successor(GraphNode state, float[] action) {
        float x = state.x + action[0];
        float y = state.y + action[1];
        float z = state.z + action[2];

        for (GraphNode neighbour : state.neighbors) {
            if (Math.abs(neighbour.x-x) < 0.00001 && Math.abs(neighbour.y-y) < 0.00001 && Math.abs(neighbour.z-z) < 0.00001) {
                return neighbour;
            }
        }
        return null;
    }

    @Override
    public float stepCost(GraphNode state, GraphNode successor) {
        float w1, w2, w3, w4, w5;
        w1 = w2 = w3 = w4 = w5 = 0f;
        switch (bot) {
            case 0:
                if (successor.owner == (player+1)) {
                    w1 += 3.5f;
                }
                if (successor.blocked) {
                    w2 += 350f;
                }
                break;
            case 1:
                if (successor.owner == (player+1)) {
                    w1 += 2.5f;
                }
                break;
            case 2:
                if (successor.owner == (player+1)) {
                    w1 += 7f;
                }
                if (successor.blocked) {
                    w2 += 750f;
                }
                break;
            default:
                w1 = 1f;
        }
        return w1 + w2 + w3 + w4 + w5;
    }
}
