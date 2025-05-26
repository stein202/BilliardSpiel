import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen {

    static JFrame fenster;
    static JPanel hauptPanel;
    static JPanel einstellungenPanel;

    public static void main(String[] args) {

        fenster = new JFrame("Startscreen");
        fenster.setSize(500, 400);
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        hauptPanel = new JPanel();
        hauptPanel.setBackground(Color.GREEN);
        hauptPanel.setLayout(new BoxLayout(hauptPanel, BoxLayout.Y_AXIS));


        JLabel titel = new JLabel("SPIEL MENU");
        titel.setFont(new Font("Arial", Font.BOLD, 24));
        titel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Abstand hinzufügen
        hauptPanel.add(Box.createVerticalStrut(50));
        hauptPanel.add(titel);
        hauptPanel.add(Box.createVerticalStrut(30));

        // Buttons mit ActionListener
        JButton startButton = new JButton("START");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(200, 40));

        JButton einstellungenButton = new JButton("EINSTELLUNGEN");
        einstellungenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        einstellungenButton.setMaximumSize(new Dimension(200, 40));

        JButton beendenButton = new JButton("BEENDEN");
        beendenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        beendenButton.setMaximumSize(new Dimension(200, 40));

        einstellungenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEinstellungen();
            }
        });

        // Alles zum Haupt Panel hinzufügen
        hauptPanel.add(startButton);
        hauptPanel.add(Box.createVerticalStrut(20));
        hauptPanel.add(einstellungenButton);
        hauptPanel.add(Box.createVerticalStrut(20));
        hauptPanel.add(beendenButton);

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
        JButton zurueckButton = new JButton("ZURÜCK");
        zurueckButton.setBounds(200, 180, 100, 40);
        zurueckButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fenster.getContentPane().removeAll();
                fenster.add(hauptPanel);
                fenster.revalidate();
                fenster.repaint();
            }
        });

        // Alles zum Einstellungen Panel hinzufügen
        einstellungenPanel.add(lautstaerkeLabel);
        einstellungenPanel.add(lautstaerkeSlider);
        einstellungenPanel.add(musikLabel);
        einstellungenPanel.add(musikSlider);
        einstellungenPanel.add(zurueckButton);


        fenster.add(hauptPanel);

        
        fenster.setVisible(true);
    }

    public static void showEinstellungen() {
        fenster.getContentPane().removeAll();
        fenster.add(einstellungenPanel);
        fenster.revalidate();
        fenster.repaint();
    }
}
