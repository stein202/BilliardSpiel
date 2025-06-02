package main.java.ui;

import main.java.Brain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Overlay extends JPanel {
    private final BilliardTable billiardTable;

    private String player1 = "Spieler 1";
    private String player2 = "Spieler 2";
    private int[] player1Balls = new int[7];
    private int pottedPlayer1Balls = 0;
    private int pottedPlayer2Balls = 0;
    private int[] player2Balls = new int[7];

    public Overlay(BilliardTable billiardTable) {
        this.billiardTable = billiardTable;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawPlayerField(g2d, true);
        drawPlayerField(g2d, false);
    }

    private void drawPlayerField(Graphics2D g2d, boolean isPlayer1) {
        int margin = BilliardTable.outerMargin;
        int fieldWidth = GameFrame.width / 3;
        int fieldHeight = GameFrame.height - 700 - margin*2;
        int fieldX = isPlayer1 ? margin : GameFrame.width - margin - fieldWidth;
        int fieldY = GameFrame.height - fieldHeight - margin;

        g2d.setColor(new Color(40, 40, 40, 255));
        RoundRectangle2D field = new RoundRectangle2D.Double(fieldX, fieldY, fieldWidth, fieldHeight, 40, 40);
        g2d.fill(field);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        String name = isPlayer1 ? player1 : player2;
        g2d.drawString(name, fieldX + 130, fieldY + 30);

        int playerNum = isPlayer1 ? 1 : 2;

        try {
            BufferedImage photo = ImageIO.read(new File("src/main/ressources/player" + playerNum + ".jpg"));
            g2d.drawImage(photo, fieldX + 20, fieldY + 20, 100, 100, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[] balls = isPlayer1 ? player1Balls : player2Balls;
        int ballRadius = 20;
        int startX = fieldX + 130;
        int startY = fieldY + 50;

        for (int i = 0; i < balls.length; i++) {
            int x = startX + i * (ballRadius + 5);

            if (balls[i] == 1) {
                g2d.setColor(isPlayer1 ? Color.BLUE : Color.RED);
            } else {
                g2d.setColor(new Color(150, 150, 150, 255));
            }

            g2d.fillOval(x, startY, ballRadius, ballRadius);
        }
    }

    private void pottedBallsToBalls() {
        for (int i = 0; i < pottedPlayer1Balls; i++) {
            player1Balls[i] = 1;
        }

        for (int i = 0; i < pottedPlayer2Balls; i++) {
            player2Balls[i] = 1;
        }
    }

    public void updatePottedBalls(BilliardBall ball) {
        if (Brain.isRed(ball.id)){
            pottedPlayer2Balls++;
        } else {
            pottedPlayer1Balls++;
        }
        pottedBallsToBalls();
        repaint();
    }
}
