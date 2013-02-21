package com.easytournament.basic.model;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import org.jdom.Document;
import org.jdom.Element;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ImportListDialog;
import com.easytournament.basic.model.dialog.ImportListDialogPModel;
import com.easytournament.basic.model.dialog.RefreeDialogPModel;
import com.easytournament.basic.model.tablemodel.RefreeImportTableModel;
import com.easytournament.basic.model.tablemodel.RefreeTableModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.comperator.PersonNameComperator;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.xml.RefreeXMLHandler;
import com.easytournament.basic.xml.XMLHandler;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

public class AssistantsTabPanelPModel extends Model {

  public static final int NEW_ASSI_ACTION = 0;
  public static final int IMPORT_ASSI_ACTION = 1;
  public static final int EXPORT_ASSI_ACTION = 2;
  public static final int EDIT_STAFF_ACTION = 3;
  public static final int DELETE_ASSI_ACTION = 4;
  public static final String PROPERTY_SELECTIONINLIST = "selectionInList";
  public static final String PROPERTY_SHOW_ASSISTDIALOG = "showAssistantDialog";

  private RefreeTableModel refreeTModel;
  private ListSelectionModel selectionmodel = new DefaultListSelectionModel();
  private ArrayListModel<Refree> refrees;
  private Refree refree;
  private PersonNameComperator pnc = new PersonNameComperator();

  public AssistantsTabPanelPModel(Refree refree) {
    this.refree = refree;
    this.retrieveData();
  }

  private void retrieveData() {
    refrees = refree.getAssistants();
    Collections.sort(refrees, pnc);
    refreeTModel = new RefreeTableModel(refrees, true);
  }

  public void sortAssistants() {
    Collections.sort(refrees, pnc);
    refreeTModel.fireTableDataChanged();
  }

  public RefreeDialogPModel getAssistantDModel(int row) {
    if (row >= 0)
      return new RefreeDialogPModel(refrees.get(row), this.refree);
    return null;
  }

  public void deleteAction() {
    int min = selectionmodel.getMinSelectionIndex();
    int max = selectionmodel.getMaxSelectionIndex();
    if (min >= 0) {
      for (int i = min; i <= max; i++) {
        refreeTModel.removeRow(min);
      }
    }
  }

  public TableModel getTableModel() {
    return refreeTModel;
  }

  public Action getAction(int action) {
    switch (action) {
      case NEW_ASSI_ACTION:
        return new NewTeamAction();
      case IMPORT_ASSI_ACTION:
        return new ImportTeamAction();
      case EXPORT_ASSI_ACTION:
        return new ExportTeamAction();
      case DELETE_ASSI_ACTION:
        return new DeleteTeamAction();
      default:
        return null;
    }
  }

  class NewTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public NewTeamAction() {
      super(ResourceManager.getText(Text.ADD_ASSISTANT), ResourceManager
          .getIcon(Icon.ADDPERSON_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      AssistantsTabPanelPModel.this.firePropertyChange(
          PROPERTY_SHOW_ASSISTDIALOG, null, new RefreeDialogPModel(
              AssistantsTabPanelPModel.this.refree, true));
    }
  }

  class ImportTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public ImportTeamAction() {
      super(ResourceManager.getText(Text.IMPORT_ASSISTANT));
    }

    public void actionPerformed(ActionEvent e) {
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
      int answer = chooser.showOpenDialog(Organizer.getInstance()
          .getMainFrame());

      if (answer == JFileChooser.APPROVE_OPTION) {
        Document doc;
        try {
          doc = XMLHandler.openXMLDoc(chooser.getSelectedFile());
        }
        catch (FileNotFoundException e1) {
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
            JOptionPane.showMessageDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager
                .getText(Text.FILE_NOT_SUPPORTED), ResourceManager
                .getText(Text.FILE_NOT_SUPPORTED), JOptionPane.ERROR_MESSAGE);
            return;
          }

          String type = filetype.getAttributeValue("type");
          if (type == null
              || !(type.equals(MetaInfos.FILE_REFREEFILETYPE)
                  || type.equals(MetaInfos.FILE_ASSISTFILETYPE) || type
                    .equals(MetaInfos.FILE_MAINFILETYPE))) {
            JOptionPane.showMessageDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager
                .getText(Text.FILE_NOT_SUPPORTED), ResourceManager
                .getText(Text.FILE_NOT_SUPPORTED), JOptionPane.ERROR_MESSAGE);
            return;
          }
          String version = filetype.getAttributeValue("version");
          if (MetaInfos
              .compareVersionNr(MetaInfos.getXMLFileVersion(), version) < 0) {
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
          ArrayList<Refree> irefree;
          if (type.equals("assistants")) {
            irefree = RefreeXMLHandler.importAssistants(filetype);
          }
          else {
            Element refEl = filetype.getChild("refrees");
            List<Element> refEls = refEl.getChildren("refree");
            irefree = new ArrayList<Refree>();
            for (Element el : refEls) {
              irefree.addAll(RefreeXMLHandler.importAssistants(el));
            }
          }

          if (irefree.size() < 0) {
            JOptionPane.showMessageDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager
                .getText(Text.NO_PERS_FOUND_MSG), ResourceManager
                .getText(Text.NO_PERS_FOUND), JOptionPane.ERROR_MESSAGE);
            return;
          }
          ArrayList<Boolean> selection = new ArrayList<Boolean>(irefree.size());
          for (int i = 0; i < irefree.size(); i++)
            selection.add(new Boolean(true));

          ImportListDialogPModel ilpm = new ImportListDialogPModel(
              new RefreeImportTableModel(irefree, selection));

          new ImportListDialog(Organizer.getInstance().getMainFrame(),
              ResourceManager.getText(Text.SELECT_PERSON_IMPORT), true, ilpm);

          if (ilpm.getAnswer()) {
            for (int i = 0; i < irefree.size(); i++) {
              if (selection.get(i)) {
                Refree t = irefree.get(i);
                t.updateId();
                refrees.add(t);
              }
            }
            Collections.sort(refrees, pnc);
          }
        }
      }
    }
  }

  class ExportTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public ExportTeamAction() {
      super(ResourceManager.getText(Text.EXPORT_ASSISTANTS));
    }

    public void actionPerformed(ActionEvent e) {
      if (selectionmodel.getMinSelectionIndex() < 0) {
        JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.SELECT_PERSON_EXPORT),
            ResourceManager.getText(Text.NO_PERS_SELECTED),
            JOptionPane.INFORMATION_MESSAGE);

      }
      else {
        ArrayList<Refree> staffToExport = new ArrayList<Refree>();
        int min = selectionmodel.getMinSelectionIndex();
        int max = selectionmodel.getMaxSelectionIndex();
        if (min >= 0) {
          for (int i = min; i <= max; i++) {
            if (selectionmodel.isSelectedIndex(i)) {
              staffToExport.add(refreeTModel.getPersonAt(i));
            }
          }
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {
          public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".xml")
                || f.isDirectory();
          }

          public String getDescription() {
            return "XML (*.xml)";
          }
        });
        int answer = chooser.showSaveDialog(Organizer.getInstance()
            .getMainFrame());
        if (answer == JFileChooser.APPROVE_OPTION) {
          File filename = chooser.getSelectedFile();

          if (filename != null) {
            if (!filename.getPath().toLowerCase().endsWith(".xml")) {
              filename = new File(filename.getPath() + ".xml");
            }
            Element filetype = new Element("filetype");
            filetype.setAttribute("application", MetaInfos.FILE_APPLICATION);
            filetype.setAttribute("type", MetaInfos.FILE_ASSISTFILETYPE);
            filetype.setAttribute("version", MetaInfos.getXMLFileVersion());

            RefreeXMLHandler.exportAssistants(filetype, staffToExport);
            XMLHandler.saveXMLDoc(new Document(filetype), filename);
          }
        }
      }
    }

  }

  class DeleteTeamAction extends AbstractAction {

    private static final long serialVersionUID = 8990437935813416417L;

    public DeleteTeamAction() {
      super(ResourceManager.getText(Text.DELETE_ASSISTANTS), ResourceManager
          .getIcon(Icon.DELETE_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      AssistantsTabPanelPModel.this.deleteAction();
    }
  }

  public ListSelectionModel getSelectionModel() {
    return selectionmodel;
  }
}
