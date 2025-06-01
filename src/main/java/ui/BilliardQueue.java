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
    public int angle2;
    public BilliardBall ball;
    public final int queueLength = 500;
    public final static double masse = 0.5;


    private int lastMouseX = -1;
    private int lastMouseY = -1;
    private final long lastUpdateTime = 0;

    private double cachedCos = 0;
    private double cachedSin = 0;
    private int cachedAngle = -1;
    double newcachedCos = 0;
    double newcachedSin = 0;
    int cachedPullBack;

    private int pullBackDistance = 0;
    private Timer chargeTimer;

    public BilliardQueue() {
        this.turn = Turn.PLAYER1;
        this.angle = 0;

        setOpaque(false);

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
        if (!ball.isBallStill()) return;

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

        int startX = (int) (mittelPunktX - (radius / 2 + 4 + pullBackDistance) * cachedCos);
        int startY = (int) (mittelPunktY - (radius / 2 + 4 + pullBackDistance) * cachedSin);
        int endX = (int) (startX - queueLength * cachedCos);
        int endY = (int) (startY - queueLength * cachedSin);

        g2d.setColor(new Color(139, 69, 19));
        g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(startX, startY, endX, endY);


        int linex = (int) (mittelPunktX + (radius / 2 + 4) * newcachedCos);
        int liney = (int) (mittelPunktY - (radius / 2 + 4) * newcachedSin);


        double dx = newcachedCos;
        double dy = -newcachedSin;


        int minX = BilliardTable.tableX + radius - 8;
        int maxX = BilliardTable.pocketRightX + radius / 4;
        int minY1 = BilliardTable.tableY + radius - 8;
        int maxY = BilliardTable.pocketBottomY + 8;

        int collisionX = linex;
        int collisionY = liney;

        int maxLineLength = 1000;
        int step = 1;

        for (int i = 0; i < maxLineLength; i += step) {
            int x = (int) (linex + i * dx);
            int y = (int) (liney + i * dy);


            if (x < minX || x > maxX || y < minY1 || y > maxY) {
                collisionX = x;
                collisionY = y;
                break;
            }

        }

        g2d.setColor(new Color(255, 255, 255)); // Weiße Linie
        g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(linex, liney, collisionX, collisionY);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && ball.isBallStill()) {
            if (chargeTimer != null && chargeTimer.isRunning()) {
                chargeTimer.stop();
            }

            pullBackDistance = 0;

            chargeTimer = new Timer(20, evt -> {
                pullBackDistance += 6;
                if (pullBackDistance > 100) {
                    pullBackDistance = 100;
                    chargeTimer.stop();
                }
                repaint();
            });
            chargeTimer.start();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && ball.isBallStill()) {
            if (chargeTimer != null && chargeTimer.isRunning()) {
                chargeTimer.stop();
                chargeTimer = null;
            }
            cachedPullBack = pullBackDistance;
            pullBackDistance = 0;
            repaint();

            ball.calcImpulse(cachedPullBack, angle);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

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
        int newAngle2 = (int) (Math.toDegrees(Math.atan2(deltaY, deltaX)) * -1);
        if (newAngle < 0) newAngle += 360;

        angle = newAngle;
        angle2 = newAngle2;

        double radians = Math.toRadians(angle);
        cachedCos = Math.cos(radians);
        cachedSin = Math.sin(radians);
        cachedAngle = angle;

        //Für Linie hinter dem Queue
        double newradians = Math.toRadians(angle2);
        newcachedCos = Math.cos(newradians);
        newcachedSin = Math.sin(newradians);
        int newcachedAngle = angle2;


        repaint();
    }
}
