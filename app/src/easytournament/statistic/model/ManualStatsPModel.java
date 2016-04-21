package easytournament.statistic.model;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.GameEvent;
import easytournament.basic.valueholder.Team;
import easytournament.basic.valueholder.Tournament;
import easytournament.statistic.csv.StatisticCSVHandler;
import easytournament.statistic.html.StatisticHTMLHandler;
import easytournament.statistic.model.tablemodel.EventSelectionTableModel;
import easytournament.statistic.model.tablemodel.StatisticTableModel;
import easytournament.statistic.statistics.ManualStatistic;

public class ManualStatsPModel extends Model implements ListDataListener,
    PropertyChangeListener {

  public static final int TEAMS = 0;
  public static final int GROUPBY = 1;
  public static final String PROPERTY_TEAM_SELECTION = "team";
  public static final String PROPERTY_GROUPBY_SELECTION = "groupBy";
  public static final String PROPERTY_BEGIN_TIME = "beginTime";
  public static final String PROPERTY_END_TIME = "endTime";
  public static final String PROPERTY_TO_TIME_MI = "totimeMin";
  public static final String PROPERTY_TO_TIME_SEC = "totimeSec";
  public static final String PROPERTY_FROM_TIME_MI = "fromtimeMin";
  public static final String PROPERTY_FROM_TIME_SEC = "fromtimeSec";
  public static final String PROPERTY_PLAYTIME_SELECTED = "playtimeSelected";
  public static final String PROPERTY_FROMDATE_SELECTED = "fromDateSelected";
  public static final String PROPERTY_TODATE_SELECTED = "toDateSelected";
  public static final String PROPERTY_TABLE_TITLE = "tableTitle";
  public static final String PROPERTY_TOTAL_SELECTED = "totalSelected";
  public static final String PROPERTY_BEGIN_DATE = "beginDate";
  public static final String PROPERTY_END_DATE = "endDate";
  
  public static final int EVENT_SEL_TABLEMODEL = 0;
  public static final int STATISTIC_TABLEMODEL = 1;

  public static final int ADD_ACTION = 0;
  public static final int REMOVE_ACTION = 1;
  public static final int EXPORT_ACTION = 2;
  public static final int CALC_ACTION = 3;

  protected ArrayListModel<Team> allteams;

  protected String team;
  protected String groupBy;
  protected int totimeMin;
  protected int totimeSec;
  protected int fromtimeMin;
  protected int fromtimeSec;
  protected boolean playtimeSelected = false;
  protected boolean fromDateSelected = false;
  protected boolean toDateSelected = false;
  protected boolean totalSelected = true;
  protected String tableTitle = "";

  protected ArrayListModel<String> teams = new ArrayListModel<String>();
  protected ArrayListModel<String> groupbys = new ArrayListModel<String>();
  protected SelectionInList<ArrayListModel<String>> teamSelection;
  protected SelectionInList<ArrayListModel<String>> groupBySelection;
  protected EventSelectionTableModel eventSTableModel = new EventSelectionTableModel();
  protected ListSelectionModel eventSelModel = new DefaultListSelectionModel();
  protected StatisticTableModel statTableModel = new StatisticTableModel();

  protected DateFormat dateFormatter = DateFormat.getDateInstance(
      DateFormat.SHORT, ResourceManager.getLocale());
  protected DateFormat timeFormatter = DateFormat.getTimeInstance(
      DateFormat.SHORT, ResourceManager.getLocale());
  protected Calendar begin = new GregorianCalendar(ResourceManager.getLocale());
  protected Calendar end = new GregorianCalendar(ResourceManager.getLocale());
  protected Calendar tmpCal = new GregorianCalendar(ResourceManager.getLocale());

  protected Tournament t = Organizer.getInstance().getCurrentTournament();
  protected ArrayListModel<GameEvent> gameEvents = t.getGameEvents();

  protected ManualStatistic statistic = new ManualStatistic();

  public ManualStatsPModel() {
    init();
  }

  protected void init() {
    t.addPropertyChangeListener(this);

    allteams = t.getTeams();
    allteams.addListDataListener(this);
    t.getSchedules().addListDataListener(this);

    groupbys.add(ResourceManager.getText(Text.TEAMS));
    groupbys.add(ResourceManager.getText(Text.PLAYERS));

    teamSelection = new SelectionInList<ArrayListModel<String>>(teams,
        new PropertyAdapter<ManualStatsPModel>(this, PROPERTY_TEAM_SELECTION,
            true));
    groupBySelection = new SelectionInList<ArrayListModel<String>>(groupbys,
        new PropertyAdapter<ManualStatsPModel>(this,
            PROPERTY_GROUPBY_SELECTION, true));

    updateTeams();
    setGroupBy(groupbys.get(0));

    this.begin = (Calendar)t.getBegin().clone();
    this.end = (Calendar)t.getEnd().clone();
  }

  public SelectionInList<ArrayListModel<String>> getSelectionInList(int list) {
    switch (list) {
      case TEAMS:
        return teamSelection;
      case GROUPBY:
        return groupBySelection;
    }
    return null;
  }

  private void updateTeams() {
    teams.clear();
    teams.add(ResourceManager.getText(Text.ALL_TEAMS));
    for (Team t : allteams) {
      teams.add(t.getName());
    }
    setTeam(teams.get(0));
  }

  public Action getAction(int a) {
    switch (a) {
      case ADD_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.ADD),
            ResourceManager.getIcon(Icon.ADD_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            if (gameEvents.size() > 0)
              ManualStatsPModel.this.eventSTableModel.addRow(new Object[] {
                  gameEvents.get(0), false, true, false, ""});
            ManualStatsPModel.this.eventSTableModel.fireTableDataChanged();

          }
        };
      case REMOVE_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.DELETE),
            ResourceManager.getIcon(Icon.DELETE_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            ManualStatsPModel.this.deleteAction();
          }
        };
      case CALC_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CALCULATE)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            statTableModel.setRowCount(0);
            Team tm = null;
            if (teamSelection.getSelectionIndex() > 0) {
              tm = allteams.get(teamSelection.getSelectionIndex() - 1);
            }
            statistic.addStatistic(statTableModel, groupBySelection
                .getSelectionIndex() == 0, tm, eventSTableModel,
                playtimeSelected? fromtimeMin : null, fromtimeSec, totimeMin,
                totimeSec, fromDateSelected? begin : null, toDateSelected? end
                    : null, totalSelected);
            statTableModel.fireTableStructureChanged();
          }
        };
      case EXPORT_ACTION:
        return new AbstractAction(
            ResourceManager.getText(Text.EXPORT_STATISTIC)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter() {
              public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".csv")
                    || f.isDirectory();
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
                return f.getName().toLowerCase().endsWith(".html")
                    || f.isDirectory();
              }
            };
            chooser.addChoosableFileFilter(htmlfilter);
            int answer = chooser.showSaveDialog(Organizer.getInstance()
                .getMainFrame());
            if (answer == JFileChooser.APPROVE_OPTION) {
              File filename = chooser.getSelectedFile();

              if (filename != null) {
                try {
                  if (chooser.getFileFilter().equals(htmlfilter)) {
                    if (!filename.getPath().toLowerCase().endsWith(".html")) {
                      filename = new File(filename.getPath() + ".html");
                    }
                    StatisticHTMLHandler.saveDefaultStat(filename, statTableModel,
                        tableTitle);
                  }
                  else {
                    if (!filename.getPath().toLowerCase().endsWith(".csv")) {
                      filename = new File(filename.getPath() + ".csv");
                    }

                    StatisticCSVHandler.saveDefaultStat(filename, statTableModel);

                  }
                }
                catch (FileNotFoundException e1) {
                  JOptionPane.showInternalMessageDialog(Organizer.getInstance()
                      .getMainFrame(), ResourceManager
                      .getText(Text.COULD_NOT_SAVE_STAT), ResourceManager
                      .getText(Text.ERROR), JOptionPane.ERROR_MESSAGE);
                }

              }
            }

          }

        };
    }
    return null;
  }

  public void deleteAction() {
    int min = eventSelModel.getMinSelectionIndex();
    int max = eventSelModel.getMaxSelectionIndex();
    if (eventSTableModel.getRowCount() > 0) {
      ArrayList<Integer> toremove = new ArrayList<Integer>();
      for (int i = min; i <= max; i++) {
        if (eventSelModel.isSelectedIndex(i)) {
          toremove.add(i);
        }
      }
      for (int i = toremove.size() - 1; i >= 0; i--) {
        eventSTableModel.removeRow(toremove.get(i));
      }
    }
  }

  public TableModel getTableModel(int model) {
    switch (model) {
      case EVENT_SEL_TABLEMODEL:
        return eventSTableModel;
      case STATISTIC_TABLEMODEL:
        return statTableModel;
    }
    return null;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    String old = this.team;
    this.team = team;
    this.firePropertyChange(PROPERTY_TEAM_SELECTION, old, this.team);
  }

  public String getGroupBy() {
    return groupBy;
  }

  public void setGroupBy(String groupBy) {
    String old = this.groupBy;
    this.groupBy = groupBy;
    this.firePropertyChange(PROPERTY_GROUPBY_SELECTION, old, this.groupBy);
  }

  public void setBeginTime(Date time) {
    tmpCal.setTime(time);
    begin.set(Calendar.HOUR, tmpCal.get(Calendar.HOUR));
    begin.set(Calendar.MINUTE, tmpCal.get(Calendar.MINUTE));
    begin.set(Calendar.SECOND, tmpCal.get(Calendar.SECOND));
    begin.set(Calendar.MILLISECOND, tmpCal.get(Calendar.MILLISECOND));
    begin.set(Calendar.AM_PM, tmpCal.get(Calendar.AM_PM));
    this.firePropertyChange(PROPERTY_BEGIN_TIME, null, time);
  }

  public Date getBeginTime() {
    return begin.getTime();
  }

  public void setEndTime(Date time) {
    tmpCal.setTime(time);
    end.set(Calendar.HOUR, tmpCal.get(Calendar.HOUR));
    end.set(Calendar.MINUTE, tmpCal.get(Calendar.MINUTE));
    end.set(Calendar.SECOND, tmpCal.get(Calendar.SECOND));
    end.set(Calendar.MILLISECOND, tmpCal.get(Calendar.MILLISECOND));
    end.set(Calendar.AM_PM, tmpCal.get(Calendar.AM_PM));
    this.firePropertyChange(PROPERTY_END_TIME, null, time);
  }

  public Date getEndTime() {
    return end.getTime();
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    if (e.getSource() == allteams) {
      updateTeams();
    }
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    if (e.getSource() == allteams) {
      updateTeams();
    }
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    if (e.getSource() == allteams) {
      updateTeams();
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() == this.t) {
      if (evt.getPropertyName() == Tournament.PROPERTY_BEGIN) {
        this.begin = (Calendar)t.getBegin().clone();
        firePropertyChange(PROPERTY_BEGIN_TIME, null, begin.getTime());
        firePropertyChange(PROPERTY_BEGIN_DATE, null, begin.getTime());
      }
      else if (evt.getPropertyName() == Tournament.PROPERTY_END) {
        this.end = (Calendar)t.getEnd().clone();
        firePropertyChange(PROPERTY_END_TIME, null, end.getTime());
        firePropertyChange(PROPERTY_END_DATE, null, end.getTime());
      }
    }
  }

  public ArrayList<GameEvent> getGameEvents() {
    return this.gameEvents;
  }

  public ListSelectionModel getSelectionModel(int model) {
    switch (model) {
      case EVENT_SEL_TABLEMODEL:
        return eventSelModel;
    }
    return null;
  }

  public int getTotimeMin() {
    return totimeMin;
  }

  public void setTotimeMin(int min) {
    int old = this.totimeMin;
    this.totimeMin = min;
    this.firePropertyChange(PROPERTY_TO_TIME_MI, old, this.totimeMin);
  }

  public int getTotimeSec() {
    return totimeSec;
  }

  public void setTotimeSec(int sec) {
    int old = this.totimeSec;
    this.totimeSec = sec;
    this.firePropertyChange(PROPERTY_TO_TIME_SEC, old, this.totimeSec);
  }

  public int getFromtimeMin() {
    return fromtimeMin;
  }

  public void setFromtimeMin(int min) {
    int old = this.fromtimeMin;
    this.fromtimeMin = min;
    this.firePropertyChange(PROPERTY_FROM_TIME_MI, old, this.fromtimeMin);
  }

  public int getFromtimeSec() {
    return fromtimeSec;
  }

  public void setFromtimeSec(int sec) {
    int old = this.fromtimeSec;
    this.fromtimeSec = sec;
    this.firePropertyChange(PROPERTY_FROM_TIME_SEC, old, this.fromtimeSec);
  }

  public boolean isPlaytimeSelected() {
    return playtimeSelected;
  }

  public void setPlaytimeSelected(boolean playtimeSelected) {
    boolean old = this.playtimeSelected;
    this.playtimeSelected = playtimeSelected;
    this.firePropertyChange(PROPERTY_PLAYTIME_SELECTED, old,
        this.playtimeSelected);
  }

  public boolean isFromDateSelected() {
    return fromDateSelected;
  }

  public void setFromDateSelected(boolean fromDateSelected) {
    this.fromDateSelected = fromDateSelected;
  }

  public boolean isToDateSelected() {
    return toDateSelected;
  }

  public void setToDateSelected(boolean toDateSelected) {
    this.toDateSelected = toDateSelected;
  }

  public String getTableTitle() {
    return tableTitle;
  }

  public void setTableTitle(String tableTitle) {
    this.tableTitle = tableTitle;
  }

  public boolean isTotalSelected() {
    return totalSelected;
  }

  public void setTotalSelected(boolean totalSelected) {
    this.totalSelected = totalSelected;
  }

  public void setBeginDate(Date date){
    Date old = begin.getTime();
    tmpCal.setTime(date);
    begin.set(tmpCal.get(Calendar.YEAR), tmpCal.get(Calendar.MONTH),
        tmpCal.get(Calendar.DAY_OF_MONTH));
    this.firePropertyChange(PROPERTY_BEGIN_DATE, old, date);
  }
  
  public Date getBeginDate() {
    return this.begin.getTime();
  }
  
  public void setEndDate(Date date){
    Date old = end.getTime();
    tmpCal.setTime(date);
    end.set(tmpCal.get(Calendar.YEAR), tmpCal.get(Calendar.MONTH),
        tmpCal.get(Calendar.DAY_OF_MONTH));
    this.firePropertyChange(PROPERTY_END_DATE, old, date);
  }
  
  public Date getEndDate() {
    return this.end.getTime();
  }
}
