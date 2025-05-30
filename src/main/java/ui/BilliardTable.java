package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class BilliardTable extends JPanel {
    private final int pocketRadius = 30;
    private final int cushionThickness = 20;
    private final BilliardQueue billiardQueue;
    private final BallPanel ballPanel;
    private final int outerMargin = 30;
    private final int frameThickness = 30;
    private final int outlineSize = 5;

    private final Color tableColor = new Color(92, 158, 49, 255);
    private final Color cushionColor = new Color(131, 214, 77, 255);
    private final Color woodColor1 = new Color(175, 115, 70);
    private final Color woodColor2 = new Color(140, 85, 50);
    private final Color woodColor3 = new Color(100, 60, 35);

    private long lastTime = System.currentTimeMillis();
    private int frames = 0;

    public ArrayList<BilliardBall> balls = new ArrayList<>();

    //Ballpositionen berechnen + Löcherpositionen
    int tableX = outerMargin + frameThickness;
    int tableY = outerMargin + frameThickness;
    int tableWidth = GameFrame.width - 2 * (outerMargin + frameThickness);
    int tableHeight = GameFrame.height - 2 * (outerMargin + frameThickness);

    float centerX = tableX + tableWidth / 2;
    float centerY = tableY + tableHeight / 2;
    float radius = Math.max(tableWidth, tableHeight) / 2;

    int pocketY = outerMargin + outlineSize - 5;
    int pocketBottomY = GameFrame.height - (pocketY + pocketRadius * 2);
    int pocketX = outerMargin + outlineSize - 5;
    int pocketRightX = GameFrame.width - (pocketX + pocketRadius * 2);
    int pocketCenterX = this.getWidth() / 2 - pocketRadius;

    public BilliardTable(JFrame g) {
        setPreferredSize(new Dimension(GameFrame.width, GameFrame.height));
        setBackground(Color.DARK_GRAY);
        billiardQueue = new BilliardQueue();
        billiardQueue.setOpaque(false);
        billiardQueue.setVisible(true);
        billiardQueue.setBounds(0, 0, GameFrame.width, GameFrame.height);
        g.add(billiardQueue);

        double whiteballx = (centerX + pocketCenterX) /2;
        double whitebally = (GameFrame.height/2) -15;
        double rectanglex = ((centerX+pocketRightX)/2) -90 ;
        double rectangley = whitebally;
        BilliardBall cueBall = new BilliardBall(0, whiteballx + BilliardBall.radius , whitebally);
        balls.add(cueBall);

        createBallTriangle(rectanglex, rectangley, 5, BilliardBall.radius);
        billiardQueue.setBall(cueBall);

        ballPanel = new BallPanel(balls);
        ballPanel.setOpaque(false);
        ballPanel.setVisible(true);
        ballPanel.setBounds(0, 0, GameFrame.width, GameFrame.height);
        g.add(ballPanel);

        for (BilliardBall ball: balls) {
            ball.setBallPanel(ballPanel);
        }
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

        super.paintComponent(g);
        frames++;
        if (System.currentTimeMillis() - lastTime >= 1000) {
            System.out.println("FPS: " + frames);
            frames = 0;
            lastTime = System.currentTimeMillis();
        }



        int tableX = outerMargin + frameThickness;
        int tableY = outerMargin + frameThickness;
        int tableWidth = GameFrame.width - 2 * (outerMargin + frameThickness);
        int tableHeight = GameFrame.height - 2 * (outerMargin + frameThickness);

        float centerX = tableX + tableWidth / 2f;
        float centerY = tableY + tableHeight / 2f;
        float radius = Math.max(tableWidth, tableHeight) / 2f;

        int pocketY = outerMargin + outlineSize - 5;
        int pocketBottomY = GameFrame.height - (pocketY + pocketRadius * 2);
        int pocketX = outerMargin + outlineSize - 5;
        int pocketRightX = GameFrame.width - (pocketX + pocketRadius * 2);
        int pocketCenterX = this.getWidth() / 2 - pocketRadius;

        // Holzrahmen
        g2d.setColor(woodColor1);
        int woodX = outerMargin - outlineSize;
        int woodY = outerMargin - outlineSize;
        int woodWidth = GameFrame.width - 2 * outerMargin + outlineSize * 2;
        int woodHeight = GameFrame.height - 2 * outerMargin + outlineSize * 2;

        // Holzrahmen für oben
        drawWoodRim(g2d, woodX, woodY, woodWidth, (float) (woodHeight - tableHeight) / 2, "TOP"); // oben
        drawWoodRim(g2d, woodX, (woodY + tableHeight + (float) (woodHeight - tableHeight) / 2), woodWidth, (float) (woodHeight - tableHeight) / 2, "BOTTOM"); // unten
        drawWoodRim(g2d, woodX, woodY, (float) (woodWidth - tableWidth) / 2, woodHeight, "LEFT"); // links
        drawWoodRim(g2d, woodX + tableWidth + (float) (woodWidth - tableWidth) / 2, woodY, (float) (woodWidth - tableWidth) / 2, woodHeight, "RIGHT"); // rechts

        // Pocket Casing
        int offset = 1;
        drawPocketCasing(g2d, pocketX - offset, pocketY - offset); // oben links
        drawPocketCasing(g2d, GameFrame.width - (pocketX + pocketRadius * 2) + offset, pocketY - offset); // oben rechts
        drawPocketCasing(g2d, pocketX - offset, GameFrame.height - (pocketY + pocketRadius * 2) + offset); // unten links
        drawPocketCasing(g2d, GameFrame.width - (pocketX + pocketRadius * 2) + offset, GameFrame.height - (pocketY + pocketRadius * 2) + offset); // unten rechts
        drawPocketCasing(g2d, centerX - pocketRadius, pocketY - offset); // oben mitte
        drawPocketCasing(g2d, centerX - pocketRadius, GameFrame.height - (pocketY + pocketRadius * 2) + offset);


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

        //Dreiek für oben in der Mitte
        Polygon topMidCut = new Polygon();

        int x = pocketCenterX + pocketRadius;
        int y = pocketY + pocketRadius;

        int x1 = x;
        int y1 = y - pocketRadius;
        int x2 = x - pocketRadius;
        int y2 = y;
        int x3 = x + pocketRadius;
        int y3 = y;

        int sx1 = x1 + 5*(x2-x1);
        int sx2= x1 + 5*(x3-x1);
        int sy1 = y2 + 5*(y2-y1);
        int sy2= y2 + 5*(y3-y1);

        topMidCut.addPoint(x1, y1);
        topMidCut.addPoint(sx1, sy1);
        topMidCut.addPoint(sx2, sy2);

        cushion.subtract(new Area(topMidCut));

        //Dreieck für die Mitte unten
        Polygon bottomMidCut = new Polygon();

        x = pocketCenterX + pocketRadius;
        y = pocketBottomY + pocketRadius;

        x1 = x;
        y1 = y + pocketRadius;
        x2 = x - pocketRadius;
        y2 = y;
        x3 = x + pocketRadius;
        y3 = y;

        sx1 = x1 + 5*(x2-x1);
        sx2= x1 + 5*(x3-x1);
        sy1 = y2 + 5*(y2-y1);
        sy2= y2 + 5*(y3-y1);

        bottomMidCut.addPoint(x1, y1);
        bottomMidCut.addPoint(sx1, sy1);
        bottomMidCut.addPoint(sx2, sy2);

        cushion.subtract(new Area(bottomMidCut));

        //Rechteck für die obere linke Ecke
        Polygon topLeftCut = new Polygon();

        x = pocketX + pocketRadius;
        y = pocketY + pocketRadius;

        x1 = x;
        y1 = y - pocketRadius;
        x2 = x - pocketRadius;
        y2 = y;
        x3 = x + pocketRadius;
        y3 = y;
        int x4 = x;
        int y4 = y + pocketRadius;

        sx1 = x2 + 5*(x4-x2);
        sy1 = y2 + 5*(y4-y2);
        sx2= x1 + 5*(x3-x1);
        sy2= y1 + 5*(y3-y1);

        topLeftCut.addPoint(x1, y1);
        topLeftCut.addPoint(x2, y2);
        topLeftCut.addPoint(sx1, sy1);
        topLeftCut.addPoint(sx2, sy2);

        cushion.subtract(new Area(topLeftCut));

        //Rechteck oben rechte Ecke
        Polygon topRightCut = new Polygon();

        x = pocketRightX + pocketRadius;
        y = pocketY + pocketRadius;

        x1 = x;
        y1 = y - pocketRadius;
        x2 = x - pocketRadius;
        y2 = y;
        x3 = x + pocketRadius;
        y3 = y;
        x4 = x;
        y4 = y + pocketRadius;

        sx1 = x1 + 5*(x2-x1);
        sy1 = y1 + 5*(y2-y1);
        sx2= x3 + 5*(x4-x3);
        sy2= y3 + 5*(y4-y3);

        topRightCut.addPoint(x1, y1);
        topRightCut.addPoint(sx1, sy1);
        topRightCut.addPoint(sx2, sy2);
        topRightCut.addPoint(x3, y3);

        cushion.subtract(new Area(topRightCut));

        //Rechteck unten linke Ecke
        Polygon bottomLeftCut = new Polygon();

        x = pocketX + pocketRadius;
        y = pocketBottomY + pocketRadius;

        x1 = x;
        y1 = y - pocketRadius;
        x2 = x - pocketRadius;
        y2 = y;
        x3 = x + pocketRadius;
        y3 = y;
        x4 = x;
        y4 = y + pocketRadius;

        sx1 = x2 + 5*(x1-x2);
        sy1 = y2 + 5*(y1-y2);
        sx2= x4 + 5*(x3-x4);
        sy2= y4 + 5*(y3-y4);

        bottomLeftCut.addPoint(sx1, sy1);
        bottomLeftCut.addPoint(x2, y2);
        bottomLeftCut.addPoint(x4, y4);
        bottomLeftCut.addPoint(sx2, sy2);

        cushion.subtract(new Area(bottomLeftCut));

        //Rechteck für die untere rechte Ecke
        Polygon bottomRightCut = new Polygon();

        x = pocketRightX + pocketRadius;
        y = pocketBottomY + pocketRadius;

        x1 = x;
        y1 = y - pocketRadius;
        x2 = x - pocketRadius;
        y2 = y;
        x3 = x + pocketRadius;
        y3 = y;
        x4 = x;
        y4 = y + pocketRadius;

        sx1 = x3 + 5*(x1-x3);
        sy1 = y3 + 5*(y1-y3);
        sx2= x4 + 5*(x2-x4);
        sy2= y4 + 5*(y2-y4);

        bottomRightCut.addPoint(sx1, sy1);
        bottomRightCut.addPoint(sx2, sy2);
        bottomRightCut.addPoint(x4, y4);
        bottomRightCut.addPoint(x3, y3);

        cushion.subtract(new Area(bottomRightCut));

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

    //Zeichnung des Holzrahmens
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

    public void createBallTriangle(double startX, double startY, int numRows, double ballDiameter) {
        double h = ballDiameter * Math.sqrt(3) / 2;

        int ballId = 1;
        for (int row = 0; row < numRows; row++) {
            double x = startX + row * h;
            double yStart = startY - row * ballDiameter / 2;

            for (int col = 0; col <= row; col++) {
                double y = yStart + col * ballDiameter;
                balls.add(new BilliardBall(ballId++, x, y));
            }
        }
    }
}
