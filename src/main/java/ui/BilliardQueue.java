package main.java.ui;

import main.java.misc.Turn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

public class BilliardQueue extends JPanel implements MouseListener, MouseMotionListener {
    public Turn turn;
    public int angle;
    public BilliardBall ball;
    public static final int laenge = 800;
    public boolean moveable = false;

    private int mouseStartX;
    private int mouseStartY;

    // HARDCORE Performance-Optimierung: Caching & Throttling
    private int lastMouseX = -1;
    private int lastMouseY = -1;
    private static final int MOUSE_THRESHOLD = 5; // Größerer Threshold
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 16; // ~60 FPS max

    // Gecachte Werte für wiederholte Berechnungen
    private double cachedCos = 0;
    private double cachedSin = 0;
    private int cachedAngle = -1;

    public BilliardQueue() {
        this.turn = Turn.PLAYER1;
        this.angle = 0;

        // KRITISCHER FIX: MouseListener registrieren!
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void switchPlayerTurn() {
        if (turn == Turn.PLAYER1) {
            this.turn = Turn.PLAYER2;
        } else {
            this.turn = Turn.PLAYER1;
        }
    }

    public void setBall(BilliardBall b) {
        this.ball = b;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (ball == null || !moveable) return;

        // MINIMAL super call - nur was nötig ist
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ULTRA-FAST Berechnungen - gecachte Werte verwenden
        int mittelPunktX = (int) ball.x + (BilliardBall.radius >> 1); // Bit-Shift
        int mittelPunktY = (int) ball.y + (BilliardBall.radius >> 1);

        // Trigonometrische Werte nur bei Bedarf neu berechnen
        if (cachedAngle != angle) {
            double winkel = Math.toRadians(angle);
            cachedCos = Math.cos(winkel);
            cachedSin = Math.sin(winkel);
            cachedAngle = angle;
        }

        // Konstanten als finale Variablen für JIT-Optimierung
        final int radius = BilliardBall.radius;
        final int queueLength = 150;
        final int ballDistance = 10;

        // Direkte int-Berechnungen wo möglich
        int startX = (int) (mittelPunktX - (radius + ballDistance) * cachedCos);
        int startY = (int) (mittelPunktY - (radius + ballDistance) * cachedSin);
        int endX = (int) (startX - queueLength * cachedCos);
        int endY = (int) (startY - queueLength * cachedSin);

        // MINIMALES Rendering - nur das Nötigste
        g2d.setColor(new Color(139, 69, 19));
        g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(startX, startY, endX, endY); // drawLine ist schneller als Line2D

        // Queue-Spitze als gefülltes Rechteck (schneller als Oval)
        g2d.setColor(new Color(101, 67, 33));
        g2d.fillRect(startX - 3, startY - 3, 6, 6);
    }

    public void setMoveable() {
        moveable = !moveable;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) { // Verwende Konstante statt 1
            setMoveable();
            mouseStartX = e.getX();
            mouseStartY = e.getY();
            repaint(); // Neu zeichnen nach Klick
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!moveable || ball == null) return;

        // THROTTLING: Max 60 FPS Updates
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime < UPDATE_INTERVAL) {
            return;
        }

        int currentX = e.getX();
        int currentY = e.getY();

        // Aggressiverer Threshold
        if (Math.abs(currentX - lastMouseX) < MOUSE_THRESHOLD &&
                Math.abs(currentY - lastMouseY) < MOUSE_THRESHOLD) {
            return;
        }

        lastMouseX = currentX;
        lastMouseY = currentY;
        lastUpdateTime = currentTime;

        // OPTIMIERTE Berechnungen - weniger Casts
        int ballCenterX = (int) ball.x + (BilliardBall.radius >> 1); // Bit-Shift statt Division
        int ballCenterY = (int) ball.y + (BilliardBall.radius >> 1);

        // Fast inverse square root für Distanz (falls nötig)
        int deltaX = currentX - ballCenterX;
        int deltaY = currentY - ballCenterY;

        // Lookup-Table für atan2 wäre optimal, aber Math.atan2 ist OK
        int newAngle = (int) Math.toDegrees(Math.atan2(deltaY, deltaX));
        if (newAngle < 0) newAngle += 360;

        // Nur bei signifikanter Änderung
        if (Math.abs(newAngle - angle) > 2) { // Größerer Threshold
            angle = newAngle;

            // Trigonometrische Werte cachen
            double radians = Math.toRadians(angle);
            cachedCos = Math.cos(radians);
            cachedSin = Math.sin(radians);
            cachedAngle = angle;

            repaint();
        }
    }
}
