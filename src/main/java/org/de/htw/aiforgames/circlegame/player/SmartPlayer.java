package org.de.htw.aiforgames.circlegame.player;

import lenz.htw.coast.world.GraphNode;
import org.de.htw.aiforgames.circlegame.CircleSearchProblem;
import org.de.htw.aiforgames.circlegame.KMeans;
import org.de.htw.aiforgames.circlegame.UniformCostSearchStrategy;

import java.util.*;
import java.util.logging.Level;

import static java.lang.Thread.sleep;

public class SmartPlayer extends Player {

    private static final int PLAYER_COUNT = 3;
    private int player;
    private final String server;
    private final String team;

    public SmartPlayer(String server, String team) {
        this.server = server;
        this.team = team;
    }

    @Override
    public String getTeam() {
        return team;
    }

    @Override
    public String getWinMessage() {
        return "Well played :)";
    }

    @Override
    public String getServer() {
        return server;
    }

    private float[][] getBotPositions(int player) {
        float[][] botPositions = new float[3][3];
        for (int i = 0; i < 3; i++) {
            botPositions[i] = client.getBotPosition(player, i);
        }
        return botPositions;
    }

    public int[] getEnemyIds() {
        if (player == 0) {
            return new int[]{1, 2};
        }
        else if (player == 1) {
            return new int[]{0, 2};
        }
        else {
            return new int[]{0, 1};
        }
    }

    private GraphNode[] getBotPositions(GraphNode[] graph) {
        GraphNode[] result = new GraphNode[3];
        float[][] positionVectors = getBotPositions(player);
        int count = 0;
        for (GraphNode node : graph) {
           for (int i = 0; i < 3; i++) {
                if (Math.abs(positionVectors[i][0]-node.x) < 0.1
                        && Math.abs(positionVectors[i][1]-node.y) < 0.1
                        && Math.abs(positionVectors[i][2]-node.z) < 0.1) {
                    result[i] = node;
                    count++;
                }
            }
            if (count == 3) {
                break;
            }
        }
        return (count != 3) ? null : result;
    }

    @Override
    public void play() {
        player = client.getMyPlayerNumber();
        long seed = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        long end = start + 200;
        float[] bot0Target;
        float[] bot1Target;
        while (client.isAlive()) {
            if (System.currentTimeMillis() > end) {
                GraphNode[] nodes = client.getGraph();
                GraphNode[] positions = getBotPositions(nodes);
                KMeans kmeans = new KMeans(nodes, seed);
                float[][] centroids = kmeans.cluster(3);
                bot1Target = kmeans.sampleTargetFromClusterWithMostBlockedElements(centroids);
                bot0Target = kmeans.sampleTargetFromClusterWIthMostFreePixels(centroids);
                for (int i = 0; i < 3; i++) {
                    if (positions != null && positions[i] != null) {
                        CircleSearchProblem problem = new CircleSearchProblem(positions[i], (i == 1) ? bot1Target : bot0Target, i, player);
                        UniformCostSearchStrategy strategy = new UniformCostSearchStrategy();
                        for (float[] action : strategy.search(problem)) {
                            client.changeMoveDirection(i, action[0], action[1], action[2]);
                        }
                    }
                }
                start = System.currentTimeMillis();
                end = start + 200;
            }

        }
    }
}
