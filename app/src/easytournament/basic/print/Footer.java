package easytournament.basic.print;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.DateFormat;
import java.util.Date;

import easytournament.basic.MetaInfos;
import easytournament.basic.resources.ResourceManager;


public class Footer implements Printable {

  private String dateString;

  public Footer() {

    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
        ResourceManager.getLocale());
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT,
        ResourceManager.getLocale());
    Date date = new Date();
    dateString = dateFormatter.format(date) + " " + timeFormatter.format(date);
  }

  public int getHeight() {
    return 30;
  }

  @Override
  public int print(Graphics graphics, PageFormat pf, int pageIndex)
      throws PrinterException {

    Graphics2D g2d = (Graphics2D)graphics.create();
    g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 10.f));
    int lineHeight = g2d.getFontMetrics().getHeight();

    g2d.drawString(dateString, 10,
        (int)(pf.getImageableHeight() - lineHeight) - 10);

    String pageString = "- " + (pageIndex + 1) + " -";
    int strWidth = g2d.getFontMetrics().stringWidth(pageString);
    g2d.drawString(pageString,
        (int)((pf.getImageableWidth() - strWidth) * 0.5),
        (int)(pf.getImageableHeight() - lineHeight) - 10);

    strWidth = g2d.getFontMetrics().stringWidth(MetaInfos.APP_WEBSITE);
    g2d.drawString(MetaInfos.APP_WEBSITE, (int)(pf.getImageableWidth()
        - strWidth - 5), (int)(pf.getImageableHeight() - lineHeight) - 10);
    graphics.dispose();

    return Printable.PAGE_EXISTS;
  }
}
