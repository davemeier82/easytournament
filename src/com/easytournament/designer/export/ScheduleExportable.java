package com.easytournament.designer.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.export.Exportable;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.csv.ScheduleCSVHandler;
import com.easytournament.designer.html.ScheduleHTMLHandler;
import com.easytournament.designer.valueholder.ScheduleEntry;

public class ScheduleExportable implements Exportable {

  @Override
  public void doExport() {
    export(Organizer.getInstance().getCurrentTournament().getSchedules(), false, "");
  }

public void export(List<ScheduleEntry> schedule, boolean showTeams, String titleExtension) {
	JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
      }

      public String getDescription() {
        return "CSV (*.csv)";
      }
    });
    FileFilter htmlfilter = new FileFilter() {

      @Override
      public String getDescription() {
        return "HTML (*.html)";
      }

      @Override
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".html") || f.isDirectory();
      }
    };
    chooser.addChoosableFileFilter(htmlfilter);
    int answer = chooser.showSaveDialog(Organizer.getInstance().getMainFrame());
    if (answer == JFileChooser.APPROVE_OPTION) {
      File filename = chooser.getSelectedFile();

      if (filename != null) {
        try {
          if (chooser.getFileFilter().equals(htmlfilter)) {
            if (!filename.getPath().toLowerCase().endsWith(".html")) {
              filename = new File(filename.getPath() + ".html");
            }
            ScheduleHTMLHandler.saveSchedule(filename, schedule, showTeams, titleExtension);
          }
          else {
            if (!filename.getPath().toLowerCase().endsWith(".csv")) {
              filename = new File(filename.getPath() + ".csv");
            }
            ScheduleCSVHandler.saveSchedule(filename, schedule, showTeams);
          }
        }
        catch (FileNotFoundException e1) {
          JOptionPane.showInternalMessageDialog(Organizer.getInstance()
              .getMainFrame(), ResourceManager.getText(Text.COULD_NOT_SAVE_SCHEDULE), ResourceManager.getText(Text.ERROR),
              JOptionPane.ERROR_MESSAGE);
        }

      }
    }
}

}
