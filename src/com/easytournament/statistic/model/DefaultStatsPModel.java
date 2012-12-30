package com.easytournament.statistic.model;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.statistic.csv.StatisticCSVHandler;
import com.easytournament.statistic.html.StatisticHTMLHandler;
import com.easytournament.statistic.model.tablemodel.StatisticTableModel;
import com.easytournament.statistic.statistics.GoalsAndAssistsStatistic;
import com.easytournament.statistic.statistics.GoalsAndAssistsStatisticInclPShootout;
import com.easytournament.statistic.statistics.GoalsDetailStatistic;
import com.easytournament.statistic.statistics.GoalsDetailStatisticInclPShootout;
import com.easytournament.statistic.statistics.GoalsStatistic;
import com.easytournament.statistic.statistics.GoalsStatisticInclPShootout;
import com.easytournament.statistic.statistics.OwngoalStatistic;
import com.easytournament.statistic.statistics.PenaltiesDetailStatistic;
import com.easytournament.statistic.statistics.PenaltiesStatistic;
import com.easytournament.statistic.statistics.Statistic;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.common.collect.ArrayListModel;

public class DefaultStatsPModel extends Model implements ListDataListener {

  public static final int TEAMS = 0;
  public static final int GROUPBY = 1;
  public static final int STATS = 2;
  public static final String PROPERTY_TEAM_SELECTION = "team";
  public static final String PROPERTY_GROUPBY_SELECTION = "groupBy";
  public static final String PROPERTY_STAT_SELECTION = "stat";
  public static final String TEAM_SELECTION_ENABLED = "teamSelEnabled";
  public static final int EXPORT_ACTION = 0;

  protected String team;
  protected String groupBy;
  protected String stat;

  protected ArrayListModel<String> teams = new ArrayListModel<String>();
  protected ArrayListModel<String> groupbys = new ArrayListModel<String>();
  protected ArrayListModel<String> stats = new ArrayListModel<String>();
  protected ArrayListModel<Statistic> statistics = new ArrayListModel<Statistic>();

  protected SelectionInList<ArrayListModel<String>> teamSelection;
  protected SelectionInList<ArrayListModel<String>> groupBySelection;
  protected SelectionInList<ArrayListModel<String>> statsSelection;

  protected ArrayListModel<Team> allteams;

  protected StatisticTableModel tableModel = new StatisticTableModel();

  protected Tournament t = Organizer.getInstance().getCurrentTournament();

  public DefaultStatsPModel() {
    init();
  }

  protected void init() {

    allteams = t.getTeams();
    allteams.addListDataListener(this);
    t.getSchedules().addListDataListener(this);

    Statistic s = new GoalsStatistic();
    statistics.add(s);
    stats.add(s.getName());
    s = new GoalsStatisticInclPShootout();
    statistics.add(s);
    stats.add(s.getName());
    s = new GoalsAndAssistsStatistic();
    statistics.add(s);
    stats.add(s.getName());
    s = new GoalsAndAssistsStatisticInclPShootout();
    statistics.add(s);
    stats.add(s.getName());
    s = new GoalsDetailStatistic();
    statistics.add(s);
    stats.add(s.getName());
    s = new GoalsDetailStatisticInclPShootout();
    statistics.add(s);
    stats.add(s.getName());
    s = new OwngoalStatistic();
    statistics.add(s);
    stats.add(s.getName());
    s = new PenaltiesStatistic();
    statistics.add(s);
    stats.add(s.getName());
    s = new PenaltiesDetailStatistic();
    statistics.add(s);
    stats.add(s.getName());

    groupbys.add(ResourceManager.getText(Text.TEAMS));
    groupbys.add(ResourceManager.getText(Text.PLAYERS));

    teamSelection = new SelectionInList<ArrayListModel<String>>(teams,
        new PropertyAdapter<DefaultStatsPModel>(this, PROPERTY_TEAM_SELECTION,
            true));
    groupBySelection = new SelectionInList<ArrayListModel<String>>(groupbys,
        new PropertyAdapter<DefaultStatsPModel>(this,
            PROPERTY_GROUPBY_SELECTION, true));
    statsSelection = new SelectionInList<ArrayListModel<String>>(stats,
        new PropertyAdapter<DefaultStatsPModel>(this, PROPERTY_STAT_SELECTION,
            true));

    updateTeams();
    setStat(stats.get(0));
    setGroupBy(groupbys.get(0));
  }

  public void updateStatistic() {
    int selection = statsSelection.getSelectionIndex();
    if (selection >= 0 && selection < statistics.getSize()) {
      Statistic s = statistics.get(selection);
      boolean hasTeam = s.existsForTeam();
      boolean hasPlayer = s.existsForPlayers();
      String oldGroupBy = this.groupBy;
      if (!hasTeam) {
        groupBy = groupbys.get(1);
        this.firePropertyChange(TEAM_SELECTION_ENABLED, true, false);
      }
      else if (!hasPlayer) {
        groupBy = groupbys.get(0);
        this.firePropertyChange(TEAM_SELECTION_ENABLED, true, false);
      }
      else {
        this.firePropertyChange(TEAM_SELECTION_ENABLED, false, true);
      }
      this.firePropertyChange(PROPERTY_GROUPBY_SELECTION, oldGroupBy,
          this.groupBy);

      tableModel.setRowCount(0);
      Team tm = null;
      if (teamSelection.getSelectionIndex() > 0) {
        tm = allteams.get(teamSelection.getSelectionIndex() - 1);
      }
      s.addStatistic(tableModel, groupBySelection.getSelectionIndex() == 0, tm);
      tableModel.fireTableStructureChanged();
    }
  }

  private void updateTeams() {
    teams.clear();
    teams.add(ResourceManager.getText(Text.ALL_TEAMS));
    for (Team t : allteams) {
      teams.add(t.getName());
    }
    setTeam(teams.get(0));
  }

  public SelectionInList<ArrayListModel<String>> getSelectionInList(int list) {
    switch (list) {
      case TEAMS:
        return teamSelection;
      case GROUPBY:
        return groupBySelection;
      case STATS:
        return statsSelection;
    }
    return null;
  }

  public StatisticTableModel getTableModel() {
    return tableModel;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    String old = this.team;
    this.team = team;
    updateStatistic();
    this.firePropertyChange(PROPERTY_TEAM_SELECTION, old, this.team);
  }

  public String getGroupBy() {
    return groupBy;
  }

  public void setGroupBy(String groupBy) {
    String old = this.groupBy;
    this.groupBy = groupBy;
    updateStatistic();
    this.firePropertyChange(PROPERTY_GROUPBY_SELECTION, old, this.groupBy);
  }

  public String getStat() {
    return stat;
  }

  public void setStat(String stat) {
    String old = this.stat;
    this.stat = stat;
    updateStatistic();
    this.firePropertyChange(PROPERTY_STAT_SELECTION, old, this.stat);
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    if (e.getSource() == allteams) {
      updateTeams();
    }
    updateStatistic();
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    if (e.getSource() == allteams) {
      updateTeams();
    }
    updateStatistic();
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    if (e.getSource() == allteams) {
      updateTeams();
    }
    updateStatistic();
  }

  public Action getAction(int a) {
    switch (a) {
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
                    StatisticHTMLHandler.saveDefaultStat(filename, tableModel,
                        getStatPrintTitle());
                  }
                  else {
                    if (!filename.getPath().toLowerCase().endsWith(".csv")) {
                      filename = new File(filename.getPath() + ".csv");
                    }

                    StatisticCSVHandler.saveDefaultStat(filename, tableModel);

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

  public String getStatPrintTitle() {
    return statistics.get(statsSelection.getSelectionIndex()).getPrintName();
  }

}
