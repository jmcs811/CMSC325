package com.jcaseydev;

import javax.swing.JFrame;

public class Driver extends JFrame {

  private Driver() {
    // Construct Class with Graphics Component
    CMSC325EX1 myExample = new CMSC325EX1();
    // Add to JFrame
    add(myExample);
    // Set the Default Size and title
    setSize(400, 400);
    setTitle("CMSC 325 Java2D Template");

    // Frame Default to be able to closd
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Center the Frame
    setLocationRelativeTo(null);
  }


  public static void main(String[] args) {

    Driver myDriver = new Driver();
    myDriver.setVisible(true);
  }
}
