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
        final int textureWidthFocus = 128;  // Rechte Hälfte der Textur
        final int textureStartX = 128;
        final int ballRenderSize = radius;

        try {
            texture = ImageIO.read(new File("C:\\Users\\stein\\Desktop\\FreakyBillard\\src\\main\\resources\\textures\\ball" + id + ".jpg"));
        } catch (IOException e) {
            throw new RuntimeException("Konnte Textur für Ball " + id + " nicht laden", e);
        }

        BufferedImage ballImage = new BufferedImage(ballRenderSize, ballRenderSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ballImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = texture.getWidth();
        int h = texture.getHeight();

        boolean isStriped = id >= 9;
        int stripeTop = (int) (h * 0.35);  // Start des Farbbalkens (ca. mittig)
        int stripeHeight = (int) (h * 0.3);  // ~30% Höhe

        for (int y = 0; y < ballRenderSize; y++) {
            double relY = (y - ballRenderSize / 2.0) / (ballRenderSize / 2.0);  // -1 bis 1
            if (Math.abs(relY) > 1) continue;

            double ellipseWidth = Math.sqrt(1 - relY * relY);  // horizontaler Querschnitt
            int destW = (int) (ellipseWidth * ballRenderSize);
            int destX = (ballRenderSize - destW) / 2;

            int srcY;
            if (isStriped) {
                // Halbe Kugeln: Bereich aus mittlerem Streifen
                srcY = stripeTop + (int)(((double)y / ballRenderSize) * stripeHeight);
            } else {
                // Volle Kugeln: Volle vertikale Skala
                srcY = (int)(((double)y / ballRenderSize) * h);
            }

            try {
                BufferedImage strip = texture.getSubimage(textureStartX, srcY, textureWidthFocus, 1);
                g.drawImage(strip, destX, y, destX + destW, y + 1,
                        0, 0, strip.getWidth(), 1, null);
            } catch (RasterFormatException e) {
                continue;
            }
        }

        // Kugelmaske anwenden
        BufferedImage masked = new BufferedImage(ballRenderSize, ballRenderSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gMasked = masked.createGraphics();
        gMasked.setClip(new Ellipse2D.Double(0, 0, ballRenderSize, ballRenderSize));
        gMasked.drawImage(ballImage, 0, 0, null);
        gMasked.dispose();

        g2d.drawImage(masked, (int) x, (int) y, null);
    }
}
