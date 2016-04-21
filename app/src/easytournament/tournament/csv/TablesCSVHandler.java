package easytournament.tournament.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.tournament.calc.Values;
import easytournament.tournament.valueholder.TableEntry;



public class TablesCSVHandler {

  public static void saveTables(File filename, ArrayList<AbstractGroup> groups)
      throws FileNotFoundException {

    PrintStream out = null;
    try {
      out = new PrintStream(new FileOutputStream(filename));

      for (AbstractGroup g : groups) {
        out.println(g.getName());

        out.print(ResourceManager.getText(Text.TEAM));
        out.print(";");
        out.print(ResourceManager.getText(Text.GAME_SHORT));
        out.print(";");
        out.print(ResourceManager.getText(Text.WINS_SHORT));
        out.print(";");
        out.print(ResourceManager.getText(Text.DRAWS_SHORT));
        out.print(";");
        out.print(ResourceManager.getText(Text.DEFEATS_SHORT));
        out.print(";");
        out.print(ResourceManager.getText(Text.POINTS_SHORT));
        out.print(";");
        out.print("+");
        out.print(";");
        out.print("-");
        out.print(";");
        out.println("+/-");

        for (TableEntry e : g.getTable()) {
          out.print(e.getTeam().getName());
          out.print(";");
          out.print(e.getValue(Values.HOME_WINS) + e.getValue(Values.AWAY_WINS)
              + e.getValue(Values.HOME_DRAWS) + e.getValue(Values.AWAY_DRAWS)
              + e.getValue(Values.HOME_DEFEATS)
              + e.getValue(Values.AWAY_DEFEATS));
          out.print(";");
          out.print(e.getValue(Values.HOME_WINS) + e.getValue(Values.AWAY_WINS));
          out.print(";");
          out.print(e.getValue(Values.HOME_DRAWS)
              + e.getValue(Values.AWAY_DRAWS));
          out.print(";");
          out.print(e.getValue(Values.HOME_DEFEATS)
              + e.getValue(Values.AWAY_DEFEATS));
          out.print(";");
          out.print(e.getValue(Values.HOME_POINTS)
              + e.getValue(Values.AWAY_POINTS));
          out.print(";");
          out.print(e.getValue(Values.SCORED_AWAY_GOALS)
              + e.getValue(Values.SCORED_HOME_GOALS));
          out.print(";");
          out.print(e.getValue(Values.RECEIVED_AWAY_GOALS)
              + e.getValue(Values.RECEIVED_HOME_GOALS));
          out.print(";");
          out.println(e.getValue(Values.SCORED_AWAY_GOALS)
              + e.getValue(Values.SCORED_HOME_GOALS)
              - e.getValue(Values.RECEIVED_AWAY_GOALS)
              - e.getValue(Values.RECEIVED_HOME_GOALS));
        }
        out.println();
      }
    }
    finally {
      if (out != null)
        out.close();
    }

  }
}
