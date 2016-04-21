package easytournament.designer.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import easytournament.basic.Organizer;
import easytournament.basic.export.ExportTriggerable;
import easytournament.basic.export.Exportable;
import easytournament.basic.navigationitem.NavigationItem;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.designer.csv.ScheduleCSVHandler;
import easytournament.designer.html.ScheduleHTMLHandler;
import easytournament.designer.valueholder.ScheduleEntry;

public class ScheduleExportable implements Exportable {

  private NavigationItem module;
  private ExportTriggerable exportable;

  public ScheduleExportable(NavigationItem module, ExportTriggerable exportable) {
    this.module = module;
    this.exportable = exportable;
  }

  @Override
  public void doExport(boolean activeModule) {
    if(activeModule)
    {
      this.exportable.triggerExport();
    } else {
      export(Organizer.getInstance().getCurrentTournament().getSchedules(),
        false, "");
    }
    
  }

  public void export(List<ScheduleEntry> schedule, boolean showTeams,
      String titleExtension) {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
      }

      @Override
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
            ScheduleHTMLHandler.saveSchedule(filename, schedule, showTeams,
                titleExtension);
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
              .getMainFrame(), ResourceManager
              .getText(Text.COULD_NOT_SAVE_SCHEDULE), ResourceManager
              .getText(Text.ERROR), JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }  

  @Override
  public NavigationItem getModule() {
    return this.module;
  }

}
