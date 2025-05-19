package ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class BilliardBall {
    public static final int radius = 30;
    public BufferedImage texture;
    public int id;
    public double x, y;
    private File a = new File("ressource/ball2.jpg");

    public BilliardBall(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        try {
            this.texture = ImageIO.read(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawBall(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fill(new Ellipse2D.Double(x, y, radius, radius));
        g2d.drawImage(texture, (int) x, (int) y, 200,400, null);
        System.out.println(texture);
    }
}
