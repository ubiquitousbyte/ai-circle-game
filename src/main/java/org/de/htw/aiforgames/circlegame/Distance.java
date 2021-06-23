package org.de.htw.aiforgames.circlegame;

public class Distance {

    public static double euclidean(float[] p1, float[] p2) {
        assert (p1.length == p2.length);
        float acc = 0f;
        for (int i = 0; i < p1.length; i++) {
            acc += Math.pow(p1[i] - p2[i], 2);
        }
        return Math.sqrt(acc);
    }
}
