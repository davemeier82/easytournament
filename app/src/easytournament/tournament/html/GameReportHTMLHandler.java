package easytournament.tournament.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.html.HtmlHandler;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.GameEventEntry;
import easytournament.basic.valueholder.Player;
import easytournament.designer.valueholder.ScheduleEntry;

public class GameReportHTMLHandler {
  public static void saveGameReport(File filename, ArrayListModel<GameEventEntry> data, ScheduleEntry sentry) throws FileNotFoundException{
    PrintStream out = null;
    try {
        out = new PrintStream(new FileOutputStream(filename));
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
        out.println("<html>");
        HtmlHandler.writeHtmlHead(out);
        out.print("<body>\n<h2>");
        out.print(ResourceManager.getText(Text.GAMEREPORT));
        out.println("</h2>");
        
        out.print("<table>\n\t<tr>\n\t\t<th>");        
        out.print(ResourceManager.getText(Text.EVENTS));
        out.print("</th>\n\t\t<th>");
        out.print(ResourceManager.getText(Text.TIME));
        out.print("</th>\n\t\t<th>");
        out.print(sentry.getHomeTeam().getName());
        out.print("</th>\n\t\t<th></th>\n\t\t<th></th>\n\t\t<th>");
        out.print(sentry.getAwayTeam().getName());
        out.println("</th>\n\t</tr>");
        
        int i = 0;
        for (GameEventEntry e : data) {
          String style = i % 2 == 1? ""
              : " style=\"background-color:#dadada\"";
          out.println("\t<tr>");
          out.print("\t\t<td"+ style + ">");
          out.print(e.getEvent().getName());
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          String time = e.getTimeMin() + ":";
          if (e.getTimeSec() < 10)
            time += "0";
          out.print(time.concat(e.getTimeSec() + ""));
          out.print("</td>\n\t\t<td"+ style + ">");
          String name = "";
          if (e.getTeam().equals(sentry.getHomeTeam()) && e.getMainPlayers().size() > 0) {
            Player p = e.getMainPlayers().get(0);
            if (p != null) {
              name = p.getNumber().length() > 0? "[" + p.getNumber() + "] " : "";
              if (p.getPrename().length() > 0)
                name += p.getPrename().substring(0, 1) + ". ";
              name += p.getName();
            }
          }
          out.print(name);
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getSummedHomePoints());
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getSummedAwayPoints());
          out.print("</td>\n\t\t<td"+ style + ">");
          name = "";
          if (!e.getTeam().equals(sentry.getHomeTeam()) && e.getMainPlayers().size() > 0) {
            Player p = e.getMainPlayers().get(0);
            if (p != null) {
              name = p.getNumber().length() > 0? "[" + p.getNumber() + "] " : "";
              if (p.getPrename().length() > 0)
                name += p.getPrename().substring(0, 1) + ". ";
              name += p.getName();
            }           
          }
          out.print(name);
          out.println("</td>\n\t</tr>");
          i++;
        }
        out.println("</table>\n</body>\n</html>");

    }
    finally {
        if (out != null) out.close();
    }

  }
}
