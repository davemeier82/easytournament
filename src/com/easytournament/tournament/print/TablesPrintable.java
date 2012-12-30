package com.easytournament.tournament.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.ExtendedTablePrintable;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;

import com.easytournament.basic.print.Footer;
import com.easytournament.basic.print.Header;
import com.easytournament.designer.valueholder.AbstractGroup;



public class TablesPrintable implements Printable {

  private static final int SPACING = 80;
  protected ArrayList<JTable> tables;
  ArrayList<String> tableNames = new ArrayList<String>();
  protected int currentIdx = 0;
  protected Printable current;
  protected JTable table;
  protected PageFormat pageFormatJTable;
  private Header header = new Header();
  private Footer footer = new Footer();
  private int currentPageIndex = 0;
  private int printedCount = 0;

  public TablesPrintable(ArrayList<JTable> tables,
      ArrayList<AbstractGroup> groups) {
    this.tables = tables;
    for (AbstractGroup g : groups)
      tableNames.add(g.getName());
    this.table = tables.get(0);
    this.current = this.table.getPrintable(PrintMode.FIT_WIDTH, null, null);
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

    if (currentPageIndex != pageIndex) {
      currentPageIndex = pageIndex;
      printedCount = ++currentIdx;
      if (currentIdx >= tables.size())
        return NO_SUCH_PAGE;
    }
    this.currentIdx = printedCount;

    this.table = tables.get(currentIdx);
    this.current = new ExtendedTablePrintable(table, PrintMode.FIT_WIDTH,
        new MessageFormat(this.tableNames.get(currentIdx)), null);

    pageFormatJTable = (PageFormat)pageFormat.clone();
    Paper p = pageFormatJTable.getPaper();
    if (pageFormatJTable.getOrientation() == PageFormat.PORTRAIT)
      p.setImageableArea(
          pageFormat.getImageableX(),
          pageFormat.getImageableY() + header.getHeight() + 20,
          pageFormat.getImageableWidth(),
          pageFormat.getImageableHeight() - header.getHeight() - 80
              - footer.getHeight());
    else
      p.setImageableArea(
          pageFormat.getImageableY() + header.getHeight() + 20,
          pageFormat.getImageableX(),
          pageFormat.getImageableHeight() - header.getHeight() - 80
              - footer.getHeight(), pageFormat.getImageableWidth());
    pageFormatJTable.setPaper(p);
    pageFormatJTable.setPaper(p);

    Graphics gCopy = graphics.create();
    int hasPage = current.print(gCopy, pageFormatJTable, 0);
    gCopy.dispose();

    int y = (int)table.getPreferredSize().getHeight() + SPACING;
    while (currentIdx < tables.size() - 1
        && y + tables.get(currentIdx + 1).getPreferredSize().getHeight() < pageFormatJTable
            .getImageableHeight()) {

      if (pageFormatJTable.getOrientation() == PageFormat.PORTRAIT)
        p.setImageableArea(pageFormat.getImageableX(),
            pageFormat.getImageableY() + header.getHeight() + 20 + y,
            pageFormat.getImageableWidth(), pageFormat.getImageableHeight()
                - header.getHeight() - 80 - footer.getHeight());
      else
        p.setImageableArea(pageFormat.getImageableY() + header.getHeight() + 20
            + y, pageFormat.getImageableX(), pageFormat.getImageableHeight()
            - header.getHeight() - 80 - footer.getHeight(),
            pageFormat.getImageableWidth());
      pageFormatJTable.setPaper(p);

      currentIdx++;
      table = tables.get(currentIdx);
      current = table.getPrintable(PrintMode.FIT_WIDTH, new MessageFormat(
          this.tableNames.get(currentIdx)), null);
      y += table.getPreferredSize().getHeight() + SPACING;
      gCopy = graphics.create();

      hasPage = current.print(graphics, pageFormatJTable, 0);
      gCopy.dispose();
    }

    footer.print(graphics, pageFormat, pageIndex);

    return hasPage;
  }

}
