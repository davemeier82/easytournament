package com.easytournament.tournament.export;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.export.Exportable;
import com.easytournament.basic.navigationitem.NavigationItem;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.tournament.csv.TablesCSVHandler;
import com.easytournament.tournament.html.TablesHTMLHandler;

public class TablesExportable implements Exportable {

  private NavigationItem module;

  public TablesExportable(NavigationItem module) {
    this.module = module;
  }

  @Override
  public void doExport(boolean activeModule) {
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
            TablesHTMLHandler.saveTables(filename, Organizer.getInstance()
                .getCurrentTournament().getPlan().getOrderedGroups());
          }
          else {
            if (!filename.getPath().toLowerCase().endsWith(".csv")) {
              filename = new File(filename.getPath() + ".csv");
            }
            TablesCSVHandler.saveTables(filename, Organizer.getInstance()
                .getCurrentTournament().getPlan().getOrderedGroups());
          }
        }
        catch (FileNotFoundException e1) {
          JOptionPane.showInternalMessageDialog(Organizer.getInstance()
              .getMainFrame(), ResourceManager
              .getText(Text.COULD_NOT_SAVE_TABLES), ResourceManager
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
