package com.easytournament.tournament.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import com.easytournament.basic.html.HtmlHandler;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.tournament.calc.Values;
import com.easytournament.tournament.valueholder.TableEntry;



public class TablesHTMLHandler {
  public static void saveTables(File filename, ArrayList<AbstractGroup> groups) throws FileNotFoundException {

    PrintStream out = null;
    try {
      out = new PrintStream(new FileOutputStream(filename));
      out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
      out.println("<html>");
      HtmlHandler.writeHtmlHead(out);
      out.print("<body>\n<h1>");
      out.print(ResourceManager.getText(Text.TABLES));
      out.println("</h1>");
      
      
      for (AbstractGroup g : groups) {
        out.print("<h2>");
        out.print(g.getName());
        out.println("</h2>");
        
        out.print("<table>\n\t<tr>\n\t\t<th>");
        out.print(ResourceManager.getText(Text.TEAM));
        out.print("</th>\n\t\t<th>");
        out.print(ResourceManager.getText(Text.GAME_SHORT));
        out.print("</th>\n\t\t<th>");
        out.print(ResourceManager.getText(Text.WINS_SHORT));
        out.print("</th>\n\t\t<th>");
        out.print(ResourceManager.getText(Text.DRAWS_SHORT));
        out.print("</th>\n\t\t<th>");
        out.print(ResourceManager.getText(Text.DEFEATS_SHORT));
        out.print("</th>\n\t\t<th>");
        out.print(ResourceManager.getText(Text.POINTS_SHORT));
        out.print("</th>\n\t\t<th>");
        out.print("+");
        out.print("</th>\n\t\t<th>");
        out.print("-");
        out.print("</th>\n\t\t<th>");
        out.print("+/-");
        out.println("</th>\n\t</tr>");

        int i = 0;
        for (TableEntry e : g.getTable()) {
          String style = i % 2 == 1? ""
              : " style=\"background-color:#dadada\"";
          out.println("\t<tr>");
          out.print("\t\t<td"+ style + ">");
          out.print(e.getTeam().getName());
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getValue(Values.HOME_WINS) + e.getValue(Values.AWAY_WINS)
              + e.getValue(Values.HOME_DRAWS) + e.getValue(Values.AWAY_DRAWS)
              + e.getValue(Values.HOME_DEFEATS)
              + e.getValue(Values.AWAY_DEFEATS));
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getValue(Values.HOME_WINS) + e.getValue(Values.AWAY_WINS));
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getValue(Values.HOME_DRAWS)
              + e.getValue(Values.AWAY_DRAWS));
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getValue(Values.HOME_DEFEATS)
              + e.getValue(Values.AWAY_DEFEATS));
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getValue(Values.HOME_POINTS)
              + e.getValue(Values.AWAY_POINTS));
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getValue(Values.SCORED_AWAY_GOALS)
              + e.getValue(Values.SCORED_HOME_GOALS));
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getValue(Values.RECEIVED_AWAY_GOALS)
              + e.getValue(Values.RECEIVED_HOME_GOALS));
          out.print("</td>\n\t\t<td align=\"center\""+ style + ">");
          out.print(e.getValue(Values.SCORED_AWAY_GOALS)
              + e.getValue(Values.SCORED_HOME_GOALS)
              - e.getValue(Values.RECEIVED_AWAY_GOALS)
              - e.getValue(Values.RECEIVED_HOME_GOALS));
          out.println("</td>");
          out.println("\t</tr>");
          i++;
        }
        out.println("</table>\n<br>");
      }
      out.println("</body>\n</html>");
    }
    finally {
      if (out != null)
        out.close();
    }

  }
}
