////////////////////////////
// Justin Casey
// CMSC 325
// Sep 13, 2019
// Main.java: This
// class provide the main
// GUI.

package com.jcaseydev;

import javax.swing.JFrame;

public class Main extends JFrame {

  private Main() {
    Animations animations = new Animations();

    add(animations);

    setSize(1000, 600);
    setTitle("Justin's Animation");

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);
  }

  public static void main(String[] args) {
    Main main = new Main();
    main.setVisible(true);
  }
}
