package easytournament.basic.model;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import org.jdom.Document;
import org.jdom.Element;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.MetaInfos;
import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ImportListDialog;
import easytournament.basic.gui.dialog.RefreeDialog;
import easytournament.basic.model.dialog.ImportListDialogPModel;
import easytournament.basic.model.dialog.RefreeDialogPModel;
import easytournament.basic.model.tablemodel.RefreeImportTableModel;
import easytournament.basic.model.tablemodel.RefreeTableModel;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.comperator.PersonNameComperator;
import easytournament.basic.valueholder.Refree;
import easytournament.basic.valueholder.Team;
import easytournament.basic.valueholder.Tournament;
import easytournament.basic.xml.RefreeXMLHandler;
import easytournament.basic.xml.XMLHandler;
import easytournament.designer.valueholder.ScheduleEntry;

public class RefreeOverviewPModel extends Model implements ListDataListener {

  private static final long serialVersionUID = 3331905007891003838L;
  public static final int NEW_REFREE_ACTION = 0;
  public static final int IMPORT_REFREE_ACTION = 1;
  public static final int EXPORT_REFREE_ACTION = 2;
  public static final int EDIT_REFREE_ACTION = 3;
  public static final int DELETE_REFREE_ACTION = 4;
  public static final String PROPERTY_SELECTIONINLIST = "selectionInList";
  private RefreeTableModel personTModel;
  private ListSelectionModel selectionmodel = new DefaultListSelectionModel();
  private ArrayListModel<Refree> refrees;
  private PersonNameComperator pnc = new PersonNameComperator();

  public RefreeOverviewPModel() {
    this.retrieveData();
  }

  private void retrieveData() {

    Tournament t = Organizer.getInstance().getCurrentTournament();
    refrees = t.getRefrees();
    personTModel = new RefreeTableModel(refrees, false);
    t.getRefrees().addListDataListener(this);
  }

  public void editRefree(int row) {
    final RefreeDialog rDialog = new RefreeDialog(Organizer.getInstance()
        .getMainFrame(), new RefreeDialogPModel(refrees.get(row), false), true,
        false);
    rDialog.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        refrees.removeListDataListener(RefreeOverviewPModel.this);
        Collections.sort(refrees, pnc);
        refrees.addListDataListener(RefreeOverviewPModel.this);
        personTModel.fireTableDataChanged();
        rDialog.removeWindowListener(this);
        super.windowClosing(e);
      }
    });
  }

  public void deleteAction() {
    int min = selectionmodel.getMinSelectionIndex();
    int max = selectionmodel.getMaxSelectionIndex();
    ArrayList<Refree> refs = new ArrayList<Refree>();
    if (min >= 0) {
      for (int i = min; i <= max; i++) {
        refs.add(personTModel.removeRow(min));
      }
    }
    for (ScheduleEntry se : Organizer.getInstance().getCurrentTournament()
        .getSchedules()) {
      se.getReferees().removeAll(refs);
    }
  }

  public TableModel getTableModel() {
    return personTModel;
  }

  public Action getAction(int action) {
    switch (action) {
      case NEW_REFREE_ACTION:
        return new NewTeamAction();
      case IMPORT_REFREE_ACTION:
        return new ImportTeamAction();
      case EXPORT_REFREE_ACTION:
        return new ExportTeamAction();
      case DELETE_REFREE_ACTION:
        return new DeleteTeamAction();
      default:
        return null;
    }
  }

  class NewTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public NewTeamAction() {
      super(ResourceManager.getText(Text.NEW_REFEREE), ResourceManager
          .getIcon(Icon.ADDPERSON_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      final RefreeDialog rDialog = new RefreeDialog(Organizer.getInstance()
          .getMainFrame(), new RefreeDialogPModel(), true, false);
      rDialog.addWindowListener(new WindowAdapter() {

        @Override
        public void windowClosed(WindowEvent e) {
          refrees.removeListDataListener(RefreeOverviewPModel.this);
          Collections.sort(refrees, pnc);
          refrees.addListDataListener(RefreeOverviewPModel.this);
          personTModel.fireTableDataChanged();
          rDialog.removeWindowListener(this);
          super.windowClosing(e);
        }
      });
    }
  }

  class ImportTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public ImportTeamAction() {
      super(ResourceManager.getText(Text.IMPORT_REFEREE));
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
              || !(type.equals(MetaInfos.FILE_REFREEFILETYPE) || type
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
          ArrayList<Refree> irefree = RefreeXMLHandler.importRefrees(filetype);

          if (irefree.size() < 0) {
            JOptionPane.showMessageDialog(Organizer.getInstance()
                .getMainFrame(),
                ResourceManager.getText(Text.NO_REF_FOUND_MSG), ResourceManager
                    .getText(Text.NO_REF_FOUND), JOptionPane.ERROR_MESSAGE);
            return;
          }
          ArrayList<Boolean> selection = new ArrayList<Boolean>(irefree.size());
          for (int i = 0; i < irefree.size(); i++)
            selection.add(new Boolean(true));

          ImportListDialogPModel ilpm = new ImportListDialogPModel(
              new RefreeImportTableModel(irefree, selection));

          new ImportListDialog(Organizer.getInstance().getMainFrame(),
              ResourceManager.getText(Text.SELECT_REF_IMPORT), true, ilpm);

          if (ilpm.getAnswer()) {
            for (int i = 0; i < irefree.size(); i++) {
              if (selection.get(i)) {
                Refree t = irefree.get(i);
                t.updateId();
                refrees.add(t);
              }

            }
            refrees.removeListDataListener(RefreeOverviewPModel.this);
            Collections.sort(refrees, pnc);
            refrees.addListDataListener(RefreeOverviewPModel.this);
          }

        }
      }
    }

  }

  class ExportTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public ExportTeamAction() {
      super(ResourceManager.getText(Text.EXPORT_REF));
    }

    public void actionPerformed(ActionEvent e) {
      if (selectionmodel.getMinSelectionIndex() < 0) {
        JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.SELECT_REF_EXPORT),
            ResourceManager.getText(Text.NO_REF_SELECTED),
            JOptionPane.INFORMATION_MESSAGE);

      }
      else {
        ArrayList<Refree> staffToExport = new ArrayList<Refree>();
        int min = selectionmodel.getMinSelectionIndex();
        int max = selectionmodel.getMaxSelectionIndex();
        if (min >= 0) {
          for (int i = min; i <= max; i++) {
            if (selectionmodel.isSelectedIndex(i)) {
              staffToExport.add(personTModel.getPersonAt(i));
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
            filetype.setAttribute("type", MetaInfos.FILE_REFREEFILETYPE);
            filetype.setAttribute("version", MetaInfos.getXMLFileVersion());

            RefreeXMLHandler.exportRefree(filetype, staffToExport);
            XMLHandler.saveXMLDoc(new Document(filetype), filename);
          }
        }
      }
    }

  }

  class DeleteTeamAction extends AbstractAction {

    private static final long serialVersionUID = 8990437935813416417L;

    public DeleteTeamAction() {
      super(ResourceManager.getText(Text.DELETE_REF), ResourceManager
          .getIcon(Icon.DELETE_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      RefreeOverviewPModel.this.deleteAction();
    }
  }

  public ListSelectionModel getSelectionModel() {
    return selectionmodel;
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    refrees.removeListDataListener(this);
    Collections.sort(refrees, pnc);
    personTModel.fireTableDataChanged();
    refrees.addListDataListener(this);
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    personTModel.fireTableDataChanged();
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    personTModel.fireTableDataChanged();
  }

}
