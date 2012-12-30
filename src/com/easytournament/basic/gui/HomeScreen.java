package com.easytournament.basic.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.easytournament.basic.model.HomeScreenPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;


public class HomeScreen extends JPanel implements MouseListener {

  protected HomeScreenPModel pm;
  protected JTable table;

  public HomeScreen(HomeScreenPModel pm) {
    this.pm = pm;
    this.init();
  }

  protected void init() {
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(getTopPanel(), BorderLayout.NORTH);
    this.add(getTablePanel(), BorderLayout.SOUTH);
    this.setBackground(Color.WHITE);
    this.setOpaque(true);
  }

  private Component getTopPanel() {
    JPanel p = new JPanel();
    p.setOpaque(false);
    p.setPreferredSize(new Dimension(300, 200));
    return p;
  }

  private Component getTablePanel() {
    JPanel p = new JPanel(new BorderLayout());
    JLabel recentLabel = new JLabel(ResourceManager.getText(Text.RECENT_TOURNS));
    recentLabel.setFont(recentLabel.getFont().deriveFont(Font.BOLD, 14));
    recentLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
    p.add(recentLabel, BorderLayout.NORTH);
    p.setOpaque(false);
    table = new JTable(pm.getTableModel());
    table.addMouseListener(this);
    JScrollPane pane = new JScrollPane(table);
    pane.setPreferredSize(new Dimension(-1, 200));
    p.add(pane, BorderLayout.CENTER);
    return p;
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    Dimension size = getSize();

    LinearGradientPaint fill = new LinearGradientPaint(new Point(0, 0),
        new Point(0, size.height), new float[] {0.0f, 1.0f}, new Color[] {
            getBackground().darker(), getBackground()});

    g2.setPaint(fill);
    g2.fillRect(0, 0, size.width - 1, size.height - 1);

    // super.paintComponent(g);
  }

  @Override
  public void mouseClicked(MouseEvent e) { /*DO NOTHING*/}

  @Override
  public void mouseEntered(MouseEvent e) {
    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
  }

  @Override
  public void mouseExited(MouseEvent e) {
    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }

  @Override
  public void mousePressed(MouseEvent e) { /*DO NOTHING*/}

  @Override
  public void mouseReleased(MouseEvent e) {
    int row = table.rowAtPoint(e.getPoint());
    if (row >= 0) {
      pm.openHistoryFile(row);
    }
  }

}
