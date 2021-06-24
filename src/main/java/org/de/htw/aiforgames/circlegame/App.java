package org.de.htw.aiforgames.circlegame;

import org.de.htw.aiforgames.circlegame.player.Player;
import org.de.htw.aiforgames.circlegame.player.RandomPlayer;
import org.de.htw.aiforgames.circlegame.player.SmartPlayer;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws InterruptedException {
        String[] teamNames = {"random ninja1", "random ninja2", "random ninja3"};

        Thread[] threads = new Thread[3];
        threads[0] = new Thread(new SmartPlayer("127.0.0.1", teamNames[0]));
        threads[0].start();
        threads[1] = new Thread(new SmartPlayer("127.0.0.1", teamNames[1]));
        threads[1].start();
        threads[2] = new Thread(new SmartPlayer("127.0.0.1", teamNames[2]));
        threads[2].start();

        for (Thread t : threads) {
            t.join();
        }
    }
}
