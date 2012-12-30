package com.easytournament.tournament.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.ArrayList;

import com.easytournament.basic.html.HtmlHandler;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.settings.ScheduleSettings;
import com.easytournament.designer.valueholder.ScheduleEntry;



public class GamesHTMLHandler {
  public static void saveGames(File filename,
      ArrayList<ScheduleEntry> entries) throws FileNotFoundException {

    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
        ResourceManager.getLocale());
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT,
        ResourceManager.getLocale());

    PrintStream out = null;
    try {
      out = new PrintStream(new FileOutputStream(filename));
      out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
      out.println("<html>");
      HtmlHandler.writeHtmlHead(out);
      out.print("<body>\n<h2>");
      out.print(ResourceManager.getText(Text.GAMES));
      out.println("</h2>");
      
      out.print("<table>\n\t<tr>\n\t\t<th>");
      out.print(ResourceManager.getText(Text.HOME));
      out.print("</th>\n\t\t<th></th>\n\t\t<th></th>\n\t\t<th>");
      out.print(ResourceManager.getText(Text.AWAY));
      out.print("</th>\n\t\t<th>");
      out.print(ResourceManager.getText(Text.PLACE));
      out.print("</th>\n\t\t<th>");
      out.print(ResourceManager.getText(Text.DATE));
      out.print("</th>\n\t\t<th>");
      out.print(ResourceManager.getText(Text.TIME));
      boolean showRef = ScheduleSettings.getInstance().isShowRefrees();
      if (showRef) {
        out.print("</th>\n\t\t<th>");
        out.print(ResourceManager.getText(Text.REFREE));
      }
      out.println("</th>\n\t</tr>");

      int i = 0;
      for (ScheduleEntry se : entries) {
        String style = i % 2 == 1? ""
            : " style=\"background-color:#dadada\"";
        out.println("\t<tr>");
        out.print("\t\t<td"+ style + ">");
        if(se.getHomeTeam() == null)
          out.print(se.getHomePos().getName());
        else
          out.print(se.getHomeTeam().getName());
        out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
        if(se.isGamePlayed())
          out.print(se.getHomeScore());
        out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
        if(se.isGamePlayed())
          out.print(se.getAwayScore());
        out.print("</td>\n\t\t<td"+ style + ">");
        if(se.getAwayTeam() == null)
          out.print(se.getAwayPos().getName());
        else
          out.print(se.getAwayTeam().getName());
        out.print("</td>\n\t\t<td"+ style + ">");
        out.print(se.getPlace());
        out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
        out.print(dateFormatter.format(se.getDate().getTime()));
        out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
        out.print(timeFormatter.format(se.getDate().getTime()));
        if (showRef) {
          out.print("</td>\n\t\t<td"+ style + ">");
          if (se.getReferees().getSize() > 0)
            out.print(se.getReferees().get(0).toString());
        }
        out.println("</td>");
        out.println("\t</tr>");
        i++;
      }
      out.println("</table>\n</body>\n</html>");
    }
    finally {
      if (out != null)
        out.close();
    }

  }
}
