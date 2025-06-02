package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class BilliardBall {
    public static final int radius = 30;
    public static final double masse = 0.17;
    public int id;
    public double orgX, orgY;
    public double x, y;
    public double vx, vy;

    public Ellipse2D ball;
    private BilliardTable billiardTable;

    private BallPanel ballPanel;
    private Timer physicsTimer;

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
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

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
        physicsTimer = new Timer(16, e -> updatePosition());
        physicsTimer.start();
    }

    public void updatePosition() {
        this.x += vx;
        this.y += vy;

        checkCushionCollision(billiardTable.cushion);

        vx *= 0.98;
        vy *= 0.98;

        if (Math.abs(vx) < 0.05) vx = 0;
        if (Math.abs(vy) < 0.05) vy = 0;

        if (vx == 0 && vy == 0 && physicsTimer != null) physicsTimer.stop();

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
            x = orgX;
            y = orgY;
            physicsTimer.stop();
            return;
        }

        if (cushion.intersects(ball.getBounds2D())) {
            double left = billiardTable.getBounds().getMinX();
            double right = billiardTable.getBounds().getMaxX();
            double top = billiardTable.getBounds().getMinY();
            double bottom = billiardTable.getBounds().getMaxY();

            if (x - radius <= left || x + radius >= right) {
                vx = -vx;
                x += vx; // kleine Korrektur, damit der Ball nicht in der Bande hängen bleibt
            }

            // Prallen an oberer oder unterer Bande
            if (y - radius <= top || y + radius >= bottom) {
                vy = -vy;
                y += vy;
            }
        }
    }
}
