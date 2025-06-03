package main.java.ui;

import main.java.Brain;
import main.java.misc.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JPanel implements ActionListener{
    private final JFrame fenster;
    private final JPanel einstellungenPanel;


    private final JButton zurueckButton;
    private final JButton einstellungenButton;
    private final JButton startButton;
    private final JButton beendenButton;

    public StartScreen(GameFrame g) {
        this.fenster = g;
        setPreferredSize(new Dimension(GameFrame.width, GameFrame.height));
        setBackground(Color.GRAY);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        JLabel titel = new JLabel("SPIEL MENU");
        titel.setFont(new Font("Arial", Font.BOLD, 24));
        titel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Abstand hinzufügen
        add(Box.createVerticalStrut(50));
        add(titel);
        add(Box.createVerticalStrut(30));

        // Buttons mit ActionListener
        startButton = new JButton("START");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(200, 40));

        startButton.addActionListener(this);

        einstellungenButton = new JButton("EINSTELLUNGEN");
        einstellungenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        einstellungenButton.setMaximumSize(new Dimension(200, 40));

        beendenButton = new JButton("BEENDEN");
        beendenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        beendenButton.setMaximumSize(new Dimension(200, 40));

        beendenButton.addActionListener(this);

        einstellungenButton.addActionListener(this);

        // Alles zum Haupt Panel hinzufügen
        add(startButton);
        add(Box.createVerticalStrut(20));
        add(einstellungenButton);
        add(Box.createVerticalStrut(20));
        add(beendenButton);

        // Einstellungen Panel erstellen
        einstellungenPanel = new JPanel();
        einstellungenPanel.setBackground(Color.GREEN);
        einstellungenPanel.setLayout(null);

        // Lautstärke Label und Slider
        JLabel lautstaerkeLabel = new JLabel("Lautstärke:");
        lautstaerkeLabel.setBounds(50, 50, 100, 30);
        lautstaerkeLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JSlider lautstaerkeSlider = new JSlider(0, 100, 50);
        lautstaerkeSlider.setBounds(150, 50, 200, 30);

        // Musik Label und Slider
        JLabel musikLabel = new JLabel("Musik:");
        musikLabel.setBounds(50, 100, 100, 30);
        musikLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JSlider musikSlider = new JSlider(0, 100, 50);
        musikSlider.setBounds(150, 100, 200, 30);

        // Zurück Button
        zurueckButton = new JButton("ZURÜCK");
        zurueckButton.setBounds(200, 180, 100, 40);
        zurueckButton.addActionListener(this);

        // Alles zum Einstellungen Panel hinzufügen
        einstellungenPanel.add(lautstaerkeLabel);
        einstellungenPanel.add(lautstaerkeSlider);
        einstellungenPanel.add(musikLabel);
        einstellungenPanel.add(musikSlider);
        einstellungenPanel.add(zurueckButton);


        fenster.add(this);

        fenster.setVisible(true);
    }

    public void showEinstellungen() {
        fenster.getContentPane().removeAll();
        fenster.add(einstellungenPanel);
        fenster.revalidate();
        fenster.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == zurueckButton) {
            fenster.getContentPane().removeAll();
            fenster.add(this);
            fenster.revalidate();
            fenster.repaint();
        } else if (e.getSource() == einstellungenButton) {
            showEinstellungen();
        } else if (e.getSource() == startButton) {
            fenster.getContentPane().removeAll();
            fenster.add(new BilliardTable(fenster));
            fenster.revalidate();
            fenster.repaint();
            Brain.gameState = GameState.AWAITING_SHOT;
        } else if (e.getSource() == beendenButton) {
            fenster.dispose();
        }
    }
}
