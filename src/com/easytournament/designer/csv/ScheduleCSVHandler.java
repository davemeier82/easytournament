package com.easytournament.designer.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.ArrayList;

import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.settings.ScheduleSettings;
import com.easytournament.designer.valueholder.ScheduleEntry;



public class ScheduleCSVHandler {

  public static void saveSchedule(File filename,
      ArrayList<ScheduleEntry> entries) throws FileNotFoundException {

    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
        ResourceManager.getLocale());
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT,
        ResourceManager.getLocale());

    PrintStream out = null;
    try {
      out = new PrintStream(new FileOutputStream(filename));
      out.print(ResourceManager.getText(Text.HOME));
      out.print(";");
      out.print(ResourceManager.getText(Text.AWAY));
      out.print(";");
      out.print(ResourceManager.getText(Text.PLACE));
      out.print(";");
      out.print(ResourceManager.getText(Text.DATE));
      out.print(";");
      out.print(ResourceManager.getText(Text.TIME));
      boolean showRef = ScheduleSettings.getInstance().isShowRefrees();
      if (showRef) {
        out.print(";");
        out.println(ResourceManager.getText(Text.REFREE));
      }
      else {
        out.println();
      }
      for (ScheduleEntry se : entries) {
        out.print(se.getHomePos().getName());
        out.print(";");
        out.print(se.getAwayPos().getName());
        out.print(";");
        out.print(se.getPlace());
        out.print(";");
        out.print(dateFormatter.format(se.getDate().getTime()));
        out.print(";");
        out.print(timeFormatter.format(se.getDate().getTime()));
        if (showRef) {
          out.print(";");
          if (se.getReferees().getSize() > 0)
            out.print(se.getReferees().get(0).toString());
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
