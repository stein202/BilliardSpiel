package main.java.ui;

import main.java.Brain;
import main.java.misc.GameState;
import main.java.misc.Turn;

import javax.swing.*;

public class GameFrame extends JFrame {
    public static final int width = 1400;
    public static final int height = 900;

    public GameFrame() {
        setTitle("Billiard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Verwende JFrame.EXIT_ON_CLOSE
        add(new StartScreen(this));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        Brain.turn = Turn.PLAYER1;
        Brain.gameState = GameState.STARTING;
    }
}
