package org.de.htw.aiforgames.circlegame.player;

import java.awt.image.BufferedImage;

public abstract class Player implements Runnable {

    /**
     * @return the name of the player's team
     */
    public abstract String getTeam();

    /**
     * @return an icon that identifies the player
     */
    public abstract BufferedImage getIcon();

    public abstract void play();
}
