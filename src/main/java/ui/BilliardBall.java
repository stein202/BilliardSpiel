package main.java.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class BilliardBall {
    public static final int radius = 30;
    public BufferedImage texture;
    public int id;
    public double x, y;

    // 0 = weiße Kugel, 5 = schwarze Kugel, 1-7 Team Rot, 9-15 Team Blau

    public BilliardBall(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void drawBall(Graphics2D g2d, BilliardBall b) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        if (b.id == 0) {
            g2d.setColor(Color.WHITE);

        } else if (b.id == 5) {
            g2d.setColor(Color.BLACK);

        } else if (b.id >= 1 && b.id <= 7) {
            g2d.setColor(Color.RED);

        } else if (b.id >= 9 && b.id <= 15) {
            g2d.setColor(Color.BLUE);
        }

        g2d.fill(new Ellipse2D.Double(b.x, b.y, radius, radius));
    }
}
