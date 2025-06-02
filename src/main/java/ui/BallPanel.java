package main.java.ui;

import main.java.Brain;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BallPanel extends JPanel {
    public final List<BilliardBall> balls;
    public final BilliardTable billiardTable;

    private java.util.Queue<BilliardBall> ballsToRemove = new LinkedList<>();
    private boolean previousAllStill = true;

    Timer physicsTimer = new Timer(8, e -> updateAllBalls());

    public BallPanel(List<BilliardBall> balls, BilliardTable billiardTable) {
        this.balls = balls;
        this.billiardTable = billiardTable;
        physicsTimer.start();
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

    public void updateAllBalls() {
        boolean allStill = balls.stream().allMatch(BilliardBall::isBallStill);

        if (!previousAllStill && allStill) {
            Brain.switchTurn();
        }

        previousAllStill = allStill;

        for (BilliardBall ball : balls) {
            ball.updatePosition();
        }

        for (BilliardBall ball : ballsToRemove) {
            billiardTable.balls.remove(ball);
        }

        repaint();
    }

    public void removeBall(BilliardBall ball) {
        ballsToRemove.add(ball);
    }
}
