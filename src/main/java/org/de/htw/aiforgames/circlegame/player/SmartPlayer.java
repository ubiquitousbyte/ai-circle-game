package org.de.htw.aiforgames.circlegame.player;

import lenz.htw.coast.world.GraphNode;
import org.de.htw.aiforgames.circlegame.CircleSearchProblem;
import org.de.htw.aiforgames.circlegame.KMeans;
import org.de.htw.aiforgames.circlegame.SearchProblem;
import org.de.htw.aiforgames.circlegame.UniformCostSearchStrategy;

import java.util.*;
import java.util.logging.Level;

import static java.lang.Thread.sleep;

public class SmartPlayer extends Player {

    private static final int PLAYER_COUNT = 3;

    private int player;

    private GraphNode[] startNodes;

    private final String server;
    private final String team;

    public SmartPlayer(String server, String team) {
        this.server = server;
        this.team = team;
        startNodes = new GraphNode[3];
    }

    @Override
    public String getTeam() {
        return team;
    }

    @Override
    public String getWinMessage() {
        return "Well player :)";
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

    private GraphNode[][] getInitialBotPositions() {
        int[] enemyIds = getEnemyIds();
        float[][] firstEnemyPositions = getBotPositions(enemyIds[0]);
        float[][] secondEnemyPositions = getBotPositions(enemyIds[1]);
        float[][] myPlayerPositions = getBotPositions(player);
        GraphNode[][] result = new GraphNode[3][3];

        int count = 0;
        for (GraphNode node : client.getGraph()) {
            for (int i = 0; i < 3; i++) {
                float[] nodePosition = new float[]{node.x, node.y, node.z};
                if (Arrays.equals(firstEnemyPositions[i], nodePosition)) {
                    result[enemyIds[0]][i] = node;
                    count++;
                }
                else if (Arrays.equals(secondEnemyPositions[i], nodePosition)) {
                    result[enemyIds[1]][i] = node;
                    count++;
                }
                else if (Arrays.equals(myPlayerPositions[i], nodePosition)) {
                    result[player][i] = node;
                    count++;
                }
            }
            if (count == 9) {
                break;
            }
        }
        return result;
    }

    private GraphNode[] getBotPositions(GraphNode[] graph) {
        GraphNode[] result = new GraphNode[3];
        float[][] positionVectors = getBotPositions(player);
        int count = 0;
        for (GraphNode node : graph) {
            float[] nodePosition = new float[]{node.x, node.y, node.z};
            for (int i = 0; i < 3; i++) {
                if (Arrays.equals(positionVectors[i], nodePosition)) {
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
        GraphNode[] graph = client.getGraph();
        GraphNode[] botPositions = getBotPositions(graph);
        if (botPositions == null) {
            logger.log(Level.WARNING, "Cannot determine bot positions. Exiting.");
            return;
        }
        KMeans kmeans = new KMeans(client.getGraph());
        float[][] centroids = kmeans.cluster(8);
        long start = System.currentTimeMillis();
        long end = start + 2*1000;
        while (client.isAlive()) {
            if (System.currentTimeMillis() > end) {
                kmeans = new KMeans(client.getGraph());
                centroids = kmeans.cluster(8);
                start = System.currentTimeMillis();
                end = start + 2*1000;
            }
           /* for (int i = 0; i < 3; i++) {
                GraphNode target = kmeans.sampleNodeFromCluster(i);
                CircleSearchProblem c = new CircleSearchProblem(botPositions[i], new float[]{target.x, target.y, target.z}, i, player);
                UniformCostSearchStrategy s = new UniformCostSearchStrategy();
                try {
                    for (float[] action : s.search(c)) {
                        client.changeMoveDirection(i, action[0], action[1], action[2]);
                    }
                }
                catch (NullPointerException e) {
                    continue;
                }
                botPositions[i] = target;
            }*/
        }
    }
}
