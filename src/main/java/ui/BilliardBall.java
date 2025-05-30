package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class BilliardBall {
    public static final int radius = 30;
    public static final double masse = 0.17;
    public BufferedImage texture;
    public int id;
    public double x, y;
    public double vx, vy;

    private BallPanel ballPanel;
    private Timer physicsTimer;

    // 0 = weiße Kugel, 5 = schwarze Kugel, 1-7 Team Rot, 9-15 Team Blau

    public BilliardBall(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void setBallPanel(BallPanel ballPanel) {
        this.ballPanel = ballPanel;
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

        g2d.fill(new Ellipse2D.Double(x, y, radius, radius));
    }

    public void calcImpulse(int power, double angle) {
        double v = ((2 * BilliardQueue.masse) / (BilliardBall.masse + BilliardQueue.masse) * power * 0.3);

        vx = v * Math.cos(Math.toRadians(angle));
        vy = v * Math.sin(Math.toRadians(angle));

        System.out.println(vx);
        System.out.println(vy);

         physicsTimer = new Timer(16, e -> {
            updatePosition();
        });
        physicsTimer.start();
    }

    public void updatePosition() {
        this.x += vx;
        this.y += vy;

        if (x <= BilliardTable.tableX || x + radius >= BilliardTable.pocketRightX) {
            vx = -vx;
            x = Math.max(BilliardTable.tableX, Math.min(x, BilliardTable.pocketRightX - radius));
        }

        if (y <= BilliardTable.tableY || y + radius >= BilliardTable.pocketBottomY) {
            vy = -vy;
            y = Math.max(BilliardTable.tableY, Math.min(y, BilliardTable.pocketBottomY - radius));
        }

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
}
