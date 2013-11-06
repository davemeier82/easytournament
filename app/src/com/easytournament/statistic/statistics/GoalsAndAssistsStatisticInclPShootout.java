package com.easytournament.statistic.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEventEntry;
import com.easytournament.basic.valueholder.Player;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.statistic.model.tablemodel.StatisticTableModel;
import com.easytournament.statistic.util.IntegerArrayComparator;



public class GoalsAndAssistsStatisticInclPShootout implements Statistic {

  protected Tournament t = Organizer.getInstance().getCurrentTournament();

  @Override
  public String getName() {
    return ResourceManager.getText(Text.GOAL_ASSITS_INCLPS);
  }

  @Override
  public String getPrintName() {
    return ResourceManager.getText(Text.TOPSCORER);
  }

  @Override
  public boolean existsForTeam() {
    return false;
  }

  @Override
  public boolean existsForPlayers() {
    return true;
  }

  @Override
  public void addStatistic(StatisticTableModel tableModel, boolean groupByTeam,
      Team team) {

    if (!groupByTeam) {
      ArrayList<Integer> strCols = tableModel.getStringColumns();
      strCols.clear();
      strCols.add(0);
      strCols.add(1);
      tableModel.setColumnIdentifiers(new String[] {
          ResourceManager.getText(Text.PLAYER),
          ResourceManager.getText(Text.TEAM),
          ResourceManager.getText(Text.GOALS),
          ResourceManager.getText(Text.ASSIST),
          ResourceManager.getText(Text.TOTAL)});
      HashMap<Integer,Integer[]> entryMap = new HashMap<Integer,Integer[]>();
      HashMap<Integer,String> nameMap = new HashMap<Integer,String>();
      HashMap<Integer,Team> teamMap = new HashMap<Integer,Team>();
      if (t.getTeams().size() < 1)
        return;
      if (team == null) {
        for (Team tm : t.getTeams()) {
          for (Player p : tm.getPlayers()) {
            entryMap.put(p.getId(), new Integer[] {p.getId(), 0, 0, 0});
            nameMap.put(p.getId(), p.toString());
            teamMap.put(p.getId(), tm);
          }
        }
      }
      else {
        for (Player p : team.getPlayers()) {
          entryMap.put(p.getId(), new Integer[] {p.getId(), 0, 0, 0});
          nameMap.put(p.getId(), p.toString());
          teamMap.put(p.getId(), team);
        }
      }
      for (ScheduleEntry se : t.getSchedules()) {
        if (se.isGamePlayed())
          for (GameEventEntry ge : se.getGameEvents()) {
            for (Player mp : ge.getMainPlayers()) {
              Team tm = teamMap.get(mp.getId());
              if (team == null || tm == team)
                if (tm == ge.getTeam()) {
                  entryMap.get(mp.getId())[1] += ge.getEvent()
                      .getPointsForTeam();
                }
            }
            for (Player sp : ge.getSecondaryPlayers()) {
              Team tm = teamMap.get(sp.getId());
              if (team == null || tm == team)
                if (tm == ge.getTeam()) {
                  entryMap.get(sp.getId())[2] += ge.getEvent()
                      .getPointsForTeam();
                }
            }
          }
      }
      ArrayList<Integer[]> vals = new ArrayList<Integer[]>();
      Integer[] arr;
      for (Map.Entry<Integer,Integer[]> e : entryMap.entrySet()) {
        arr = e.getValue();
        arr[3] = arr[1] + arr[2];
        if (arr[3] > 0)
          vals.add(arr);
      }
      Collections.sort(vals, new IntegerArrayComparator(
          new Integer[] {3, 1, 2}, false));

      for (Integer[] val : vals) {
        tableModel.addRow(new String[] {nameMap.get(val[0]),
            teamMap.get(val[0]).getName(), val[1].toString(),
            val[2].toString(), val[3].toString()});
      }
    }
  }

}
