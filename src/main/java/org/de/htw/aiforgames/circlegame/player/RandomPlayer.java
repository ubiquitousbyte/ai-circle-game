package org.de.htw.aiforgames.circlegame.player;

import lenz.htw.coast.net.NetworkClient;

import java.util.Random;
import java.util.logging.Level;

public class RandomPlayer extends Player {

    private final String server;
    private final String team;

    public RandomPlayer(String server, String team) {
        this.server = server;
        this.team = team;
    }

    @Override
    public String getTeam() {
        return team;
    }

    @Override
    public String getWinMessage() { return "Well played :)"; }

    @Override
    public String getServer() { return server; }

    @Override
    public void play() {
        Random r = new Random();
        float[] position = new float[3];
        while (client.isAlive()) {
            for (int i = 0; i < 3; i++) {
                position[i] = r.nextFloat();
            }
            client.changeMoveDirection(0, position[0], position[1], position[2]);
        }
    }
}
