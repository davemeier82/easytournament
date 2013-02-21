package com.easytournament.designer.importable;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.Element;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.Organizer;
import com.easytournament.basic.export.Importable;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.basic.xml.XMLHandler;
import com.easytournament.designer.navigationitem.DesignerItem;


public class PlanImportable implements Importable {

  @Override
  public void doImport() {

    int answer = JOptionPane.showConfirmDialog(Organizer.getInstance()
        .getMainFrame(), ResourceManager.getText(Text.LOOSE_DIAG_DATA_MSG),
        ResourceManager.getText(Text.LOOSE_DATA), JOptionPane.WARNING_MESSAGE);
    if (answer != JFileChooser.APPROVE_OPTION)
      return;

    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".xml")
            || f.getName().toLowerCase().endsWith(".ett") || f.isDirectory();
      }

      public String getDescription() {
        return "ETT (*.ett), XML (*.xml)";
      }
    });

    answer = chooser.showOpenDialog(Organizer.getInstance().getMainFrame());
    if (answer == JFileChooser.APPROVE_OPTION) {
      Document doc;
      try {
        doc = XMLHandler.openXMLDoc(chooser.getSelectedFile());
      }
      catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(Organizer.getInstance()
            .getMainFrame(), ResourceManager
            .getText(Text.FILE_NOT_FOUND), ResourceManager
            .getText(Text.FILE_NOT_FOUND), JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (doc != null) {

        Element filetype = doc.getRootElement();
        String app = filetype.getAttributeValue("application");
        if (app == null || !app.equals(MetaInfos.FILE_APPLICATION)) {
          showFileNotSupported();
          return;
        }

        String type = filetype.getAttributeValue("type");
        if (type == null
            || !(type.equals(MetaInfos.FILE_PLANFILETYPE) || type
                .equals(MetaInfos.FILE_MAINFILETYPE))) {
          showFileNotSupported();
          return;
        }
        String version = filetype.getAttributeValue("version");
        if (MetaInfos.compareVersionNr(MetaInfos.getXMLFileVersion(), version) < 0) {
          JOptionPane.showMessageDialog(
              Organizer.getInstance().getMainFrame(),
              ResourceManager.getText(Text.NEW_VERSION1) + MetaInfos.APP_NAME
                  + ResourceManager.getText(Text.NEW_VERSION2)
                  + MetaInfos.APP_WEBSITE
                  + ResourceManager.getText(Text.NEW_VERSION3),
              ResourceManager.getText(Text.NEW_VERSION),
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        Tournament t = Organizer.getInstance().getCurrentTournament();
        t.setUnassignedteams(t.getTeams());
        for (Team tm : t.getTeams()) {
          tm.clearGroups();
        }
        t.getPlan().getGroups().clear();
        t.getSchedules().clear();
        DesignerItem.openPlan(doc.getRootElement(), true);
      }
      else {
        showFileNotSupported();
      }

    }

  }

  public void showFileNotSupported() {
    JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
        ResourceManager.getText(Text.FILE_NOT_SUPPORTED),
        ResourceManager.getText(Text.FILE_NOT_SUPPORTED),
        JOptionPane.ERROR_MESSAGE);
  }

}
