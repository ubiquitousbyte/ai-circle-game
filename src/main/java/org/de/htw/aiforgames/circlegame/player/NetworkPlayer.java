package org.de.htw.aiforgames.circlegame.player;

import lenz.htw.coast.net.NetworkClient;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkPlayer extends Player {

    private final String server;
    private final String team;
    private final BufferedImage icon;
    private NetworkClient client;

    private final Logger logger = Logger.getLogger(getClass().getName());


    public NetworkPlayer(String server, String team, BufferedImage image) {
        this.server = server;
        this.team = team;
        this.icon = image;
    }

    private void connect() {
        client = new NetworkClient(server, team, "Well played :)");
    }

    @Override
    public String getTeam() {
        return team;
    }

    @Override
    public BufferedImage getIcon() {
        return icon;
    }

    @Override
    public void play() {
        return;
    }

    @Override
    public void run() {
        try {
            this.connect();
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return;
        }
        play();
    }
}
