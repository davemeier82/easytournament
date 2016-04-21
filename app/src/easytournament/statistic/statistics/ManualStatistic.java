package easytournament.statistic.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import easytournament.basic.Organizer;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.GameEvent;
import easytournament.basic.valueholder.GameEventEntry;
import easytournament.basic.valueholder.Player;
import easytournament.basic.valueholder.Team;
import easytournament.basic.valueholder.Tournament;
import easytournament.designer.valueholder.ScheduleEntry;
import easytournament.statistic.model.tablemodel.EventSelectionTableModel;
import easytournament.statistic.model.tablemodel.StatisticTableModel;
import easytournament.statistic.util.IntegerArrayComparator;

public class ManualStatistic {

  protected Tournament t = Organizer.getInstance().getCurrentTournament();

  public void addStatistic(StatisticTableModel tableModel, boolean groupByTeam,
      Team team, EventSelectionTableModel esTableModel, Integer fromMin,
      Integer fromSec, Integer toMin, Integer toSec, Calendar begin,
      Calendar end, boolean showTotal) {

    HashMap<Integer,Integer[]> entryMap = new HashMap<Integer,Integer[]>();
    HashMap<Integer,String> nameMap = new HashMap<Integer,String>();

    ArrayList<String> types = new ArrayList<String>();
    HashMap<Integer,ArrayList<Integer>> typePos = new HashMap<Integer,ArrayList<Integer>>();

    int i = 1;
    for (int r = 0; r < esTableModel.getRowCount(); r++) {
      GameEvent e = ((GameEvent)esTableModel.getValueAt(r, 0));
      Boolean secPlayer = ((Boolean)esTableModel.getValueAt(r, 1));

      if (typePos.get(e.getId()) == null) {
        ArrayList<Integer> poss = new ArrayList<Integer>();
        poss.add(i++);
        typePos.put(e.getId(), poss);
      }
      else {
        typePos.get(e.getId()).add(i++);
      }

      if (secPlayer && e.isSecondaryPlayer()) {
        if (esTableModel.getValueAt(r, 4) == null
            || ((String)esTableModel.getValueAt(r, 4)).equals("")) {
          types.add(e.getSecondaryPlayerText());
        }
        else {
          types.add((String)esTableModel.getValueAt(r, 4));
        }
      }
      else if (!secPlayer) {
        if (esTableModel.getValueAt(r, 4) == null
            || ((String)esTableModel.getValueAt(r, 4)).equals("")) {
          types.add(e.getName());
        }
        else {
          types.add((String)esTableModel.getValueAt(r, 4));
        }
      }
      else {
        types.add(ResourceManager.getText(Text.NOT_AVAILABLE));
      }
    }

    if (groupByTeam) {
      ArrayList<Integer> strCols = tableModel.getStringColumns();
      strCols.clear();
      strCols.add(0);
      String[] columns = new String[showTotal? types.size() + 2
          : types.size() + 1];
      columns[0] = ResourceManager.getText(Text.TEAM);
      i = 1;
      for (String tname : types) {
        columns[i++] = tname;
      }
      if (showTotal)
        columns[types.size() + 1] = ResourceManager.getText(Text.TOTAL);
      tableModel.setColumnIdentifiers(columns);
      if (t.getTeams().size() < 1)
        return;
      for (Team tm : t.getTeams()) {

        Integer[] ints = new Integer[showTotal? types.size() + 2
            : types.size() + 1];
        ints[0] = tm.getId();
        for (i = 1; i < ints.length; i++) {
          ints[i] = new Integer(0);
        }
        entryMap.put(tm.getId(), ints);
        nameMap.put(tm.getId(), tm.getName());
      }

      for (ScheduleEntry se : t.getSchedules()) {
        if (se.isGamePlayed()
            && (begin == null || se.getDate().getTimeInMillis() >= begin
                .getTimeInMillis())
            && (end == null || se.getDate().getTimeInMillis() <= end
                .getTimeInMillis())) {

          for (GameEventEntry ge : se.getGameEvents()) {
            if (fromMin == null
                || (fromMin.intValue() < ge.getTimeMin() && toMin.intValue() > ge
                    .getTimeMin())
                || ((fromMin.intValue() == ge.getTimeMin() && fromSec
                    .intValue() <= ge.getTimeSec()) && (toMin.intValue() > ge
                    .getTimeMin() || (toMin.intValue() == ge.getTimeMin() && toSec
                    .intValue() >= ge.getTimeSec())))
                || ((toMin.intValue() == ge.getTimeMin() && toSec.intValue() >= ge
                    .getTimeSec()) && (fromMin.intValue() < ge.getTimeMin() || (fromMin
                    .intValue() == ge.getTimeMin() && fromSec.intValue() <= ge
                    .getTimeSec())))) {
              int eventID = ge.getEvent().getId();
              ArrayList<Integer> positions = typePos.get(eventID);

              if (positions != null)
                for (Integer p : positions) {

                  boolean secPlayer = (Boolean)esTableModel
                      .getValueAt(p - 1, 1);
                  boolean onlyNumber = (Boolean)esTableModel.getValueAt(p - 1,
                      2);
                  boolean wrongTeam = (Boolean)esTableModel
                      .getValueAt(p - 1, 3);

                  if (secPlayer) {
                    if (ge.getSecondaryPlayers().getSize() >= 0) {
                      if (ge.getSecondaryPlayers().getSize() == 0
                          || ge.getTeam().getPlayers()
                              .contains(ge.getSecondaryPlayers().get(0))) {
                        if (onlyNumber) {
                          if (ge.getTeam().equals(se.getHomeTeam()))
                            entryMap.get(se.getHomeTeam().getId())[p]++;
                          else if (ge.getTeam().equals(se.getAwayTeam()))
                            entryMap.get(se.getAwayTeam().getId())[p]++;
                        }
                        else {
                          if (ge.getTeam().equals(se.getHomeTeam())) {
                            entryMap.get(se.getHomeTeam().getId())[p] += ge
                                .getEvent().getPointsForTeam();
                            entryMap.get(se.getAwayTeam().getId())[p] += ge
                                .getEvent().getPointsForOpponent();
                          }
                          else {
                            entryMap.get(se.getHomeTeam().getId())[p] += ge
                                .getEvent().getPointsForOpponent();
                            entryMap.get(se.getAwayTeam().getId())[p] += ge
                                .getEvent().getPointsForTeam();
                          }
                        }
                      }
                    }
                  }
                  else {
                    if (wrongTeam) {
                      if (ge.getMainPlayers().getSize() > 0) {
                        if (!ge.getTeam().getPlayers()
                            .contains(ge.getMainPlayers().get(0))) {
                          if (onlyNumber) {
                            if (ge.getTeam().equals(se.getHomeTeam()))
                              entryMap.get(se.getHomeTeam().getId())[p]++;
                            else if (ge.getTeam().equals(se.getAwayTeam()))
                              entryMap.get(se.getAwayTeam().getId())[p]++;
                          }
                          else {
                            if (ge.getTeam().equals(se.getHomeTeam())) {
                              entryMap.get(se.getHomeTeam().getId())[p] += ge
                                  .getEvent().getPointsForTeam();
                              entryMap.get(se.getAwayTeam().getId())[p] += ge
                                  .getEvent().getPointsForOpponent();
                            }
                            else {
                              entryMap.get(se.getHomeTeam().getId())[p] += ge
                                  .getEvent().getPointsForOpponent();
                              entryMap.get(se.getAwayTeam().getId())[p] += ge
                                  .getEvent().getPointsForTeam();
                            }
                          }
                        }
                      }
                    }
                    else {
                      if (ge.getMainPlayers().getSize() >= 0) {
                        if (ge.getMainPlayers().getSize() == 0
                            || ge.getTeam().getPlayers()
                                .contains(ge.getMainPlayers().get(0))) {
                          if (onlyNumber) {
                            if (ge.getTeam().equals(se.getHomeTeam()))
                              entryMap.get(se.getHomeTeam().getId())[p]++;
                            else if (ge.getTeam().equals(se.getAwayTeam()))
                              entryMap.get(se.getAwayTeam().getId())[p]++;
                          }
                          else {
                            if (ge.getTeam().equals(se.getHomeTeam())) {
                              entryMap.get(se.getHomeTeam().getId())[p] += ge
                                  .getEvent().getPointsForTeam();
                              entryMap.get(se.getAwayTeam().getId())[p] += ge
                                  .getEvent().getPointsForOpponent();
                            }
                            else {
                              entryMap.get(se.getHomeTeam().getId())[p] += ge
                                  .getEvent().getPointsForOpponent();
                              entryMap.get(se.getAwayTeam().getId())[p] += ge
                                  .getEvent().getPointsForTeam();
                            }
                          }
                        }
                      }
                    }
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
        if (showTotal) {
          int sum = 0;
          for (int v = 1; v <= types.size(); v++) {
            sum += e.getValue()[v];
          }
          arr[types.size() + 1] = sum;
        }
        if (team == null || team.getId() == arr[0].intValue())
          vals.add(arr);
      }
      Integer[] sortOrder = null;
      if (showTotal) {
        sortOrder = new Integer[types.size() + 1];
        sortOrder[0] = types.size() + 1;
        for (int p = 1; p <= types.size(); p++)
          sortOrder[p] = p;
      }
      else {
        sortOrder = new Integer[types.size()];
        for (int p = 0; p < types.size(); p++)
          sortOrder[p] = p + 1;
      }

      Collections.sort(vals, new IntegerArrayComparator(sortOrder, false));

      String[] row = new String[showTotal? types.size() + 2 : types.size() + 1];
      for (Integer[] val : vals) {
        row[0] = nameMap.get(val[0]);
        int r = 1;
        for (int v = 1; v < val.length; v++) {
          row[r++] = val[v].toString();
        }
        tableModel.addRow(row);
      }
    } // END TEAMs
    else {
      ArrayList<Integer> strCols = tableModel.getStringColumns();
      strCols.clear();
      strCols.add(0);
      strCols.add(1);
      HashMap<Integer,Team> teamMap = new HashMap<Integer,Team>();

      String[] columns = new String[showTotal? esTableModel.getRowCount() + 3
          : esTableModel.getRowCount() + 2];
      columns[0] = ResourceManager.getText(Text.PLAYER);
      columns[1] = ResourceManager.getText(Text.TEAM);
      i = 2;
      for (int r = 0; r < esTableModel.getRowCount(); r++) {
        GameEvent e = ((GameEvent)esTableModel.getValueAt(r, 0));
        Boolean secPlayer = ((Boolean)esTableModel.getValueAt(r, 1));

        if (secPlayer && e.isSecondaryPlayer()) {
          if (esTableModel.getValueAt(r, 4) == null
              || ((String)esTableModel.getValueAt(r, 4)).equals("")) {
            columns[i++] = e.getSecondaryPlayerText();
          }
          else {
            columns[i++] = (String)esTableModel.getValueAt(r, 4);
          }
        }
        else if (!secPlayer) {
          if (esTableModel.getValueAt(r, 4) == null
              || ((String)esTableModel.getValueAt(r, 4)).equals("")) {
            columns[i++] = e.getName();
          }
          else {
            columns[i++] = (String)esTableModel.getValueAt(r, 4);
          }
        } else {
          columns[i++] = ResourceManager.getText(Text.NOT_AVAILABLE);
        }
      }
      if (showTotal)
        columns[esTableModel.getRowCount() + 2] = ResourceManager
            .getText(Text.TOTAL);
      tableModel.setColumnIdentifiers(columns);

      if (t.getTeams().size() < 1)
        return;
      if (team == null) {
        for (Team tm : t.getTeams()) {
          for (Player p : tm.getPlayers()) {
            Integer[] ints = new Integer[showTotal? esTableModel.getRowCount() + 2
                : esTableModel.getRowCount() + 1];
            ints[0] = p.getId();
            for (i = 1; i < ints.length; i++) {
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
          Integer[] ints = new Integer[showTotal? esTableModel.getRowCount() + 2
              : esTableModel.getRowCount() + 1];
          ints[0] = p.getId();
          for (i = 1; i < ints.length; i++) {
            ints[i] = new Integer(0);
          }
          entryMap.put(p.getId(), ints);
          nameMap.put(p.getId(), p.toString());
          teamMap.put(p.getId(), team);
        }
      }
      for (ScheduleEntry se : t.getSchedules()) {
        if (se.isGamePlayed()
            && (begin == null || se.getDate().getTimeInMillis() >= begin
                .getTimeInMillis())
            && (end == null || se.getDate().getTimeInMillis() <= end
                .getTimeInMillis())) {

          for (GameEventEntry ge : se.getGameEvents()) {
            if (fromMin == null
                || (fromMin.intValue() < ge.getTimeMin() && toMin.intValue() > ge
                    .getTimeMin())
                || ((fromMin.intValue() == ge.getTimeMin() && fromSec
                    .intValue() <= ge.getTimeSec()) && (toMin.intValue() > ge
                    .getTimeMin() || (toMin.intValue() == ge.getTimeMin() && toSec
                    .intValue() >= ge.getTimeSec())))
                || ((toMin.intValue() == ge.getTimeMin() && toSec.intValue() >= ge
                    .getTimeSec()) && (fromMin.intValue() < ge.getTimeMin() || (fromMin
                    .intValue() == ge.getTimeMin() && fromSec.intValue() <= ge
                    .getTimeSec())))) {

              int eventID = ge.getEvent().getId();
              ArrayList<Integer> positions = typePos.get(eventID);

              if (positions != null)
                for (Integer p : positions) {

                  boolean secPlayer = (Boolean)esTableModel
                      .getValueAt(p - 1, 1);
                  boolean onlyNumber = (Boolean)esTableModel.getValueAt(p - 1,
                      2);
                  boolean wrongTeam = (Boolean)esTableModel
                      .getValueAt(p - 1, 3);

                  if (secPlayer) {
                    for (Player mp : ge.getSecondaryPlayers()) {
                      Team tm = teamMap.get(mp.getId());
                      if (team == null || tm == team) {
                        if (onlyNumber) {
                          entryMap.get(mp.getId())[p]++;
                        }
                        else {
                          entryMap.get(mp.getId())[p] += ge.getEvent()
                              .getPointsForTeam();
                        }
                      }
                    }
                  }
                  else {
                    if (wrongTeam) {
                      for (Player mp : ge.getMainPlayers()) {
                        Team tm = teamMap.get(mp.getId());
                        if (!ge.getTeam().equals(tm)) {
                          if (team == null || tm == team) {
                            if (onlyNumber) {
                              entryMap.get(mp.getId())[p]++;
                            }
                            else {
                              entryMap.get(mp.getId())[p] += ge.getEvent()
                                  .getPointsForTeam();
                            }
                          }
                        }
                      }
                    }
                    else {
                      for (Player mp : ge.getMainPlayers()) {
                        Team tm = teamMap.get(mp.getId());
                        if (ge.getTeam().equals(tm)) {
                          if (team == null || tm == team) {
                            if (onlyNumber) {
                              entryMap.get(mp.getId())[p]++;
                            }
                            else {
                              entryMap.get(mp.getId())[p] += ge.getEvent()
                                  .getPointsForTeam();
                            }
                          }
                        }
                      }
                    }
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
        for (int v = 1; v < types.size() + 1; v++) {
          sum += e.getValue()[v];
        }
        if (sum > 0) {
          if (showTotal)
            arr[types.size() + 1] = sum;
          vals.add(arr);
        }
      }

      Integer[] sortOrder = null;
      if (showTotal) {
        sortOrder = new Integer[types.size() + 1];
        sortOrder[0] = types.size() + 1;
        for (int p = 1; p < types.size() + 1; p++)
          sortOrder[p] = p;
      }
      else {
        sortOrder = new Integer[types.size()];
        for (int p = 0; p < types.size(); p++)
          sortOrder[p] = p + 1;
      }

      Collections.sort(vals, new IntegerArrayComparator(sortOrder, false));

      String[] row = new String[showTotal? types.size() + 3 : types.size() + 2];
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
