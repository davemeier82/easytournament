package com.easytournament.statistic.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.table.DefaultTableModel;

import com.easytournament.basic.html.HtmlHandler;


public class StatisticHTMLHandler {

  public static void saveDefaultStat(File filename, DefaultTableModel tm,
      String subtitle) throws FileNotFoundException {
    PrintStream out = null;
    try {
      out = new PrintStream(new FileOutputStream(filename));
      out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
      out.println("<html>");
      HtmlHandler.writeHtmlHead(out);
      out.print("<body>\n<h2>");
      out.print(subtitle);
      out.println("</h2>");

      out.println("<table>\n\t<tr>\n\t\t<th>");
      for (int col = 0; col < tm.getColumnCount(); col++) {
        if (col != 0)
          out.print("</th>\n\t\t<th>");
        out.print(tm.getColumnName(col));
      }
      out.println("</th>\n\t</tr>");
      for (int row = 0; row < tm.getRowCount(); row++) {
        out.println("\t<tr>");
        for (int col = 0; col < tm.getColumnCount(); col++) {
          String style = row % 2 == 1? ""
              : " style=\"background-color:#dadada\"";
          Object obj = tm.getValueAt(row, col);
          if (obj instanceof Integer)
            out.print("\t\t<td align=\"center\"" + style + ">");
          else
            out.print("\t\t<td" + style + ">");
          out.print(tm.getValueAt(row, col));
          out.println("</td>");
        }
        out.println("\t</tr>");
      }
      out.println("</table>");
      out.println("</body>\n</html>");
    }
    finally {
      if (out != null)
        out.close();
    }
  }

}
