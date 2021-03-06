package easytournament.basic.model;

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

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.MetaInfos;
import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ImportListDialog;
import easytournament.basic.model.dialog.ImportListDialogPModel;
import easytournament.basic.model.dialog.StaffDialogPModel;
import easytournament.basic.model.tablemodel.StaffImportTableModel;
import easytournament.basic.model.tablemodel.StaffTableModel;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.comperator.PersonNameComperator;
import easytournament.basic.valueholder.Staff;
import easytournament.basic.valueholder.Team;
import easytournament.basic.xml.TeamsXMLHandler;
import easytournament.basic.xml.XMLHandler;

public class StaffTabPanelPModel extends Model {

  public static final int NEW_STAFF_ACTION = 0;
  public static final int IMPORT_STAFF_ACTION = 1;
  public static final int EXPORT_STAFF_ACTION = 2;
  public static final int EDIT_STAFF_ACTION = 3;
  public static final int DELETE_STAFF_ACTION = 4;
  public static final String PROPERTY_SELECTIONINLIST = "selectionInList";
  public static final String PROPERTY_SHOW_STAFFDIALOG = "showStaffDialog";

  private StaffTableModel staffTModel;
  private ListSelectionModel selectionmodel = new DefaultListSelectionModel();
  private ArrayListModel<Staff> staff;
  private Team team;
  private PersonNameComperator pnc = new PersonNameComperator();

  public StaffTabPanelPModel(Team team) {
    this.team = team;
    this.retrieveData();
  }

  private void retrieveData() {
    staff = team.getStaff();
    Collections.sort(staff, pnc);
    staffTModel = new StaffTableModel(staff);
  }

  public StaffDialogPModel getStaffDModel(int row) {
    if (row >= 0)
      return new StaffDialogPModel(staff.get(row));
    return null;
  }

  public void sortStaff() {
    Collections.sort(staff, pnc);
    staffTModel.fireTableDataChanged();
  }

  public void deleteAction() {
    int min = selectionmodel.getMinSelectionIndex();
    int max = selectionmodel.getMaxSelectionIndex();
    if (min >= 0) {
      for (int i = min; i <= max; i++) {
        staffTModel.removeRow(min);
      }
    }
  }

  public TableModel getTableModel() {
    return staffTModel;
  }

  public Action getAction(int action) {
    switch (action) {
      case NEW_STAFF_ACTION:
        return new NewTeamAction();
      case IMPORT_STAFF_ACTION:
        return new ImportTeamAction();
      case EXPORT_STAFF_ACTION:
        return new ExportTeamAction();
      case DELETE_STAFF_ACTION:
        return new DeleteTeamAction();
      default:
        return null;
    }
  }

  class NewTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public NewTeamAction() {
      super(ResourceManager.getText(Text.ADD_PERSON), ResourceManager
          .getIcon(Icon.ADDPERSON_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      StaffTabPanelPModel.this.firePropertyChange(PROPERTY_SHOW_STAFFDIALOG,
          null, new StaffDialogPModel(team));
    }

  }

  class ImportTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public ImportTeamAction() {
      super(ResourceManager.getText(Text.IMPORT_PERSON));
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
              || !(type.equals(MetaInfos.FILE_STAFFFILETYPE)
                  || type.equals(MetaInfos.FILE_TEAMFILETYPE) || type
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
          ArrayList<Staff> istaff;
          if (type.equals(MetaInfos.FILE_MAINFILETYPE)
              || type.equals(MetaInfos.FILE_TEAMFILETYPE)) {
            istaff = new ArrayList<Staff>();
            Element teamEl = filetype.getChild("teams");
            List<Element> teamEls = teamEl.getChildren("team");
            for (Element el : teamEls)
              istaff.addAll(TeamsXMLHandler.readStaff(el, true));
          }
          else {
            istaff = TeamsXMLHandler.readStaff(filetype, true);
          }
          if (istaff.size() < 0) {
            JOptionPane.showMessageDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager
                .getText(Text.NO_PERS_FOUND_MSG), ResourceManager
                .getText(Text.NO_PERS_FOUND), JOptionPane.ERROR_MESSAGE);
            return;
          }
          ArrayList<Boolean> selection = new ArrayList<Boolean>(istaff.size());
          for (int i = 0; i < istaff.size(); i++)
            selection.add(new Boolean(true));

          ImportListDialogPModel ilpm = new ImportListDialogPModel(
              new StaffImportTableModel(istaff, selection));

          new ImportListDialog(Organizer.getInstance().getMainFrame(),
              ResourceManager.getText(Text.SELECT_PERSON_IMPORT), true, ilpm);

          if (ilpm.getAnswer()) {
            for (int i = 0; i < istaff.size(); i++) {
              if (selection.get(i)) {
                Staff t = istaff.get(i);
                t.updateId();
                staff.add(t);
              }

            }
            Collections.sort(staff, pnc);
          }

        }
      }
    }

  }

  class ExportTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public ExportTeamAction() {
      super(ResourceManager.getText(Text.EXPORT_PERS));
    }

    public void actionPerformed(ActionEvent e) {
      if (selectionmodel.getMinSelectionIndex() < 0) {
        JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.SELECT_PERSON_EXPORT),
            ResourceManager.getText(Text.NO_PERS_SELECTED),
            JOptionPane.INFORMATION_MESSAGE);

      }
      else {
        ArrayList<Staff> staffToExport = new ArrayList<Staff>();
        int min = selectionmodel.getMinSelectionIndex();
        int max = selectionmodel.getMaxSelectionIndex();
        if (min >= 0) {
          for (int i = min; i <= max; i++) {
            if (selectionmodel.isSelectedIndex(i)) {
              staffToExport.add(staffTModel.getPersonAt(i));
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
            filetype.setAttribute("type", MetaInfos.FILE_STAFFFILETYPE);
            filetype.setAttribute("version", MetaInfos.getXMLFileVersion());

            TeamsXMLHandler.exportStaff(filetype, staffToExport);
            XMLHandler.saveXMLDoc(new Document(filetype), filename);
          }
        }
      }
    }

  }

  class DeleteTeamAction extends AbstractAction {

    private static final long serialVersionUID = 8990437935813416417L;

    public DeleteTeamAction() {
      super(ResourceManager.getText(Text.DELETE_PERSON), ResourceManager
          .getIcon(Icon.DELETE_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      StaffTabPanelPModel.this.deleteAction();
    }
  }

  public ListSelectionModel getSelectionModel() {
    return selectionmodel;
  }
}
