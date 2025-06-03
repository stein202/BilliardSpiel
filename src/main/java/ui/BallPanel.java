package main.java.ui;

import main.java.Brain;
import main.java.misc.Turn;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BallPanel extends JPanel {
    public final List<BilliardBall> balls;
    public final BilliardTable billiardTable;

    private java.util.Queue<BilliardBall> ballsToRemove = new LinkedList<>();
    private boolean previousAllStill = true;

    private boolean foulOccurred = false;

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
        boolean allStillBeforeUpdate = balls.stream().allMatch(BilliardBall::isBallStill);

        for (BilliardBall ball : balls) {
            ball.updatePosition();
        }

        boolean allStillAfterUpdate = balls.stream().allMatch(BilliardBall::isBallStill);

        for (BilliardBall ball : ballsToRemove) {
            int playerColor = Brain.playerColor[Brain.currentPlayer];

            if (ball.id == 0 ||
                    (playerColor == 0 && Brain.isBlue(ball.id)) ||
                    (playerColor == 1 && Brain.isRed(ball.id))) {
                foulOccurred = true;
            }

            if (playerColor == 0 && Brain.isBlue(ball.id)) {
                foulOccurred = true;
            } else if (playerColor == 1 && Brain.isRed(ball.id)) {
                foulOccurred = true;
            }
        }

        if (foulOccurred) {
            Brain.switchTurn();
            foulOccurred = false;
        } else {
            if (!allStillBeforeUpdate && allStillAfterUpdate && ballsToRemove.isEmpty()) {
                Brain.switchTurn();
            }
        }

        for (BilliardBall ball : ballsToRemove) {
            billiardTable.balls.remove(ball);
        }

        ballsToRemove.clear();
        previousAllStill = allStillAfterUpdate;

        repaint();
    }

    public void removeBall(BilliardBall ball) {
        ballsToRemove.add(ball);
    }
}
