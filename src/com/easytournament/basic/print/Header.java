package com.easytournament.basic.print;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.ImageIcon;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.valueholder.Tournament;


public class Header implements Printable {

  private static final double IMAGE_HEIGHT = 100;
  private static final BasicStroke headerLineStroke = new BasicStroke(1.0f);
  private ImageIcon logo;
  private double scaleImage;
  private String title;

  public Header() {
    Tournament t = Organizer.getInstance().getCurrentTournament();
    this.logo = t.getLogo();
    this.title = t.getName();
    if(logo != null)
      this.scaleImage = IMAGE_HEIGHT / this.logo.getIconHeight();
  }

  public int getHeight() {
    return (int)IMAGE_HEIGHT + 10;
  }

  @Override
  public int print(Graphics graphics, PageFormat pf, int pageIndex)
      throws PrinterException {

    Graphics2D g2d = (Graphics2D)graphics.create();

    Font tmpFont = g2d.getFont();
    Font titleFont = tmpFont.deriveFont(Font.BOLD, 20);
    g2d.setFont(titleFont);

    if (logo != null) {

      g2d.drawImage(logo.getImage(), (int)pf.getImageableX(), 0,
          (int)(logo.getIconWidth() * scaleImage),
          (int)(logo.getIconHeight() * scaleImage), null);

      g2d.drawString(title, (int)(logo.getIconWidth() * scaleImage) + 10,
          (int)((IMAGE_HEIGHT + g2d.getFontMetrics().getHeight()) * 0.5));

    }
    else {
      int titleWidth = g2d.getFontMetrics().stringWidth(title);
      g2d.drawString(title, (int)((pf.getImageableWidth() - titleWidth) * 0.5),
          (int)((IMAGE_HEIGHT + g2d.getFontMetrics().getHeight()) * 0.5));
    }

    g2d.setFont(tmpFont);

    int yPostion = (int)IMAGE_HEIGHT;

    Stroke tmp = g2d.getStroke();
    g2d.setStroke(headerLineStroke);
    g2d.setColor(Color.BLACK);
    g2d.drawLine(0, yPostion, (int)+pf.getImageableWidth(), yPostion);
    g2d.setStroke(tmp);
    g2d.dispose();

    return Printable.PAGE_EXISTS;
  }
}
