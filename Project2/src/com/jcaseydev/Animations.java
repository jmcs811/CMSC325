package com.jcaseydev;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

class Animations extends JPanel {
    private static int translateX = 0;
    private static int translateY = 0;
    private static double rot = 0.0;
    private static double scaleX = 1.0;
    private static double scaleY = 1.0;

    // create all of the images
    private Image image = new Image();
    private BufferedImage redSquare = image.getImage(Image.redSquare);
    private BufferedImage circle = image.getImage(Image.circle);
    private BufferedImage letterE = image.getImage(Image.letterE);
    private BufferedImage letterJ = image.getImage(Image.letterJ);
    private BufferedImage cross = image.getImage(Image.cross);

    private int frameNumber;

    Animations() {
        Timer timer = new Timer(
            1000,
            arg0 -> {
           if (frameNumber >= 6) {
               frameNumber = 0;
           } else {
               frameNumber ++;
           }
           repaint();
        });

        timer.start();
    }
    public void paintComponent(Graphics g) {

        /* First, create a Graphics2D drawing context for drawing on the panel.
         * (g.create() makes a copy of g, which will draw to the same place as g,
         * but changes to the returned copy will not affect the original.)
         */
        Graphics2D g2 = (Graphics2D) g.create();

        /* Turn on antialiasing in this graphics context, for better drawing.
         */
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /* Fill in the entire drawing area with white.
         */
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight()); // From the old graphics API!

        /* Here, I set up a new coordinate system on the drawing area, by calling
         * the applyLimits() method that is defined below.  Without this call, I
         * would be using regular pixel coordinates.  This function sets the value
         * of the global variable pixelSize, which I need for stroke widths in the
         * transformed coordinate system.
         */
        // Controls your zoom and area you are looking at
        applyWindowToViewportTransformation(g2, -75, 75, -75, 75, true);

        AffineTransform savedTransform = g2.getTransform();
        System.out.println("Frame is " + frameNumber);
        switch (frameNumber) {
            case 1: // First frame is unmodified.
                translateX = 0;
                translateY = 0;
                scaleX = 1.0;
                scaleY = 1.0;
                rot = 0;
                break;
            case 2: // Second frame translates each image by (-9, 5).
                translateX = -9;
                translateY = 5;
                break;
            case 3: // Third frame rotates each image by 60 degrees Counter
                translateX = -9;
                translateY = 5;
                rot = 60*Math.PI / 180.0;
                break;
            // Can add more cases as needed
            default:
                break;
        } // End switch
        g2.translate(translateX, translateY); // Move image.
        // To offset translate again
        g2.translate(-10,10);
        g2.rotate(rot); // Rotate image.
        g2.scale(scaleX, scaleY); // Scale image.
        g2.drawImage(redSquare, 0, 0, this); // Draw image.
        g2.setTransform(savedTransform);

        // Add another T image
        g2.translate(translateX, translateY); // Move image.
        // To offset translate again
        // This allows you to place your images across your graphic
        g2.translate(-30,30);
        g2.rotate(rot); // Rotate image.
        g2.scale(scaleX, scaleY); // Scale image.
        g2.drawImage(redSquare, 0, 0, this); // Draw image.
        g2.setTransform(savedTransform);
    }
    private void applyWindowToViewportTransformation(Graphics2D g2,
        double left, double right, double bottom, double top,
        boolean preserveAspect) {
        int width = getWidth();   // The width of this drawing area, in pixels.
        int height = getHeight(); // The height of this drawing area, in pixels.
        if (preserveAspect) {
            // Adjust the limits to match the aspect ratio of the drawing area.
            double displayAspect = Math.abs((double) height / width);
            double requestedAspect = Math.abs((bottom - top) / (right - left));
            if (displayAspect > requestedAspect) {
                // Expand the viewport vertically.
                double excess = (bottom - top) * (displayAspect / requestedAspect - 1);
                bottom += excess / 2;
                top -= excess / 2;
            } else if (displayAspect < requestedAspect) {
                // Expand the viewport vertically.
                double excess = (right - left) * (requestedAspect / displayAspect - 1);
                right += excess / 2;
                left -= excess / 2;
            }
        }
        g2.scale(width / (right - left), height / (bottom - top));
        g2.translate(-left, -top);
        double pixelWidth = Math.abs((right - left) / width);
        double pixelHeight = Math.abs((bottom - top) / height);
        //pixelSize = (float) Math.max(pixelWidth, pixelHeight);
    }
}
