package main.java.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

public class BilliardBall {
    public static final int radius = 30;
    public BufferedImage texture;
    public int id;
    public double x, y;

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

        g2d.setColor(Color.WHITE);
        g2d.fill(new Ellipse2D.Double(b.x, b.y, radius, radius));
    }

    public void drawBall(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        try {
            texture = ImageIO.read(new File("C:\\Users\\stein\\Desktop\\FreakyBillard\\src\\main\\resources\\textures\\ball" + id + ".jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int shiftX = (int)(texture.getWidth() * 0.2); // 20% Verschiebung nach links
        BufferedImage shiftedTexture = new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gShift = shiftedTexture.createGraphics();
        gShift.drawImage(texture, -shiftX, 0, null); // linker Teil
        gShift.drawImage(texture, texture.getWidth() - shiftX, 0, null); // rechter Wraparound-Teil
        gShift.dispose();

        BufferedImage masked = new BufferedImage(radius, radius, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = masked.createGraphics();

        Ellipse2D circle = new Ellipse2D.Double(0, 0, radius, radius);
        g.setClip(circle);
        g.drawImage(shiftedTexture, 0, 0, radius, radius, null);

        // Optionaler Lichtreflex für 3D-Effekt
        g.setPaint(new RadialGradientPaint(
                new Point(radius / 3, radius / 3),
                radius,
                new float[]{0f, 1f},
                new Color[]{new Color(255, 255, 255, 100), new Color(255, 255, 255, 0)}
        ));
        g.fill(circle);

        g.dispose();
        g2d.drawImage(masked, (int) x, (int) y, null);
    }
}
