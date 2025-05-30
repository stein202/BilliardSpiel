package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BallPanel extends JPanel {
    private final List<BilliardBall> balls;

    public BallPanel(List<BilliardBall> balls) {
        this.balls = balls;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (BilliardBall ball: balls) {
            ball.drawBall(g2d);
        }
    }
}
