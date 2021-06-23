package org.de.htw.aiforgames.circlegame;

import lenz.htw.coast.world.GraphNode;

import java.util.*;
import java.util.stream.Collectors;

public class KMeans {

    public static final int FEATURE_VECTOR_LEN = 2;
    private GraphNode[] graph;
    private int[] clusterAssignments;

    public KMeans(GraphNode[] graph) {
        this.graph = graph;
        this.clusterAssignments = new int[graph.length];
    }

    private float[][] X() {
        float[][] X = new float[graph.length][FEATURE_VECTOR_LEN];
        for (int i = 0; i < graph.length; i++) {
            GraphNode node = graph[i];
            X[i][0] = node.owner;
            X[i][1] = node.blocked ? 1f : 0f;
        }
        return X;
    }

    private float[][] sampleRandomCentroids(float[][] X, int k) {
        float[][] centroids = new float[k][FEATURE_VECTOR_LEN];
        Random r = new Random();
        for (int i = 0; i < k; i++) {
            centroids[i] = X[r.nextInt(graph.length)];
        }
        return centroids;
    }

    public float[][] cluster(int k) {
        float[][] X = X();
        float[][] centroids = sampleRandomCentroids(X, k);
        int[] clusterSizes = new int[k];
        double SSE = Double.MAX_VALUE;

        while (true) {
            for (int i = 0; i < X.length; i++) {
                float[] example = X[i];
                double minDist = Double.MAX_VALUE;
                for (int j = 0; j < centroids.length; j++) {
                    double dist = Distance.euclidean(example, centroids[j]);
                    if (dist < minDist) {
                        minDist = dist;
                        clusterAssignments[i] = j;
                    }
                }
            }

            for (int i = 0; i < k; i++) {
                clusterSizes[i] = 0;
                for (int j = 0; j < FEATURE_VECTOR_LEN; j++) {
                    centroids[i][j] = 0;
                }
            }
            for (int i = 0; i < clusterAssignments.length; i++) {
                clusterSizes[clusterAssignments[i]]++;
                for (int j = 0; j < FEATURE_VECTOR_LEN; j++) {
                    centroids[clusterAssignments[i]][j] += X[i][j];
                }
            }

            for (int i = 0; i < k; i++) {
                if (clusterSizes[i] == 0) {
                    continue;
                }
                for (int j = 0; j < FEATURE_VECTOR_LEN; j++) {
                    centroids[i][j] /= clusterSizes[i];
                }
            }

            double newSSE = 0;
            for (int i = 0; i < X.length; i++) {
                newSSE += Distance.euclidean(centroids[clusterAssignments[i]], X[i]);
            }
            if (SSE-newSSE <= 0) {
                break;
            }
            SSE = newSSE;
        }
        System.out.println(Arrays.deepToString(centroids));
        return centroids;
    }

    public GraphNode sampleNodeFromCluster(int k) {
        List<GraphNode> possibleSamples = new ArrayList<>();
        for (int i = 0; i < clusterAssignments.length; i++) {
            if (clusterAssignments[i] == k) {
                possibleSamples.add(graph[i]);
            }
        }
        if (possibleSamples.isEmpty()) {
            return graph[0];
        }
        Random r = new Random();
        return possibleSamples.get(r.nextInt(possibleSamples.size()));
    }
}
