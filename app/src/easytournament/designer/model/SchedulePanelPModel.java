package easytournament.designer.model;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Refree;
import easytournament.basic.valueholder.Team;
import easytournament.basic.valueholder.Tournament;
import easytournament.designer.export.ScheduleExportable;
import easytournament.designer.model.tablemodel.ScheduleTableModel;
import easytournament.designer.settings.ScheduleSettings;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.designer.valueholder.Position;
import easytournament.designer.valueholder.ScheduleEntry;

public class SchedulePanelPModel extends Model implements TableModelListener,
    PropertyChangeListener {

  private static final long serialVersionUID = -7596821302494623927L;
  public static final String PROPERTY_SHOW_TEAMS = "showTeams";
  public static final String PROPERTY_STOP_CELL_EDITING = "stopCellEditing";
  public static final String PROPERTY_FILTERLABELS = "filterLabels";
  public static final String PROPERTY_FILTER = "filter";

  public static final int NEW_ACTION = 0;
  public static final int CHANGE_TEAMVIEW_ACTION = 3;

  protected ScheduleTableModel tableModel;
  protected ListSelectionModel selectionmodel = new DefaultListSelectionModel();
  private Tournament tournament = Organizer.getInstance()
      .getCurrentTournament();
  protected boolean showTeams = false;
  protected boolean dataChanged = false;
  protected String filter = ResourceManager.getText(Text.NOFILTER);

  public SchedulePanelPModel() {
    this.tableModel = new ScheduleTableModel(getTournament().getSchedules());
    this.tableModel.addTableModelListener(this);
    ScheduleSettings.getInstance().addPropertyChangeListener(
        ScheduleSettings.PROPERTY_SHOWREFREES, this);
  }

  public ScheduleTableModel getTableModel() {
    return this.tableModel;
  }

  public Action getAction(int a) {
    switch (a) {
      case NEW_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.ADD_GAME),
            ResourceManager.getIcon(Icon.ADD_ICON_SMALL)) {

          private static final long serialVersionUID = -4189416213188250883L;

          @Override
          public void actionPerformed(ActionEvent arg0) {
            ArrayListModel<AbstractGroup> groups = getTournament().getPlan()
                .getGroups();
            if (groups.isEmpty()) {
              return;
            }
            ArrayList<Position> pos = groups.get(0).getPositions();
            if (pos.size() > 1) {
              ScheduleEntry se = new ScheduleEntry(pos.get(0), pos.get(1));
              if (getFilter() != null && getFilter() != "") {
                boolean found = false;
                for (AbstractGroup g : groups) {
                  if (g.getName().equals(SchedulePanelPModel.this.filter)) {
                    pos = g.getPositions();
                    se.setHomePos(pos.get(0));
                    se.setAwayPos(pos.get(1));
                    found = true;
                    break;
                  }
                }
                if (!found) {
                  for (Team t : getTournament().getTeams()) {
                    if (t.getName().equals(SchedulePanelPModel.this.filter)) {
                      Position position = t.getPositionAssignedTo();
                      if (position == null) {
                        found = false;
                        break;
                      }
                      pos = position.getGroup().getPositions();
                      se.setHomePos(position);
                      for (Position p : pos) {
                        if (!p.equals(position)) {
                          se.setAwayPos(p);
                          break;
                        }
                      }

                      found = true;
                      break;
                    }
                  }
                }
                if (!found) {
                  for (Refree r : getTournament().getRefrees()) {
                    if (r.getName().equals(SchedulePanelPModel.this.filter)) {
                      se.getReferees().add(r);
                      break;
                    }
                  }
                }
              }
              getTableModel().addShedule(se);
              getTableModel().fireTableDataChanged();
            }
          }
        };
      case CHANGE_TEAMVIEW_ACTION:
        AbstractAction act = new AbstractAction("",
            ResourceManager.getIcon(Icon.CHANGE_VIEW_ICON_SMALL)) {

          private static final long serialVersionUID = -6694796994254952112L;

          @Override
          public void actionPerformed(ActionEvent e) {
            SchedulePanelPModel.this.setShowTeams(!SchedulePanelPModel.this
                .isShowTeams());
          }
        };
        act.putValue(Action.SHORT_DESCRIPTION,
            ResourceManager.getText(Text.SWITCH_TEAM_DESIGN_VIEW));
        return act;
      default:
        break;
    }
    return null;
  }

  public ListSelectionModel getSelectionModel() {
    return this.selectionmodel;
  }

  public void deleteGames(List<Integer> gameIndices) {
    ArrayListModel<ScheduleEntry> schedules = this.tournament.getSchedules();
    Collections.sort(gameIndices, Collections.reverseOrder());
    for (Integer i : gameIndices) {
      ScheduleEntry se = schedules.get(i);
      this.getTournament().getSchedules().remove(se);
      se.getGroupAssignedTo().getSchedules().remove(se);
    }
    this.tableModel.fireTableDataChanged();
  }

  public boolean isDataChanged() {
    return this.dataChanged;
  }

  public void setDataChanged(boolean b) {
    this.dataChanged = b;
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    this.dataChanged = true;
  }

  public ArrayList<Refree> getRefrees() {
    return this.getTournament().getRefrees();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(ScheduleSettings.PROPERTY_SHOWREFREES)) {
      this.tableModel.fireTableStructureChanged();
    }

  }

  public boolean isShowTeams() {
    return this.showTeams;
  }

  public void setShowTeams(boolean showTeams) {
    boolean old = this.showTeams;
    this.showTeams = showTeams;
    this.firePropertyChange(PROPERTY_SHOW_TEAMS, old, this.showTeams);
    this.tableModel.fireTableDataChanged();
  }

  public void stopCellEditing() {
    this.firePropertyChange(PROPERTY_STOP_CELL_EDITING, false, true);
  }

  public String getFilter() {
    return this.filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
    this.tableModel.fireTableDataChanged();
  }

  public List<String> getFilterLabels() {
    ArrayList<String> filters = new ArrayList<String>();
    ArrayListModel<AbstractGroup> groups = this.getTournament().getPlan()
        .getOrderedGroups();
    filters.add(ResourceManager.getText(Text.NOFILTER));
    for (AbstractGroup g : groups) {
      filters.add(g.getName());
    }

    for (Team team : this.getTournament().getTeams()) {
      filters.add(team.getName());
    }
    for (Refree ref : this.getTournament().getRefrees()) {
      filters.add(ref.getName());
    }
    return filters;
  }

  public void exportSchedule(List<Integer> indices) {
    ScheduleExportable e = new ScheduleExportable(null, null);
    ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
    for (Integer i : indices) {
      schedule.add(this.getTournament().getSchedules().get(i));
    }
    e.export(schedule, isShowTeams(), this.filter.equals(ResourceManager
        .getText(Text.NOFILTER))? "" : this.filter);
  }

  public Tournament getTournament() {
    return this.tournament;
  }

  public void setTournament(Tournament t) {
    this.tournament = t;
  }

  public void delayGames(ArrayList<Integer> indices, Calendar calendar) {
    if (indices.isEmpty()) {
      return;
    }
    Calendar oldCal = getDate(indices.get(0));
    int differenceInMin = (int)((calendar.getTimeInMillis() - oldCal
        .getTimeInMillis()) / (1000 * 60));
    List<ScheduleEntry> schedule = this.tournament.getSchedules();
    for (Integer i : indices) {
      schedule.get(i).getDate().add(Calendar.MINUTE, differenceInMin);
    }
    this.tableModel.sortData();
  }

  public Calendar getDate(Integer index) {
    return this.tournament.getSchedules().get(index).getDate();
  }
}
