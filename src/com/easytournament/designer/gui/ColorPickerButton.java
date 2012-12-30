package com.easytournament.designer.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Action;
import javax.swing.JButton;

public class ColorPickerButton extends JButton {

  public ColorPickerButton(Action a) {
    super(a);
    setFocusPainted(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g.create();
    g2.setColor(this.getBackground());
    g2.fillRect(2, 2, this.getWidth() - 4, this.getHeight() - 4);
    g2.dispose();
  }

}
