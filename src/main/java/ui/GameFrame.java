package main.java.ui;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Billiard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(new BilliardTable());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
