package main.java.ui;

import main.java.misc.Turn;

import java.awt.*;

public class BilliardQueue {
    public Turn turn;
    public int angle;
    public BilliardBall ball;

    public BilliardQueue() {
        this.turn = Turn.PLAYER1;
        this.angle = 270;
    }

    public void switchPlayerTurn() {
        if (turn == Turn.PLAYER1) {
            this.turn = Turn.PLAYER2;
        }else {
            this.turn = Turn.PLAYER1;
        }
    }

    public void setBall(BilliardBall b) {
        this.ball = b;
    }

    public void draw(Graphics2D g) {
        Polygon queue = new Polygon();
        double x1 = ball.x - BilliardBall.radius;
        double x2 = ball.x - 800;

        double y1 = (ball.y + BilliardBall.radius/2) + BilliardBall.radius/4;
        double y2 = (ball.y + BilliardBall.radius/2) - BilliardBall.radius/4;

        queue.addPoint((int) x1, (int) y1);
        queue.addPoint((int) x2, (int) y1);
        queue.addPoint((int) x2, (int) y2);
        queue.addPoint((int) x1, (int) y2);

        g.setColor(Color.BLACK);
        g.fill(queue);
    }
}
