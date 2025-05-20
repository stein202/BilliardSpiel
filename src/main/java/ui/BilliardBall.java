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
    private File a = new File("C:\\Users\\BG12\\IdeaProjects\\billard\\src\\main\\textures\\image.jpg");

    public BilliardBall(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        try {
            texture = ImageIO.read(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        BufferedImage ballImage = new BufferedImage(radius, radius, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ballImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        int w = texture.getWidth();
        int h = texture.getHeight();

        for (int y = 0; y < radius; y++) {
            double relY = (y - radius / 2.0) / (radius / 2.0);  // von -1 bis 1
            double ellipseWidth = Math.sqrt(1 - relY * relY);   // horizontale Breite des Kreises an dieser Höhe (cosinus-förmig)
            int srcX = (int) ((1 - ellipseWidth) / 2 * w);
            int srcW = (int) (ellipseWidth * w);

            if (srcW <= 0) continue;

            try {
                BufferedImage strip = texture.getSubimage(srcX, (int)((double)y / radius * h), srcW, 1);
                int destW = (int) (ellipseWidth * radius);
                int destX = (radius - destW) / 2;
                g.drawImage(strip, destX, y, destX + destW, y + 1, 0, 0, strip.getWidth(), 1, null);
            } catch (RasterFormatException e) {
                // Wenn die Subimage-Koordinaten ungültig sind, überspringen
                continue;
            }
        }

        // Kreis-Maske anwenden
        BufferedImage masked = new BufferedImage(radius, radius, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gMasked = masked.createGraphics();
        gMasked.setClip(new Ellipse2D.Double(0, 0, radius, radius));
        gMasked.drawImage(ballImage, 0, 0, null);
        gMasked.dispose();

        // Auf Fenster zeichnen
        g2d.drawImage(masked, (int) x, (int) y, null);
    }
}
