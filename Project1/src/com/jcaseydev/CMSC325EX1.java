package com.jcaseydev;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import javax.swing.JPanel;

public class CMSC325EX1 extends JPanel {

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    // Call methods to draw
    drawGrass(g);
    drawSky(g);
    drawHouse(g);
    drawCloud(g, 250, 58);
    drawSun(g);
  }

  private void drawSun(Graphics g) {
    Graphics2D graphics2D = (Graphics2D) g;
    Ellipse2D sun = new Ellipse2D.Double(-100, -100, 200, 200);

    graphics2D.setColor(Color.yellow);
    graphics2D.fill(sun);
    graphics2D.draw(sun);
  }

  private void drawCloud(Graphics g, int xPos, int yPos) {
    Graphics2D graphics2D = (Graphics2D) g;

    Ellipse2D cloud1 = new Ellipse2D.Double(xPos, yPos, 50, 50);
    Ellipse2D cloud2 = new Ellipse2D.Double(xPos + 25, yPos - 15, 50, 60);
    Ellipse2D cloud3 = new Ellipse2D.Double(xPos + 45, yPos, 50, 50);

    graphics2D.setColor(new Color(237, 237, 237));
    graphics2D.fill(cloud1);
    graphics2D.fill(cloud2);
    graphics2D.fill(cloud3);
    graphics2D.draw(cloud1);
    graphics2D.draw(cloud2);
    graphics2D.draw(cloud3);
  }

  private void drawHouse(Graphics g) {
    Graphics2D graphics2D = (Graphics2D) g;
    Rectangle2D house = new Rectangle2D.Double(100, 150, 250, 150);
    graphics2D.setColor(new Color(79, 25, 32));
    graphics2D.fill(house);
    graphics2D.draw(house);

    drawDoor(g);
  }

  private void drawDoor(Graphics g) {
    Graphics2D graphics2D = (Graphics2D) g;
    Rectangle2D door = new Rectangle2D.Double(195, 185, 60, 115);
    Ellipse2D doorKnob = new Ellipse2D.Double(240, 250, 10, 10);

    graphics2D.setColor(new Color(107, 68, 35));
    graphics2D.fill(door);
    graphics2D.draw(door);

    graphics2D.setColor(new Color(255, 204, 0));
    graphics2D.fill(doorKnob);
    graphics2D.draw(doorKnob);
  }

  private void drawSky(Graphics g) {
    Graphics2D graphics2D = (Graphics2D) g;
    Rectangle2D sky = new Double(0, 0, 400, 300);
    graphics2D.setColor(new Color(0, 170, 255));
    graphics2D.fill(sky);
    graphics2D.draw(sky);
  }

  private void drawGrass(Graphics g) {
    Graphics2D graphics2D = (Graphics2D) g;
    Rectangle2D grass = new Double(0, 300, 400, 100);
    Rectangle2D dirt = new Double(0, 345, 400, 25);
    graphics2D.setColor(new Color(147, 252, 81));
    graphics2D.fill(grass);
    graphics2D.draw(grass);

    graphics2D.setColor(new Color(92, 67, 39));
    graphics2D.fill(dirt);
    graphics2D.draw(dirt);
  }
}