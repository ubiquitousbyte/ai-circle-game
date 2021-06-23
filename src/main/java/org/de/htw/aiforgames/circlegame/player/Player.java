package org.de.htw.aiforgames.circlegame.player;

import lenz.htw.coast.net.NetworkClient;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Player implements Runnable {

    protected NetworkClient client;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    public abstract String getTeam();

    public abstract String getWinMessage();

    public abstract String getServer();

    public void connect() { client = new NetworkClient(getServer(), getTeam(), getWinMessage()); }

    public abstract void play();

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
