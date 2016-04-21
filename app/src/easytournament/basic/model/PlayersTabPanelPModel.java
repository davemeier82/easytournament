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
import easytournament.basic.model.dialog.PlayerDialogPModel;
import easytournament.basic.model.tablemodel.PlayerImportTableModel;
import easytournament.basic.model.tablemodel.PlayerTableModel;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.comperator.PersonNameComperator;
import easytournament.basic.valueholder.Player;
import easytournament.basic.valueholder.Team;
import easytournament.basic.xml.TeamsXMLHandler;
import easytournament.basic.xml.XMLHandler;

public class PlayersTabPanelPModel extends Model {

  private static final long serialVersionUID = 3331905007891003838L;
  public static final int NEW_PLAYER_ACTION = 0;
  public static final int IMPORT_PLAYER_ACTION = 1;
  public static final int EXPORT_PLAYER_ACTION = 2;
  public static final int EDIT_PLAYER_ACTION = 3;
  public static final int DELETE_PLAYER_ACTION = 4;
  public static final String PROPERTY_SELECTIONINLIST = "selectionInList";
  public static final String PROPERTY_SHOW_PLAYERDIALOG = "showPlayerDialog";
  private PlayerTableModel playerTModel;
  private ListSelectionModel selectionmodel = new DefaultListSelectionModel();
  private ArrayListModel<Player> players;
  private Team team;
  private PersonNameComperator pnc = new PersonNameComperator();

  public PlayersTabPanelPModel(Team team) {
    this.team = team;
    this.retrieveData();
  }

  private void retrieveData() {
    players = team.getPlayers();
    Collections.sort(players, pnc);
    playerTModel = new PlayerTableModel(players);
  }

  public void sortPlayers() {
    Collections.sort(players, pnc);
    playerTModel.fireTableDataChanged();
  }

  public PlayerDialogPModel getPlayerDModel(int row) {
    if (row >= 0)
      return new PlayerDialogPModel(players.get(row));
    return null;
  }

  public void deleteAction() {
    int min = selectionmodel.getMinSelectionIndex();
    int max = selectionmodel.getMaxSelectionIndex();
    if (min >= 0) {
      for (int i = min; i <= max; i++) {
        playerTModel.removeRow(min);
      }
    }
  }

  public TableModel getTableModel() {
    return playerTModel;
  }

  public Action getAction(int action) {
    switch (action) {
      case NEW_PLAYER_ACTION:
        return new NewTeamAction();
      case IMPORT_PLAYER_ACTION:
        return new ImportTeamAction();
      case EXPORT_PLAYER_ACTION:
        return new ExportTeamAction();
      case DELETE_PLAYER_ACTION:
        return new DeleteTeamAction();
      default:
        return null;
    }
  }

  class NewTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public NewTeamAction() {
      super(ResourceManager.getText(Text.ADD_PLAYER), ResourceManager
          .getIcon(Icon.ADDPERSON_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      PlayersTabPanelPModel.this.firePropertyChange(PROPERTY_SHOW_PLAYERDIALOG,
          null, new PlayerDialogPModel(team));
    }
  }

  class ImportTeamAction extends AbstractAction {

    public ImportTeamAction() {
      super(ResourceManager.getText(Text.IMPORT_PLAYERS));
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
              || !(type.equals(MetaInfos.FILE_PLAYERFILETYPE)
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
          ArrayList<Player> iplayer;
          if (type.equals(MetaInfos.FILE_MAINFILETYPE)
              || type.equals(MetaInfos.FILE_TEAMFILETYPE)) {
            iplayer = new ArrayList<Player>();
            Element teamEl = filetype.getChild("teams");
            List<Element> teamEls = teamEl.getChildren("team");
            for (Element el : teamEls)
              iplayer.addAll(TeamsXMLHandler.readPlayers(el, true));
          }
          else {
            iplayer = TeamsXMLHandler.readPlayers(filetype, true);
          }
          if (iplayer.size() < 0) {
            JOptionPane.showMessageDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager
                .getText(Text.NO_PLAYER_FOUND_MSG), ResourceManager
                .getText(Text.NO_PLAYER_FOUND), JOptionPane.ERROR_MESSAGE);
            return;
          }
          ArrayList<Boolean> selection = new ArrayList<Boolean>(iplayer.size());
          for (int i = 0; i < iplayer.size(); i++)
            selection.add(new Boolean(true));

          ImportListDialogPModel ilpm = new ImportListDialogPModel(
              new PlayerImportTableModel(iplayer, selection));

          new ImportListDialog(Organizer.getInstance().getMainFrame(),
              ResourceManager.getText(Text.SELECT_PLAYER_IMPORT), true, ilpm);

          if (ilpm.getAnswer()) {
            for (int i = 0; i < iplayer.size(); i++) {
              if (selection.get(i)) {
                Player t = iplayer.get(i);
                t.updateId();
                players.add(t);
              }

            }
            Collections.sort(players, pnc);
          }

        }
      }
    }
  }

  class ExportTeamAction extends AbstractAction {

    public ExportTeamAction() {
      super(ResourceManager.getText(Text.EXPORT_PLAYER));
    }

    public void actionPerformed(ActionEvent e) {
      if (selectionmodel.getMinSelectionIndex() < 0) {
        JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.SELECT_PLAYER_EXPORT),
            ResourceManager.getText(Text.NO_PLAYER_SELECTED),
            JOptionPane.INFORMATION_MESSAGE);

      }
      else {
        ArrayList<Player> playersToExport = new ArrayList<Player>();
        int min = selectionmodel.getMinSelectionIndex();
        int max = selectionmodel.getMaxSelectionIndex();
        if (min >= 0) {
          for (int i = min; i <= max; i++) {
            if (selectionmodel.isSelectedIndex(i)) {
              playersToExport.add((Player)playerTModel.getPersonAt(i));
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
            filetype.setAttribute("type", MetaInfos.FILE_PLAYERFILETYPE);
            filetype.setAttribute("version", MetaInfos.getXMLFileVersion());

            TeamsXMLHandler.exportPlayers(filetype, playersToExport);
            XMLHandler.saveXMLDoc(new Document(filetype), filename);
          }
        }
      }
    }

  }

  class DeleteTeamAction extends AbstractAction {

    public DeleteTeamAction() {
      super(ResourceManager.getText(Text.DELETE_PLAYER), ResourceManager
          .getIcon(Icon.DELETE_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      PlayersTabPanelPModel.this.deleteAction();
    }
  }

  public ListSelectionModel getSelectionModel() {
    return selectionmodel;
  }

}
