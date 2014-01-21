package com.easytournament.statistic.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gameevent.GameEventType;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEvent;
import com.easytournament.basic.valueholder.GameEventEntry;
import com.easytournament.basic.valueholder.Player;
import com.easytournament.basic.valueholder.SportSettings;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.statistic.model.tablemodel.StatisticTableModel;
import com.easytournament.statistic.util.IntegerArrayComparator;

public class GoalsDetailStatistic implements Statistic {

  protected Tournament t = Organizer.getInstance().getCurrentTournament();

  @Override
  public String getName() {
    return ResourceManager.getText(Text.GOALS_DETAIL);
  }

  @Override
  public String getPrintName() {
    return ResourceManager.getText(Text.TOPSCORER);
  }

  @Override
  public boolean existsForTeam() {
    return true;
  }

  @Override
  public boolean existsForPlayers() {
    return true;
  }

  @Override
  public void addStatistic(StatisticTableModel tableModel, boolean groupByTeam,
      Team team) {

    HashMap<Integer,Integer[]> entryMap = new HashMap<Integer,Integer[]>();
    HashMap<Integer,String> nameMap = new HashMap<Integer,String>();

    ArrayList<String> types = new ArrayList<String>();
    HashMap<Integer,Integer> typePos = new HashMap<Integer,Integer>();
    int i = 1;
    for (GameEvent e : t.getGameEvents()) {
      if (e.getType() == GameEventType.GOAL) {
        types.add(e.getName());
        typePos.put(e.getId(), i++);
      }
    }

    if (groupByTeam) {
      ArrayList<Integer> strCols = tableModel.getStringColumns();
      strCols.clear();
      strCols.add(0);
      String[] columns = new String[types.size() + 2];
      columns[0] = ResourceManager.getText(Text.TEAM);
      i = 1;
      for (String tname : types) {
        columns[i++] = tname;
      }
      columns[types.size() + 1] = ResourceManager.getText(Text.TOTAL);
      tableModel.setColumnIdentifiers(columns);
      if (t.getTeams().size() < 1)
        return;
      for (Team tm : t.getTeams()) {

        Integer[] ints = new Integer[types.size() + 2];
        ints[0] = tm.getId();
        for (i = 1; i < types.size() + 2; i++) {
          ints[i] = new Integer(0);
        }
        entryMap.put(tm.getId(), ints);
        nameMap.put(tm.getId(), tm.getName());
      }

      for (ScheduleEntry se : t.getSchedules()) {
        if (se.isGamePlayed()) {

          int totalmin;
          if (se.getGroupAssignedTo().isDefaultRules()) {
            SportSettings set = t.getSettings();
            totalmin = set.getNumGameTimes() * set.getMinPerGameTime()
                + set.getNumOvertimes() * set.getMinPerOvertime();
          }
          else {
            SportSettings set = se.getGroupAssignedTo().getSettings();
            totalmin = set.getNumGameTimes() * set.getMinPerGameTime()
                + set.getNumOvertimes() * set.getMinPerOvertime();
          }

          for (GameEventEntry ge : se.getGameEvents()) {
            if (ge.getEvent().getType() == GameEventType.GOAL
                && (totalmin > ge.getTimeMin() || (totalmin == ge.getTimeMin() && ge
                    .getTimeSec() == 0))) {
              if (ge.getTeam().equals(se.getHomeTeam())) {
                if (se.getHomeTeam() != null)
                  entryMap.get(se.getHomeTeam().getId())[typePos.get(ge
                      .getEvent().getId())] += ge.getEvent().getPointsForTeam();
                if (se.getAwayTeam() != null)
                  entryMap.get(se.getAwayTeam().getId())[typePos.get(ge
                      .getEvent().getId())] += ge.getEvent()
                      .getPointsForOpponent();
              }
              else {
                if (se.getAwayTeam() != null)
                  entryMap.get(se.getAwayTeam().getId())[typePos.get(ge
                      .getEvent().getId())] += ge.getEvent().getPointsForTeam();
                if (se.getHomeTeam() != null)
                  entryMap.get(se.getHomeTeam().getId())[typePos.get(ge
                      .getEvent().getId())] += ge.getEvent()
                      .getPointsForOpponent();
              }
            }
          }
        }
      }

      ArrayList<Integer[]> vals = new ArrayList<Integer[]>();
      Integer[] arr;
      for (Map.Entry<Integer,Integer[]> e : entryMap.entrySet()) {
        arr = e.getValue();
        int sum = 0;
        for (int v = 1; v <= types.size(); v++) {
          sum += e.getValue()[v];
        }
        arr[types.size() + 1] = sum;
        if (team == null || team.getId() == arr[0].intValue())
          vals.add(arr);
      }
      Integer[] sortOrder = new Integer[types.size() + 1];
      sortOrder[0] = types.size() + 1;
      for (int p = 1; p <= types.size(); p++)
        sortOrder[p] = p;

      Collections.sort(vals, new IntegerArrayComparator(sortOrder, false));

      String[] row = new String[types.size() + 2];
      for (Integer[] val : vals) {
        row[0] = nameMap.get(val[0]);
        int r = 1;
        for (int v = 1; v < val.length; v++) {
          row[r++] = val[v].toString();
        }
        tableModel.addRow(row);
      }

    }
    else {
      ArrayList<Integer> strCols = tableModel.getStringColumns();
      strCols.clear();
      strCols.add(0);
      strCols.add(1);
      HashMap<Integer,Team> teamMap = new HashMap<Integer,Team>();

      String[] columns = new String[types.size() + 4];
      columns[0] = ResourceManager.getText(Text.PLAYER);
      columns[1] = ResourceManager.getText(Text.TEAM);
      i = 2;
      for (String tname : types) {
        columns[i++] = tname;
      }
      columns[types.size() + 2] = ResourceManager.getText(Text.ASSIST);
      columns[types.size() + 3] = ResourceManager.getText(Text.TOTAL);
      tableModel.setColumnIdentifiers(columns);
      if (t.getTeams().size() < 1)
        return;
      if (team == null) {
        for (Team tm : t.getTeams()) {
          for (Player p : tm.getPlayers()) {
            Integer[] ints = new Integer[types.size() + 3];
            ints[0] = p.getId();
            for (i = 1; i < types.size() + 2; i++) {
              ints[i] = new Integer(0);
            }
            entryMap.put(p.getId(), ints);
            nameMap.put(p.getId(), p.toString());
            teamMap.put(p.getId(), tm);
          }
        }
      }
      else {
        for (Player p : team.getPlayers()) {
          Integer[] ints = new Integer[types.size() + 3];
          ints[0] = p.getId();
          for (i = 1; i < types.size() + 2; i++) {
            ints[i] = new Integer(0);
          }
          entryMap.put(p.getId(), ints);
          nameMap.put(p.getId(), p.toString());
          teamMap.put(p.getId(), team);
        }
      }
      for (ScheduleEntry se : t.getSchedules()) {
        if (se.isGamePlayed()) {

          int totalmin;
          if (se.getGroupAssignedTo().isDefaultRules()) {
            SportSettings set = t.getSettings();
            totalmin = set.getNumGameTimes() * set.getMinPerGameTime()
                + set.getNumOvertimes() * set.getMinPerOvertime();
          }
          else {
            SportSettings set = se.getGroupAssignedTo().getSettings();
            totalmin = set.getNumGameTimes() * set.getMinPerGameTime()
                + set.getNumOvertimes() * set.getMinPerOvertime();
          }

          for (GameEventEntry ge : se.getGameEvents()) {
            if (ge.getEvent().getType() == GameEventType.GOAL
                && (totalmin > ge.getTimeMin() || (totalmin == ge.getTimeMin() && ge
                    .getTimeSec() == 0))) {
              for (Player mp : ge.getMainPlayers()) {
                Team tm = teamMap.get(mp.getId());
                if (team == null || tm == team)
                  if (tm == ge.getTeam()) {
                    entryMap.get(mp.getId())[typePos.get(ge.getEvent().getId())] += ge
                        .getEvent().getPointsForTeam();
                  }
              }
              for (Player sp : ge.getSecondaryPlayers()) {
                Team tm = teamMap.get(sp.getId());
                if (team == null || tm == team)
                  if (tm == ge.getTeam()) {
                    entryMap.get(sp.getId())[types.size() + 1] += ge.getEvent()
                        .getPointsForTeam();
                  }
              }
            }
          }
        }
      }

      ArrayList<Integer[]> vals = new ArrayList<Integer[]>();
      Integer[] arr;
      for (Map.Entry<Integer,Integer[]> e : entryMap.entrySet()) {
        arr = e.getValue();
        int sum = 0;
        for (int v = 1; v < types.size() + 2; v++) {
          sum += e.getValue()[v];
        }
        if (sum > 0) {
          arr[types.size() + 2] = sum;
          vals.add(arr);
        }
      }

      Integer[] sortOrder = new Integer[types.size() + 2];
      sortOrder[0] = types.size() + 2;
      for (int p = 1; p < types.size() + 2; p++)
        sortOrder[p] = p;

      Collections.sort(vals, new IntegerArrayComparator(sortOrder, false));

      String[] row = new String[types.size() + 4];
      for (Integer[] val : vals) {
        row[0] = nameMap.get(val[0]);
        row[1] = teamMap.get(val[0]).getName();
        int r = 2;
        for (int v = 1; v < val.length; v++) {
          row[r++] = val[v].toString();
        }
        tableModel.addRow(row);
      }
    }
  }
}
