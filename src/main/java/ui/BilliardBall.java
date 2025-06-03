package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class BilliardBall {
    public static final int radius = 30;
    public static final double masse = 0.17;
    public int id;
    public double orgX, orgY;
    public double x, y;
    public double vx, vy;

    public BallPanel ballPanel;
    public Ellipse2D ball;
    private BilliardTable billiardTable;

    // 0 = weiße Kugel, 5 = schwarze Kugel, 1-7 Team Rot, 9-15 Team Blau

    public BilliardBall(int id, double x, double y) {
        this.id = id;
        this.orgX = x;
        this.orgY = y;
        this.x = x;
        this.y = y;
    }

    public void setBallPanel(BallPanel ballPanel) {
        this.ballPanel = ballPanel;
    }

    public void setBilliardTable(BilliardTable billiardTable) {
        this.billiardTable = billiardTable;
    }

    public void drawBall(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (id == 0) g2d.setColor(Color.WHITE);
        else if (id == 5) g2d.setColor(Color.BLACK);
        else if (id >= 1 && id <= 7) g2d.setColor(Color.RED);
        else if (id >= 9 && id <= 15) g2d.setColor(Color.BLUE);
        ball = new Ellipse2D.Double(x, y, radius, radius);
        g2d.fill(ball);
    }

    public void calcImpulse(int power, double angle) {
        double v = ((2 * BilliardQueue.masse) / (BilliardBall.masse + BilliardQueue.masse) * power * 0.3);
        vx = v * Math.cos(Math.toRadians(angle));
        vy = v * Math.sin(Math.toRadians(angle));
    }

    public void updatePosition() {
        this.x += vx;
        this.y += vy;

        checkCushionCollision(billiardTable.cushion);

        vx *= 0.98;
        vy *= 0.98;

        checkBallCollision();

        if (Math.abs(vx) < 0.05) vx = 0;
        if (Math.abs(vy) < 0.05) vy = 0;

        if (ballPanel != null) {
            ballPanel.repaint();
        }
    }

    public boolean isBallStill() {
        return (Math.abs(vx) < 0.05 || Math.abs(vy) < 0.05);
    }

    public void checkCushionCollision(Area cushion) {
        if (billiardTable.isInPocket(this)) {
            vx = 0;
            vy = 0;
            if (this.id == 0) {
                x = orgX;
                y = orgY;
            } else {
                ballPanel.removeBall(this);
            }
            return;
        }

        Ellipse2D ballShape = new Ellipse2D.Double(x, y, radius, radius);
        Area ballArea = new Area(ballShape);

        Area intersection = new Area(cushion);
        intersection.intersect(ballArea);

        if (!intersection.isEmpty()) {
            // Durchlaufe die Kanten des Cushions und prüfe die nächste Kante zur Ballmitte
            PathIterator pi = cushion.getPathIterator(null, 1.0);
            double[] coords = new double[6];
            double startX = 0, startY = 0;
            double lastX = 0, lastY = 0;

            Line2D nearestLine = null;
            double minDistance = Double.MAX_VALUE;
            Point2D ballCenter = new Point2D.Double(x + radius / 2.0, y + radius / 2.0);

            while (!pi.isDone()) {
                int type = pi.currentSegment(coords);

                switch (type) {
                    case PathIterator.SEG_MOVETO:
                        lastX = startX = coords[0];
                        lastY = startY = coords[1];
                        break;
                    case PathIterator.SEG_LINETO:
                        Line2D line = new Line2D.Double(lastX, lastY, coords[0], coords[1]);
                        Point2D closest = getClosestPointOnLine(line, ballCenter);
                        double distance = ballCenter.distance(closest);

                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestLine = line;
                        }

                        lastX = coords[0];
                        lastY = coords[1];
                        break;
                    case PathIterator.SEG_CLOSE:
                        Line2D closingLine = new Line2D.Double(lastX, lastY, startX, startY);
                        Point2D closePoint = getClosestPointOnLine(closingLine, ballCenter);
                        double distClose = ballCenter.distance(closePoint);

                        if (distClose < minDistance) {
                            minDistance = distClose;
                            nearestLine = closingLine;
                        }
                        break;
                }

                pi.next();
            }

            if (nearestLine != null) {
                reflectBallFromLine(nearestLine);
            }


        }
    }

    private void checkBallCollision() {
        if (vx == 0 && vy == 0) return;

        for (BilliardBall ball : billiardTable.balls) {
            if (ball == this) continue;
            double dx =  (ball.x + radius / 2) - (this.x + radius / 2);
            double dy =  (ball.y + radius / 2) - (this.y + radius / 2);
            double distance = Math.hypot(dx, dy);

            if (distance < radius) {
                resolveBallCollision(ball);
            }
        }
    }

    private void resolveBallCollision(BilliardBall ball) {

        // Berechne die Zentren der beiden Bälle
        double thisX = this.x + radius / 2.0;
        double thisY = this.y + radius / 2.0;
        double otherX = ball.x + radius / 2.0;
        double otherY = ball.y + radius / 2.0;

        // Berechne den Kollisionsvektor (von diesem Ball zum anderen)
        double dx = otherX - thisX;
        double dy = otherY - thisY;
        double distance = Math.hypot(dx, dy);

        if (distance == 0) {
            dx = 1;
            dy = 0;
            distance = 1;
        }

        // Normalisiere den Kollisionsvektor
        double nx = dx / distance;
        double ny = dy / distance;

        // Berechne die relativen Geschwindigkeiten
        double relativeVx = this.vx - ball.vx;
        double relativeVy = this.vy - ball.vy;

        // Berechne die relative Geschwindigkeit in Richtung der Kollisionsnormale
        double relativeSpeed = relativeVx * nx + relativeVy * ny;

        // Kollision nur behandeln, wenn sich die Bälle aufeinander zu bewegen
        if (relativeSpeed <= 0) return;

        // Berechne den Impulsübertrag (bei gleicher Masse vereinfacht sich die Formel)
        double impulse = 2 * relativeSpeed / 2; // masse1 = masse2 = masse

        // Aktualisiere die Geschwindigkeiten beider Bälle
        this.vx -= impulse * nx;
        this.vy -= impulse * ny;
        ball.vx += impulse * nx;
        ball.vy += impulse * ny;


        double overlap = radius - distance;
        if (overlap > 0) {
            double separationDistance = overlap / 2.0 + 1;

            this.x -= separationDistance * nx;
            this.y -= separationDistance * ny;
            ball.x += separationDistance * nx;
            ball.y += separationDistance * ny;
        }
    }

    private Point2D getClosestPointOnLine(Line2D line, Point2D point) {
        double x1 = line.getX1(), y1 = line.getY1();
        double x2 = line.getX2(), y2 = line.getY2();
        double px = point.getX(), py = point.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;

        if (dx == 0 && dy == 0) return new Point2D.Double(x1, y1);

        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        return new Point2D.Double(x1 + t * dx, y1 + t * dy);
    }

    private void reflectBallFromLine(Line2D line) {
        double dx = line.getX2() - line.getX1();
        double dy = line.getY2() - line.getY1();
        double len = Math.sqrt(dx * dx + dy * dy);

        double nx = -dy / len;
        double ny = dx / len;

        double dot = vx * nx + vy * ny;
        vx -= 2 * dot * nx;
        vy -= 2 * dot * ny;

        x += vx * 0.1;
        y += vy * 0.1;
    }
}
