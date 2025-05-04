package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class BilliardTable extends JPanel {
    private final int width = 1400;
    private final int height = 700;
    private final int pocketRadius = 30;
    private final int cushionThickness = 20;

    private final Color tableColor = new Color(92, 158, 49, 255);
    private final Color cushionColor = new Color(131, 214, 77, 255);
    private final Color woodColor1 = new Color(175, 115, 70);
    private final Color woodColor2 = new Color(140, 85, 50);
    private final Color woodColor3 = new Color(100, 60, 35);

    public BilliardTable() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        int outerMargin = 30;
        int frameThickness = 30;
        int outlineSize = 5;

        int tableX = outerMargin + frameThickness;
        int tableY = outerMargin + frameThickness;
        int tableWidth = width - 2 * (outerMargin + frameThickness);
        int tableHeight = height - 2 * (outerMargin + frameThickness);

        float centerX = tableX + tableWidth / 2f;
        float centerY = tableY + tableHeight / 2f;
        float radius = Math.max(tableWidth, tableHeight) / 2f;

        int pocketY = outerMargin + outlineSize - 5;
        int pocketBottomY = height - (pocketY + pocketRadius * 2);
        int pocketX = outerMargin + outlineSize - 5;
        int pocketRightX = width - (pocketX + pocketRadius * 2);
        int pocketCenterX = this.getWidth() / 2 - pocketRadius;

        // Holzrahmen
        g2d.setColor(woodColor1);
        int woodX = outerMargin - outlineSize;
        int woodY = outerMargin - outlineSize;
        int woodWidth = width - 2 * outerMargin + outlineSize * 2;
        int woodHeight = height - 2 * outerMargin + outlineSize * 2;

        // Holzrahmen für oben
        drawWoodRim(g2d, woodX, woodY, woodWidth, (float) (woodHeight - tableHeight) / 2, "TOP"); // oben
        drawWoodRim(g2d, woodX, (woodY + tableHeight + (float) (woodHeight - tableHeight) / 2), woodWidth, (float) (woodHeight - tableHeight) / 2, "BOTTOM"); // unten
        drawWoodRim(g2d, woodX, woodY, (float) (woodWidth - tableWidth) / 2, woodHeight, "LEFT"); // links
        drawWoodRim(g2d, woodX + tableWidth + (float) (woodWidth - tableWidth) / 2, woodY, (float) (woodWidth - tableWidth) / 2, woodHeight, "RIGHT"); // rechts

        // Pocket Casing
        int offset = 1;
        drawPocketCasing(g2d, pocketX - offset, pocketY - offset); // oben links
        drawPocketCasing(g2d, width - (pocketX + pocketRadius * 2) + offset, pocketY - offset); // oben rechts
        drawPocketCasing(g2d, pocketX - offset, height - (pocketY + pocketRadius * 2) + offset); // unten links
        drawPocketCasing(g2d, width - (pocketX + pocketRadius * 2) + offset, height - (pocketY + pocketRadius * 2) + offset); // unten rechts
        drawPocketCasing(g2d, centerX - pocketRadius, pocketY - offset); // oben mitte
        drawPocketCasing(g2d, centerX - pocketRadius, height - (pocketY + pocketRadius * 2) + offset);


        // Grüner Tisch
        RoundRectangle2D tableRect = new RoundRectangle2D.Double(tableX, tableY, tableWidth, tableHeight, 40, 40);
        g2d.setColor(tableColor);
        g2d.fill(tableRect);

        // Banden
        Shape top = new Rectangle2D.Float(tableX, tableY, tableWidth, cushionThickness);
        Shape left = new Rectangle2D.Float(tableX, tableY, cushionThickness, tableHeight);
        Shape right = new Rectangle2D.Float(tableX + tableWidth - cushionThickness, tableY, cushionThickness, tableHeight);
        Shape bottom = new Rectangle2D.Float(tableX, tableY + tableHeight - cushionThickness, tableWidth, cushionThickness);

        Area cushion = new Area(top);
        cushion.add(new Area(left));
        cushion.add(new Area(right));
        cushion.add(new Area(bottom));

        Polygon topMidCut = new Polygon();

        int xAchse = pocketCenterX + pocketRadius;
        int yAchse = pocketY + pocketRadius;

        int x1 = xAchse;
        int y1 = yAchse - pocketRadius;
        int x2 = xAchse - pocketRadius;
        int y2 = yAchse;
        int x3 = xAchse + pocketRadius;
        int y3 = yAchse;

        int sx1 = x1 + 5*(x2-x1);
        int sx2= x1 + 5*(x3-x1);
        int sy1 = y2 + 5*(y2-y1);
        int sy2= y2 + 5*(y3-y1);

        topMidCut.addPoint(x1, y1);
        topMidCut.addPoint(sx1, sy1);
        topMidCut.addPoint(sx2, sy2);

        cushion.subtract(new Area(topMidCut));

        Polygon bottomMidCut = new Polygon();

        xAchse = pocketCenterX + pocketRadius;
        yAchse = pocketBottomY + pocketRadius;

        x1 = xAchse;
        y1 = yAchse + pocketRadius;
        x2 = xAchse - pocketRadius;
        y2 = yAchse;
        x3 = xAchse + pocketRadius;
        y3 = yAchse;

        sx1 = x1 + 5*(x2-x1);
        sx2= x1 + 5*(x3-x1);
        sy1 = y2 + 5*(y2-y1);
        sy2= y2 + 5*(y3-y1);

        bottomMidCut.addPoint(x1, y1);
        bottomMidCut.addPoint(sx1, sy1);
        bottomMidCut.addPoint(sx2, sy2);

        cushion.subtract(new Area(bottomMidCut));

        g2d.setColor(cushionColor);
        g2d.fill(cushion);

        // Schatten
        RadialGradientPaint radialShadow = new RadialGradientPaint(
                centerX, centerY, radius,
                new float[]{0f, 1f},
                new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 100)},
                MultipleGradientPaint.CycleMethod.NO_CYCLE
        );
        g2d.setPaint(radialShadow);
        g2d.fill(tableRect);

        // Löcher
        g2d.setColor(new Color(0, 0, 0, 255));
        drawPocket(g2d, pocketX, pocketY); // oben links
        drawPocket(g2d, pocketRightX, pocketY); // oben rechts
        drawPocket(g2d, pocketX, pocketBottomY); // unten links
        drawPocket(g2d, pocketRightX, pocketBottomY); // unten rechts
        drawPocket(g2d, pocketCenterX, pocketY - 5); // oben mitte
        drawPocket(g2d, pocketCenterX, pocketBottomY + 5); // unten mitte
    }

    private void drawPocket(Graphics2D g, float x, float y) {
        float centerX = x + pocketRadius;
        float centerY = y + pocketRadius;

        float radius = pocketRadius;
        g.fill(new Ellipse2D.Float(centerX - radius, centerY - radius, radius * 2, radius * 2));
    }

    private void drawPocketCasing(Graphics2D g, float x, float y) {
        float centerX = x + pocketRadius;
        float centerY = y + pocketRadius;

        float casingThickness = 15f; // Dicke des goldenen Rahmens
        float casingRadius = pocketRadius + casingThickness;

        RadialGradientPaint goldGradient = new RadialGradientPaint(
                centerX, centerY, casingRadius,
                new float[]{0.7f, 1f},
                new Color[]{
                        new Color(184, 134, 11),  // dunkles Gold
                        new Color(218, 165, 32)   // sattes Gold
                }
        );

        Paint oldPaint = g.getPaint();
        g.setPaint(goldGradient);
        g.fill(new RoundRectangle2D.Float(
                centerX - casingRadius,
                centerY - casingRadius,
                casingRadius * 2,
                casingRadius * 2,
                50, 50
        ));
        g.setPaint(oldPaint);
    }

    private void drawWoodRim(Graphics2D g, float x, float y, float width, float height, String pos) {
        float startX = x;
        float startY = y;
        float endX = x + width;
        float endY = y + height;

        switch (pos) {
            case ("TOP"):
                startX = x;
                startY = y + height;
                endX = x;
                endY = y;
                break;
            case ("BOTTOM"):
                startX = x;
                startY = y;
                endX = x;
                endY = y + height;
                break;
            case ("LEFT"):
                startX = x + width;
                startY = y;
                endX = x;
                endY = y;
                break;
            case ("RIGHT"):
                startX = x;
                startY = y;
                endX = x + width;
                endY = y;
                break;
        }


        LinearGradientPaint woodGradient = new LinearGradientPaint(
                startX, startY,
                endX, endY,
                new float[]{0f, 0.2f, 1f},
                new Color[]{woodColor3, woodColor2, woodColor1}
        );
        g.setPaint(woodGradient);
        g.fill(new RoundRectangle2D.Float(x, y, width, height, 50, 50));
    }
}
