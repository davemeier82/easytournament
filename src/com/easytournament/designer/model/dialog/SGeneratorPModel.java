package com.easytournament.designer.model.dialog;

import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.model.tablemodel.SelectableTableModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.designer.model.tablemodel.GroupSelTableModel;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

public class SGeneratorPModel extends Model {

  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;
  public static final String PROPERTY_DISPOSE = "dispose";
  public static final String PROPERTY_NUMGAMES = "numGames";
  public static final String PROPERTY_MAX_CONCURRENT_GAMES = "maxConcurrentGames";
  public static final String PROPERTY_BREAK_DAYS = "breakDays";
  public static final String PROPERTY_BREAK_HOURS = "breakHours";
  public static final String PROPERTY_BREAK_MIN = "breakMin";
  public static final String PROPERTY_STARTTIME = "startTime";
  public static final String PROPERTY_STARTDATE = "startDate";
  public static final String PROPERTY_MAXCG_SELECTED = "maxCGamesSelected";
  public static final String PROPERTY_CONSECUTIVE_GAMES_ALLOWED = "consecutiveGamesAllowed";
  protected int numGames = 1;
  protected int maxConcurrentGames = 3;
  protected int breakDays = 0;
  protected int breakHours = 0;
  protected int breakMin = 10;

  protected DateFormat dateFormatter = DateFormat.getDateInstance(
      DateFormat.SHORT, ResourceManager.getLocale());
  protected Calendar calendar = new GregorianCalendar(
      ResourceManager.getLocale());
  protected Date startDate;

  protected boolean maxCGamesSelected = false;
  protected boolean consecutiveGamesAllowed = false;
  protected GroupSelTableModel tableModel;
  protected ArrayList<Boolean> selectedGroups = new ArrayList<Boolean>();

  protected Tournament t = Organizer.getInstance().getCurrentTournament();

  public SGeneratorPModel() {
    startDate = (Date)t.getBegin().getTime().clone();
    calendar = (Calendar)t.getBegin().clone();
    for (int i = 0; i < t.getPlan().getOrderedGroups().size(); i++)
      selectedGroups.add(new Boolean(true));
    tableModel = new GroupSelTableModel(t.getPlan().getOrderedGroups(),
        selectedGroups);
  }

  public Action getAction(int actionkey) {
    switch (actionkey) {
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            SGeneratorPModel.this.firePropertyChange(PROPERTY_DISPOSE, false,
                true);

          }
        };
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            ArrayListModel<ScheduleEntry> entries = t.getSchedules();
            HashMap<AbstractGroup,Integer> groupToIdxMap = new HashMap<AbstractGroup,Integer>();
            for (int i = 0; i < selectedGroups.size(); i++) {
              groupToIdxMap.put(tableModel.getGroupAt(i), i);
            }

            ArrayList<AbstractGroup> _processedGroups = new ArrayList<AbstractGroup>();
            ArrayList<AbstractGroup> _toProcessNext = new ArrayList<AbstractGroup>();

            Calendar date = (Calendar)calendar.clone();
            Calendar tmp = new GregorianCalendar();
            tmp.setTime(startDate);
            date.set(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH),
                tmp.get(Calendar.DAY_OF_MONTH));

            int lastProcessCount;
            do {
              for (int i = 0; i < selectedGroups.size(); i++) {
                if (selectedGroups.get(i)) {
                  AbstractGroup g = tableModel.getGroupAt(i);
                  if (!_processedGroups.contains(g)) {
                    boolean skipGroup = false;
                    ArrayList<Position> positions = g.getPositions();
                    for (Position p : positions) {
                      if (p.getPrev() != null
                          && !_processedGroups.contains(p.getPrev().getGroup())
                          && selectedGroups.get(groupToIdxMap.get(p.getPrev()
                              .getGroup()))) {
                        // previous group is not processet yet
                        skipGroup = true;
                        break;

                      }
                    }
                    if (skipGroup) {
                      continue;
                    }
                    _toProcessNext.add(g);
                  }
                }
              }
              insertScheduleEntriesForGroups(entries, _toProcessNext, date);
              _processedGroups.addAll(_toProcessNext);
              lastProcessCount = _toProcessNext.size();
              _toProcessNext.clear();
            } while (lastProcessCount > 0);

            SGeneratorPModel.this.firePropertyChange(PROPERTY_DISPOSE, false,
                true);
          }

          public void insertScheduleEntriesForGroups(
              ArrayListModel<ScheduleEntry> entries,
              ArrayList<AbstractGroup> _toProcessNext, Calendar date) {
            ArrayListModel<ScheduleEntry> onegameEntries = new ArrayListModel<ScheduleEntry>();
            ArrayList<Position[]> games = new ArrayList<Position[]>();
            HashMap<Position,ArrayList<Position>> hgames = new HashMap<Position,ArrayList<Position>>();

            for (AbstractGroup g : _toProcessNext) {
              for (Position p : g.getPositions()) {
                hgames.put(p, new ArrayList<Position>());
              }

              ArrayList<Position> positions = g.getPositions();

              for (int j = 0; j < positions.size() - 1; j++) {
                for (int k = j + 1; k < positions.size(); k++) {
                  games
                      .add(new Position[] {positions.get(j), positions.get(k)});
                }
              }
            }

            ArrayList<Position> played = new ArrayList<Position>();
            ArrayList<Position> lastplayed = new ArrayList<Position>();
            ArrayList<Position> skipped = new ArrayList<Position>();
            int gameCount = 0;
            boolean maxGamesReached = false;
            while (games.size() > 0) {
              boolean addedGame = false;
              for (AbstractGroup g : _toProcessNext) {
                for (Position p : g.getPositions()) {
                  if (!played.contains(p)
                      && (consecutiveGamesAllowed || !lastplayed.contains(p)))
                    if (findGame(entries, onegameEntries, games, hgames, date,
                        played, lastplayed, consecutiveGamesAllowed, p)) {
                      addedGame = true;
                      if (maxCGamesSelected) {
                        gameCount++;
                        if (gameCount == maxConcurrentGames) {
                          gameCount = 0;
                          incrementTime(date, played, lastplayed);
                        }
                      }
                    }
                    else {
                      skipped.add(p);
                    }
                }
              }
              
              if (!addedGame) {
                gameCount = 0;
                incrementTime(date, played, lastplayed);
              }

              ArrayList<Position> addedPositions = new ArrayList<Position>();
              for (Position p : skipped) {
                if (!played.contains(p)
                    && (consecutiveGamesAllowed || !lastplayed.contains(p)))
                  if (findGame(entries, onegameEntries, games, hgames, date,
                      played, lastplayed, consecutiveGamesAllowed, p)) {
                    addedPositions.add(p);
                    addedGame = true;
                    if (maxCGamesSelected) {
                      gameCount++;
                      if (gameCount == maxConcurrentGames) {
                        gameCount = 0;
                        incrementTime(date, played, lastplayed);
                      }
                    }
                  }
              }
              skipped.removeAll(addedPositions);
            }

            if (played.size() > 0) {
              date.add(Calendar.HOUR, breakDays * 24 + breakHours);
              date.add(Calendar.MINUTE, breakMin
                  + t.getSettings().getMinPerGameTime()
                  * t.getSettings().getNumGameTimes());
            }

            
            // add games in same order but with home/away changed
            if (onegameEntries.size() > 0) {

              Calendar lastTime = (Calendar)onegameEntries.get(0).getDate()
                  .clone();

              for (int h = 1; h < numGames; h++) {

                for (ScheduleEntry se : onegameEntries) {

                  if (!lastTime.equals(se.getDate())) {
                    lastTime = (Calendar)se.getDate().clone();
                    date.add(Calendar.HOUR, breakDays * 24 + breakHours);
                    date.add(Calendar.MINUTE, breakMin
                        + t.getSettings().getMinPerGameTime()
                        * t.getSettings().getNumGameTimes());
                  }
                  ScheduleEntry s;
                  if (h % 2 == 0) {
                    s = new ScheduleEntry(se.getHomePos(), se.getAwayPos());
                  }
                  else {
                    s = new ScheduleEntry(se.getAwayPos(), se.getHomePos());
                  }
                  s.setDate((Calendar)date.clone());
                  entries.add(s);
                  se.getHomePos().getGroup().getSchedules().add(s);
                }
              }
            }
          }

          public void incrementTime(Calendar date, ArrayList<Position> played,
              ArrayList<Position> lastplayed) {
            date.add(Calendar.HOUR, breakDays * 24 + breakHours);
            date.add(Calendar.MINUTE, breakMin
                + t.getSettings().getMinPerGameTime()
                * t.getSettings().getNumGameTimes());
            lastplayed.clear();
            lastplayed.addAll(played);
            played.clear();
          }

          public boolean findGame(ArrayListModel<ScheduleEntry> entries,
              ArrayListModel<ScheduleEntry> onegameEntries,
              ArrayList<Position[]> games,
              HashMap<Position,ArrayList<Position>> hgames, Calendar date,
              ArrayList<Position> played, ArrayList<Position> lastplayed,
              boolean consecutiveGamesAllowed, Position p) {
            if (!played.contains(p)) {
              boolean gameFound = false;
              for (Position[] game : games) {
                if ((game[0].equals(p) && !played.contains(game[1]))
                    || (game[1].equals(p) && !played.contains(game[0]))) {
                  if (!consecutiveGamesAllowed
                      && (lastplayed.contains(game[0]) || lastplayed
                          .contains(game[1])))
                    continue;
                  if (hgames.get(game[0]).size() <= hgames.get(game[1]).size()) {
                    hgames.get(game[0]).add(game[1]);
                    ScheduleEntry se = new ScheduleEntry(game[0], game[1]);
                    se.setDate((Calendar)date.clone());
                    onegameEntries.add(se);
                    entries.add(se);
                    game[0].getGroup().getSchedules().add(se);
                  }
                  else {
                    hgames.get(game[1]).add(game[0]);
                    ScheduleEntry se = new ScheduleEntry(game[1], game[0]);
                    se.setDate((Calendar)date.clone());
                    onegameEntries.add(se);
                    entries.add(se);
                    game[0].getGroup().getSchedules().add(se);
                  }
                  games.remove(game);
                  played.add(game[0]);
                  played.add(game[1]);
                  gameFound = true;
                  break;
                }
              }
              return gameFound;
            }
            return false;
          }
        };
    }
    return null;
  }

  public int getNumGames() {
    return numGames;
  }

  public void setNumGames(int numGames) {
    this.numGames = numGames;
  }

  public int getMaxConcurrentGames() {
    return maxConcurrentGames;
  }

  public void setMaxConcurrentGames(int maxConcurrentGames) {
    this.maxConcurrentGames = maxConcurrentGames;
  }

  public int getBreakDays() {
    return breakDays;
  }

  public void setBreakDays(int breakDays) {
    this.breakDays = breakDays;
  }

  public int getBreakHours() {
    return breakHours;
  }

  public void setBreakHours(int breakHours) {
    this.breakHours = breakHours;
  }

  public int getBreakMin() {
    return breakMin;
  }

  public void setBreakMin(int breakMin) {
    this.breakMin = breakMin;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date date) {
    Date old = this.startDate;
    this.startDate = date;
    this.firePropertyChange(PROPERTY_STARTDATE, old, this.startDate);
  }

  public void setStartTime(Date time) {
    calendar.setTime(time);
  }

  public Date getStartTime() {
    return calendar.getTime();
  }

  public SelectableTableModel getTableModel() {
    return tableModel;
  }

  public void toggleSelected(int row) {
    if (row >= 0) {
      selectedGroups.set(row, !selectedGroups.get(row).booleanValue());
      tableModel.fireTableDataChanged();
    }
  }

  public boolean isMaxCGamesSelected() {
    return maxCGamesSelected;
  }

  public void setMaxCGamesSelected(boolean maxCGamesSelected) {
    this.maxCGamesSelected = maxCGamesSelected;
  }

  public boolean isConsecutiveGamesAllowed() {
    return consecutiveGamesAllowed;
  }

  public void setConsecutiveGamesAllowed(boolean consecutiveGamesAllowed) {
    this.consecutiveGamesAllowed = consecutiveGamesAllowed;
  }

}
