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
