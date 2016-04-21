package easytournament.designer.valueholder;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.resources.ResourceManager;
import easytournament.basic.valueholder.*;


public class ScheduleEntry extends Model implements ListDataListener {

  private static final long serialVersionUID = 8963747152856899405L;

  public static final String PROPERTY_HOMEPOS = "homePos";
  public static final String PROPERTY_AWAYPOS = "awayPos";
  public static final String PROPERTY_PLACE = "place";
  public static final String PROPERTY_GAMEEVENTS = "gameEvents";
  public static final String PROPERTY_DATE = "date";
  public static final String PROPERTY_REFREES = "referees";
  public static final String PROPERTY_RESULTS = "results";
  public static final String PROPERTY_GAME_PLAYED = "gamePlayed";

  private Position homePos;
  private Position awayPos;
  private String place = "";
  private Calendar date = new GregorianCalendar(ResourceManager.getLocale());
  private ArrayListModel<GameEventEntry> gameEvents = new ArrayListModel<GameEventEntry>();
  private ArrayListModel<Refree> referees = new ArrayListModel<Refree>();
  private ArrayListModel<Integer> results = new ArrayListModel<Integer>();
  private ArrayListModel<Boolean> checked = new ArrayListModel<Boolean>();

  private boolean gamePlayed = false;

  public ScheduleEntry(Position homePos, Position awayPos) {
    super();
    this.homePos = homePos;
    this.awayPos = awayPos;
    this.referees.addListDataListener(this);
    this.results.addListDataListener(this);
    this.gameEvents.addListDataListener(this);
  }

  public int getHomeScore() {
    if (isAdvancedGoalMode()) {
      return this.gameEvents.get(this.gameEvents.size() - 1)
          .getSummedHomePoints();
    }
    if (this.results.size() > 1)
      return this.results.get(this.results.size() - 2);
    return 0;
  }

  public int getAwayScore() {
    if (isAdvancedGoalMode()) {
      return this.gameEvents.get(this.gameEvents.size() - 1)
          .getSummedAwayPoints();
    }
    if (this.results.size() > 1)
      return this.results.get(this.results.size() - 1);
    return 0;
  }

  public ArrayListModel<Refree> getReferees() {
    return referees;
  }

  public void setReferees(ArrayListModel<Refree> referees) {
    this.referees.removeListDataListener(this);
    ArrayListModel<Refree> old = this.referees;
    this.referees = referees;
    this.referees.addListDataListener(this);
    this.firePropertyChange(PROPERTY_REFREES, old, this.referees);
  }

  public Position getHomePos() {
    return homePos;
  }

  public void setHomePos(Position homePos) {
    Position old = this.homePos;
    this.homePos = homePos;
    this.firePropertyChange(PROPERTY_HOMEPOS, old, this.homePos);
  }

  public Position getAwayPos() {
    return awayPos;
  }

  public void setAwayPos(Position awayPos) {
    Position old = this.awayPos;
    this.awayPos = awayPos;
    this.firePropertyChange(PROPERTY_AWAYPOS, old, this.awayPos);
  }

  public Team getHomeTeam() {
    return this.homePos.getTeam();
  }

  public Team getAwayTeam() {
    return this.awayPos.getTeam();
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    String old = this.place;
    this.place = place;
    this.firePropertyChange(PROPERTY_PLACE, old, this.place);
  }

  public Calendar getDate() {
    return date;
  }

  public void setDate(Calendar date) {
    Calendar old = this.date;
    this.date = date;
    this.firePropertyChange(PROPERTY_DATE, old, this.date);
  }

  public AbstractGroup getGroupAssignedTo() {
    if(homePos == null)
      return null;
    return homePos.getGroup();
  }

  public boolean isAdvancedGoalMode() {
    return gameEvents.size() > 0;
  }

  public boolean isGamePlayed() {
    return gamePlayed && getHomeTeam() != null && getAwayTeam() != null;
  }

  public void setGamePlayed(boolean gamePlayed) {
    boolean old = this.gamePlayed;
    this.gamePlayed = gamePlayed;
    this.firePropertyChange(PROPERTY_GAME_PLAYED, old, this.gamePlayed);
  }

  public void setNumHomeGoals(int numHomeGoals) {
    if (this.results.size() < 2) {
      while (this.results.size() < 2) {
        this.results.add(new Integer(0));
        this.checked.add(false);
      }
    }
    for (int i = 2; i < this.results.size() - 2; i += 2) {
      this.results.set(i, 0);
    }
    this.results.set(0, numHomeGoals);
    this.results.set(this.results.size() - 2, numHomeGoals);
  }

  public void setNumAwayGoals(int numAwayGoals) {
    if (this.results.size() < 2) {
      while (this.results.size() < 2) {
        this.results.add(new Integer(0));
        this.checked.add(false);
      }
    }
    for (int i = 3; i < this.results.size() - 2; i += 2) {
      this.results.set(i, 0);
    }
    this.results.set(1, numAwayGoals);
    this.results.set(this.results.size() - 1, numAwayGoals);
  }

  public ArrayListModel<GameEventEntry> getGameEvents() {
    return gameEvents;
  }

  public void setGameEvents(ArrayListModel<GameEventEntry> gameEvents) {
    this.gameEvents.removeListDataListener(this);
    ArrayListModel<GameEventEntry> old = this.gameEvents;
    this.gameEvents = gameEvents;
    this.gameEvents.addListDataListener(this);
    this.firePropertyChange(PROPERTY_GAMEEVENTS, old, this.gameEvents);
  }

  public ArrayListModel<Integer> getResults() {
    return results;
  }

  public void setResults(ArrayListModel<Integer> results) {
    this.results.removeListDataListener(this);
    ArrayListModel<Integer> old = this.results;
    this.results = results;
    this.results.addListDataListener(this);
    this.firePropertyChange(PROPERTY_RESULTS, old, this.results);
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    if (e.getSource() == results) {
      this.firePropertyChange(PROPERTY_RESULTS, null, this.results);
    }
    else if (e.getSource() == referees) {
      this.firePropertyChange(PROPERTY_REFREES, null, this.referees);
    } else if (e.getSource() == gameEvents) {
      this.firePropertyChange(PROPERTY_GAMEEVENTS, null, this.gameEvents);
    }
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    if (e.getSource() == results) {
      this.firePropertyChange(PROPERTY_RESULTS, null, this.results);
    }
    else if (e.getSource() == referees) {
      this.firePropertyChange(PROPERTY_REFREES, null, this.referees);
    } else if (e.getSource() == gameEvents) {
      this.firePropertyChange(PROPERTY_GAMEEVENTS, null, this.gameEvents);
    }
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    if (e.getSource() == results) {
      this.firePropertyChange(PROPERTY_RESULTS, null, this.results);
    }
    else if (e.getSource() == referees) {
      this.firePropertyChange(PROPERTY_REFREES, null, this.referees);
    } else if (e.getSource() == gameEvents) {
      updateScore();
      this.firePropertyChange(PROPERTY_GAMEEVENTS, null, this.gameEvents);
    }
  }
  
  public void updateScore(){
    int homePts = 0, tmpHome;
    int awayPts = 0, tmpAway;

    for(GameEventEntry e : gameEvents){
      GameEvent gameEvent = e.getEvent();
      if(gameEvent != null) {
        if(e.getTeam().equals(this.getHomeTeam())) {
          tmpHome = gameEvent.getPointsForTeam();
          tmpAway = gameEvent.getPointsForOpponent();
        } else {
          tmpHome = gameEvent.getPointsForOpponent();
          tmpAway = gameEvent.getPointsForTeam();
        }
        homePts += tmpHome;
        awayPts += tmpAway;
        e.setSummedHomePoints(homePts);
        e.setSummedAwayPoints(awayPts); 
      }
    }
  }

  public ArrayListModel<Boolean> getChecked() {
    return checked;
  }

  public void setChecked(ArrayListModel<Boolean> checked) {
    this.checked = checked;
  }

}
