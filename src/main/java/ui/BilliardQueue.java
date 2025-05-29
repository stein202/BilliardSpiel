package main.java.ui;

import main.java.misc.Turn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BilliardQueue extends JPanel implements MouseListener, MouseMotionListener {
    public Turn turn;
    public int angle;
    public BilliardBall ball;
    public final int queueLength = 500;

    private int lastMouseX = -1;
    private int lastMouseY = -1;
    private long lastUpdateTime = 0;

    private double cachedCos = 0;
    private double cachedSin = 0;
    private int cachedAngle = -1;

    private int pullBackDistance = 0;
    private Timer chargeTimer;

    public BilliardQueue() {
        this.turn = Turn.PLAYER1;
        this.angle = 0;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setBall(BilliardBall b) {
        this.ball = b;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (ball == null) return;

        if (cachedAngle != angle) {
            double winkel = Math.toRadians(angle);
            cachedCos = Math.cos(winkel);
            cachedSin = Math.sin(winkel);
            cachedAngle = angle;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int mittelPunktX = (int) ball.x + (BilliardBall.radius >> 1);
        int mittelPunktY = (int) ball.y + (BilliardBall.radius >> 1);

        final int radius = BilliardBall.radius;

        int startX = (int) (mittelPunktX - (radius/2+4 + pullBackDistance) * cachedCos);
        int startY = (int) (mittelPunktY - (radius/2+4 + pullBackDistance) * cachedSin);
        int endX = (int) (startX - queueLength * cachedCos);
        int endY = (int) (startY - queueLength * cachedSin);

        g2d.setColor(new Color(139, 69, 19));
        g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(startX, startY, endX, endY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            pullBackDistance = 0;

            chargeTimer = new Timer(20, evt -> {
                pullBackDistance += 3;
                if (pullBackDistance > 100) pullBackDistance = 100;
                repaint();
            });
            chargeTimer.start();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (chargeTimer != null) {
                chargeTimer.stop();
            }

            pullBackDistance = 0;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (ball == null) return;

        int currentX = e.getX();
        int currentY = e.getY();

        lastMouseX = currentX;
        lastMouseY = currentY;

        int ballCenterX = (int) ball.x + (BilliardBall.radius >> 1);
        int ballCenterY = (int) ball.y + (BilliardBall.radius >> 1);

        int deltaX = currentX - ballCenterX;
        int deltaY = currentY - ballCenterY;

        int newAngle = (int) Math.toDegrees(Math.atan2(deltaY, deltaX));
        if (newAngle < 0) newAngle += 360;

        angle = newAngle;

        double radians = Math.toRadians(angle);
        cachedCos = Math.cos(radians);
        cachedSin = Math.sin(radians);
        cachedAngle = angle;

        repaint();
    }
}
