package com.easytournament.basic.model;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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


import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ImportListDialog;
import com.easytournament.basic.gui.dialog.TeamDialog;
import com.easytournament.basic.model.dialog.ImportListDialogPModel;
import com.easytournament.basic.model.dialog.TeamDialogPModel;
import com.easytournament.basic.model.tablemodel.TeamImportTableModel;
import com.easytournament.basic.model.tablemodel.TeamTableModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.util.comperator.TeamNameComperator;
import com.easytournament.basic.valueholder.Player;
import com.easytournament.basic.valueholder.Staff;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.basic.xml.TeamsXMLHandler;
import com.easytournament.basic.xml.XMLHandler;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.tournament.calc.Calculator;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;


public class TeamOverviewPModel extends Model implements ListDataListener {

  private static final long serialVersionUID = 3331905007891003838L;
  public static final int NEW_TEAM_ACTION = 0;
  public static final int IMPORT_TEAM_ACTION = 1;
  public static final int EXPORT_TEAM_ACTION = 2;
  public static final int EDIT_TEAM_ACTION = 3;
  public static final int DELETE_TEAM_ACTION = 4;

  public static final String PROPERTY_SELECTIONINLIST = "selectionInList";

  private TeamTableModel teamTModel;
  private ListSelectionModel selectionmodel = new DefaultListSelectionModel();
  private ArrayListModel<Team> teams;
  private ArrayListModel<Team> unassignedteams;
  private TeamNameComperator tnc = new TeamNameComperator();
  private Set<Integer> randomPositions = new HashSet<Integer>();

  public TeamOverviewPModel() {
    this.retrieveData();
  }

  private void retrieveData() {

    Tournament t = Organizer.getInstance().getCurrentTournament();
    teams = t.getTeams();
    unassignedteams = t.getUnassignedteams();
    for (Team team : teams) {
      randomPositions.add(team.getRandomPosition());
    }
    teamTModel = new TeamTableModel(teams);
    teams.addListDataListener(this);
  }

  public void editTeam(int row) {
    final TeamDialog tDialog = new TeamDialog(Organizer.getInstance()
        .getMainFrame(), new TeamDialogPModel(teams.get(row)), true);
    tDialog.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        ListDataListener[] ldls = teams.getListDataListeners();
        for (ListDataListener ldl : ldls)
          teams.removeListDataListener(ldl);
        Collections.sort(teams, tnc);
        for (ListDataListener ldl : ldls)
          teams.addListDataListener(ldl);
        teamTModel.fireTableDataChanged();
        tDialog.removeWindowListener(this);
        super.windowClosing(e);
      }
    });

  }

  public void deleteSelectedTeams() {
    int min = selectionmodel.getMinSelectionIndex();
    int max = selectionmodel.getMaxSelectionIndex();

    if (min >= 0) {
      ArrayList<Integer> idxs = new ArrayList<Integer>();
      for (int i = max; i >= min; i--) {
        if (selectionmodel.isSelectedIndex(i)) {
          Team team = teamTModel.getTeamAt(i);
          Position p = team.getPositionAssignedTo();
          if(p != null)
            p.setTeam(null);
          randomPositions.remove(team.getRandomPosition());

          for (ScheduleEntry s : Organizer.getInstance().getCurrentTournament()
              .getSchedules()) {
            if (team.equals(s.getHomeTeam())) {
              s.getHomePos().setTeam(null);
              s.getGameEvents().clear();
              s.setGamePlayed(false);
            }
            else if (team.equals(s.getAwayTeam())) {
              s.getAwayPos().setTeam(null);
              s.getGameEvents().clear();
              s.setGamePlayed(false);
            }
          }
          idxs.add(i);
        }
      }
      ArrayListModel<AbstractGroup> groups = Organizer.getInstance()
          .getCurrentTournament().getPlan().getOrderedGroups();
      for (AbstractGroup g : groups) {
        Calculator.calcTableEntries(g, false);
      }
      for (int i = 0; i < idxs.size(); i++) {
        teamTModel.removeRow(idxs.get(i));
      }
    }
  }

  public TableModel getTableModel() {
    return teamTModel;
  }

  public ListSelectionModel getSelectionModel() {
    return selectionmodel;
  }

  public Action getAction(int action) {
    switch (action) {
      case NEW_TEAM_ACTION:
        return new NewTeamAction();
      case IMPORT_TEAM_ACTION:
        return new ImportTeamAction();
      case EXPORT_TEAM_ACTION:
        return new ExportTeamAction();
      case DELETE_TEAM_ACTION:
        return new DeleteTeamAction();
      default:
        return null;
    }
  }

  class NewTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public NewTeamAction() {
      super(ResourceManager.getText(Text.NEW_TEAM), ResourceManager
          .getIcon(Icon.ADD_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      final TeamDialog tDialog = new TeamDialog(Organizer.getInstance()
          .getMainFrame(), new TeamDialogPModel(randomPositions), true);
      tDialog.addWindowListener(new WindowAdapter() {

        @Override
        public void windowClosed(WindowEvent e) {
          teams.removeListDataListener(TeamOverviewPModel.this);
          Collections.sort(teams, tnc);
          teams.addListDataListener(TeamOverviewPModel.this);
          teamTModel.fireTableDataChanged();
          tDialog.removeWindowListener(this);
          super.windowClosing(e);
        }
      });
    }

  }

  class ImportTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public ImportTeamAction() {
      super(ResourceManager.getText(Text.IMPORT_TEAM));
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
            showFileNotSupported();
            return;
          }

          String type = filetype.getAttributeValue("type");
          if (type == null
              || !(type.equals(MetaInfos.FILE_TEAMFILETYPE) || type
                  .equals(MetaInfos.FILE_MAINFILETYPE))) {
            showFileNotSupported();
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
          ArrayList<Team> iteams = TeamsXMLHandler.importTeams(filetype);
          if (iteams.size() < 0) {
            JOptionPane.showMessageDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager
                .getText(Text.NO_TEAM_FOUND_MSG), ResourceManager
                .getText(Text.NO_TEAM_FOUND), JOptionPane.ERROR_MESSAGE);
            return;
          }
          ArrayList<Boolean> selection = new ArrayList<Boolean>(iteams.size());
          for (int i = 0; i < iteams.size(); i++)
            selection.add(new Boolean(true));

          ImportListDialogPModel ilpm = new ImportListDialogPModel(
              new TeamImportTableModel(iteams, selection));

          new ImportListDialog(Organizer.getInstance().getMainFrame(),
              ResourceManager.getText(Text.SELECT_TEAM_IMPORT), true, ilpm);

          Random r = new Random();
          if (ilpm.getAnswer()) {
            for (int i = 0; i < iteams.size(); i++) {
              if (selection.get(i)) {

                int randPos = r.nextInt(Integer.MAX_VALUE);
                while (randomPositions.contains(randPos)) {
                  randPos++;
                }
                Team t = iteams.get(i);
                t.updateId();
                t.setRandomPosition(randPos);
                randomPositions.add(randPos);
                for (Player p : t.getPlayers())
                  p.updateId();
                for (Staff s : t.getStaff())
                  s.updateId();
                teams.add(t);
                unassignedteams.add(t);
              }

            }
            teams.removeListDataListener(TeamOverviewPModel.this);
            Collections.sort(teams, tnc);
            Collections.sort(unassignedteams, tnc);
            teams.addListDataListener(TeamOverviewPModel.this);
          }

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

  class ExportTeamAction extends AbstractAction {

    private static final long serialVersionUID = -5932715510680146919L;

    public ExportTeamAction() {
      super(ResourceManager.getText(Text.EXPORT_TEAM));
    }

    public void actionPerformed(ActionEvent e) {
      if (selectionmodel.getMinSelectionIndex() < 0) {
        JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.SELECT_TEAM_EXPORT),
            ResourceManager.getText(Text.NO_TEAM_SELECTED),
            JOptionPane.INFORMATION_MESSAGE);

      }
      else {
        ArrayList<Team> teamsToExport = new ArrayList<Team>();
        int min = selectionmodel.getMinSelectionIndex();
        int max = selectionmodel.getMaxSelectionIndex();
        if (min >= 0) {
          for (int i = min; i <= max; i++) {
            if (selectionmodel.isSelectedIndex(i)) {
              teamsToExport.add(teamTModel.getTeamAt(i));
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
            filetype.setAttribute("type", MetaInfos.FILE_TEAMFILETYPE);
            filetype.setAttribute("version", MetaInfos.getXMLFileVersion());

            TeamsXMLHandler.exportTeams(filetype, teamsToExport);
            XMLHandler.saveXMLDoc(new Document(filetype), filename);
          }
        }
      }
    }

  }

  class DeleteTeamAction extends AbstractAction {

    private static final long serialVersionUID = 8990437935813416417L;

    public DeleteTeamAction() {
      super(ResourceManager.getText(Text.DELETE_TEAM), ResourceManager
          .getIcon(Icon.DELETE_ICON_SMALL));
    }

    public void actionPerformed(ActionEvent e) {
      TeamOverviewPModel.this.deleteSelectedTeams();
    }
  }

  @Override
  public void contentsChanged(ListDataEvent arg0) {
    Collections.sort(teams, tnc);
    teamTModel.fireTableDataChanged();

  }

  @Override
  public void intervalAdded(ListDataEvent arg0) {
    teamTModel.fireTableDataChanged();
  }

  @Override
  public void intervalRemoved(ListDataEvent arg0) {
    teamTModel.fireTableDataChanged();
  }

}
