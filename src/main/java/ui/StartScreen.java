package main.java.ui;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JPanel {

    public StartScreen(GameFrame g) {
        setPreferredSize(new Dimension(GameFrame.width, GameFrame.height));
        setVisible(true);

        JButton jButton = new JButton("Start");
        jButton.addActionListener(e -> {
            // KORREKTE COMPONENT-VERWALTUNG
            g.getContentPane().removeAll(); // Alle Components entfernen
            g.add(new BilliardTable(g));
            g.revalidate(); // Layout neu berechnen
            g.repaint();    // Neu zeichnen
        });
        add(jButton);
    }
}
