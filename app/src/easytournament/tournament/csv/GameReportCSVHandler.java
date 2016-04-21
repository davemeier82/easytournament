package easytournament.tournament.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.GameEventEntry;
import easytournament.basic.valueholder.Player;
import easytournament.designer.valueholder.ScheduleEntry;

public class GameReportCSVHandler {

  public static void saveGameReport(File filename,
      ArrayListModel<GameEventEntry> data, ScheduleEntry sentry)
      throws FileNotFoundException {
    PrintStream out = null;
    try {
      out = new PrintStream(new FileOutputStream(filename));

      out.print(ResourceManager.getText(Text.EVENT));
      out.print(";");
      out.print(ResourceManager.getText(Text.TIME));
      out.print(";");
      out.print(sentry.getHomeTeam().getName());
      out.print(";;;");
      out.println(sentry.getAwayTeam().getName());

      for (GameEventEntry e : data) {
        out.print(e.getEvent().getName());
        out.print(";");
        String time = e.getTimeMin() + ":";
        if (e.getTimeSec() < 10)
          time += "0";
        out.print(time.concat(e.getTimeSec() + ""));
        out.print(";");
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
        out.print(";");
        out.print(e.getSummedHomePoints());
        out.print(";");
        out.print(e.getSummedAwayPoints());
        out.print(";");
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
        out.println(name);
      }
    }
    finally {
      if (out != null)
        out.close();
    }

  }
}
