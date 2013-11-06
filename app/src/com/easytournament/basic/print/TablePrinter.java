package com.easytournament.basic.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.swing.JTable;
import javax.swing.JTable.PrintMode;

public class TablePrinter implements Printable {

  private Printable content;
  private Header header = new Header();
  private Footer footer = new Footer();
  private PageFormat pageFormatJTable;

  public TablePrinter(JTable content, String subtitle) {
    this.content = content.getPrintable(PrintMode.FIT_WIDTH, new MessageFormat(
        subtitle), null);
  }

  @Override
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
      throws PrinterException {

    Graphics2D g2d = (Graphics2D)graphics;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

    header.print(graphics, pageFormat, pageIndex);

    if (pageFormatJTable == null) {
      pageFormatJTable = (PageFormat)pageFormat.clone();
      Paper p = pageFormatJTable.getPaper();
      if (pageFormatJTable.getOrientation() == PageFormat.PORTRAIT)
        p.setImageableArea(pageFormat.getImageableX(),
            pageFormat.getImageableY() + header.getHeight() + 10,
            pageFormat.getImageableWidth(), pageFormat.getImageableHeight()
                - header.getHeight() - 40 - footer.getHeight());
      else
        p.setImageableArea(
            pageFormat.getImageableY() + header.getHeight() + 10,
            pageFormat.getImageableX(), pageFormat.getImageableHeight()
                - header.getHeight() - 30 - footer.getHeight(),
            pageFormat.getImageableWidth());
      pageFormatJTable.setPaper(p);
    }

    Graphics gCopy = graphics.create();
    int success = content.print(gCopy, pageFormatJTable, pageIndex);
    gCopy.dispose();

    footer.print(graphics, pageFormat, pageIndex);

    return success;
  }
}
