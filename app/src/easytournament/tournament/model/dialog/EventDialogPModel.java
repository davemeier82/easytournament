package easytournament.tournament.model.dialog;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.gameevent.GameEventType;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.model.listmodel.PlayerListModel;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.GameEvent;
import easytournament.basic.valueholder.GameEventEntry;
import easytournament.basic.valueholder.Player;
import easytournament.basic.valueholder.Team;
import easytournament.designer.valueholder.ScheduleEntry;


public class EventDialogPModel extends Model {

  public static final String DISPOSE = "dispose";
  public static final String PROPERTY_TEAMFORLIST = "teamForList";
  public static final String PROPERTY_HOMETEAM = "homeTeam";

  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;
  public static final int ADD_PLAYER_ACTION = 2;
  public static final int REMOVE_PLAYER_ACTION = 3;
  public static final int ADD_ASSIST_ACTION = 4;
  public static final int REMOVE_ASSIST_ACTION = 5;

  public static final int PLAYER_LIST = 0;
  public static final int ASSIST_LIST = 1;
  public static final int ALL_PLAYERS_LIST = 2;

  protected PlayerListModel allPlayerListModel;
  protected PlayerListModel playerListModel;
  protected PlayerListModel assistListModel;
  protected DefaultListSelectionModel allPlayerSelModel = new DefaultListSelectionModel();
  protected DefaultListSelectionModel playerSelModel = new DefaultListSelectionModel();
  protected DefaultListSelectionModel assistSelModel = new DefaultListSelectionModel();
  protected Team teamForList;
  protected boolean homeTeam;

  protected GameEventEntry entry, oldEntry;
  protected ScheduleEntry sentry;

  protected ArrayListModel<Player> allPlayers;

  ArrayListModel<GameEventEntry> eventList;

  protected boolean newEntry;

  public EventDialogPModel(ScheduleEntry sentry,
      ArrayListModel<GameEventEntry> eventList) {
    this.entry = new GameEventEntry();
    this.sentry = sentry;
    this.eventList = eventList;
    this.newEntry = true;
    this.homeTeam = true;
    this.init();
    ArrayListModel<GameEvent> gameEvents = Organizer.getInstance().getCurrentTournament()
        .getGameEvents();
    if(!gameEvents.isEmpty())
      this.entry.setEvent(gameEvents.get(0));
  }

  public EventDialogPModel(ScheduleEntry sentry,
      ArrayListModel<GameEventEntry> eventList, GameEventEntry entry) {
    this.oldEntry = entry;
    try {
      this.entry = (GameEventEntry)entry.clone();
    }
    catch (CloneNotSupportedException e) {
      ErrorLogger.getLogger().throwing("EventDialogPModel", "constructor", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
    this.eventList = eventList;
    this.sentry = sentry;
    this.newEntry = false;
    this.homeTeam = entry.getTeam().equals(sentry.getHomeTeam());
    this.init();
  }

  protected void init() {
    this.playerListModel = new PlayerListModel(this.entry.getMainPlayers());
    this.assistListModel = new PlayerListModel(this.entry.getSecondaryPlayers());
    this.allPlayers = new ArrayListModel<Player>();
    this.allPlayers.addAll(this.sentry.getHomeTeam().getPlayers());
    this.allPlayerListModel = new PlayerListModel(this.allPlayers);

    this.teamForList = this.sentry.getHomeTeam();
  }

  public Action getAction(int action) {
    switch (action) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            if(!entry.getEvent().isSecondaryPlayer()){
              entry.getSecondaryPlayers().clear();
            }
            if(homeTeam){
              entry.setTeam(sentry.getHomeTeam());
            } else {
              entry.setTeam(sentry.getAwayTeam());
            }
            if (!newEntry)
              eventList.remove(oldEntry);
            eventList.add(EventDialogPModel.this.entry);
            EventDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            EventDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case ADD_PLAYER_ACTION:
        return new AbstractAction(null,
            ResourceManager.getIcon(Icon.LEFT_ICON)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            int min = allPlayerSelModel.getMinSelectionIndex();
            int max = allPlayerSelModel.getMaxSelectionIndex();
            ArrayListModel<Player> data = playerListModel.getData();
            for (int i = min; i <= max; i++) {
              if (allPlayerSelModel.isSelectedIndex(i)) {
                Player player = allPlayerListModel.getData().get(i);
                if (!data.contains(player))
                  data.add(player);
              }
            }
          }
        };
      case REMOVE_PLAYER_ACTION:
        return new AbstractAction(null,
            ResourceManager.getIcon(Icon.RIGHT_ICON)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            int min = playerSelModel.getMinSelectionIndex();
            int max = playerSelModel.getMaxSelectionIndex();
            LinkedList<Integer> indices = new LinkedList<Integer>();
            for (int i = min; i <= max; i++) {
              if (playerSelModel.isSelectedIndex(i))
                indices.add(0, i);
            }
            for (int i : indices) {
              playerListModel.getData().remove(i);
            }
          }
        };
      case ADD_ASSIST_ACTION:
        return new AbstractAction(null,
            ResourceManager.getIcon(Icon.LEFT_ICON)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            int min = allPlayerSelModel.getMinSelectionIndex();
            int max = allPlayerSelModel.getMaxSelectionIndex();
            ArrayListModel<Player> data = assistListModel.getData();
            for (int i = min; i <= max; i++) {
              if (allPlayerSelModel.isSelectedIndex(i)) {
                Player player = allPlayerListModel.getData().get(i);
                if (!data.contains(player))
                  data.add(player);
              }
            }
          }
        };
      case REMOVE_ASSIST_ACTION:
        return new AbstractAction(null,
            ResourceManager.getIcon(Icon.RIGHT_ICON)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            int min = assistSelModel.getMinSelectionIndex();
            int max = assistSelModel.getMaxSelectionIndex();
            ArrayList<Integer> indices = new ArrayList<Integer>();
            for (int i = min; i <= max; i++) {
              if (assistSelModel.isSelectedIndex(i))
                indices.add(0, i);
            }
            for (int i : indices) {
              assistListModel.getData().remove(i);
            }
          }
        };
    }
    return null;
  }

  public String getTeamName(boolean home) {
    if (home)
      return this.sentry.getHomeTeam().getName();
    return this.sentry.getAwayTeam().getName();
  }

  public ValueModel getValueModel(String property) {
    return new PropertyAdapter<GameEventEntry>(this.entry, property, true);
  }

  public SelectionInList<?> getSelectionInList(String property) {
    if (property == GameEvent.PROPERTY_TYPE) {
      ArrayListModel<GameEventType> eventTypes = new ArrayListModel<GameEventType>(
          Arrays.asList(GameEventType.values()));
      return new SelectionInList<ArrayListModel<GameEventType>>(eventTypes,
          new PropertyAdapter<GameEvent>(this.entry.getEvent(),
              GameEvent.PROPERTY_TYPE));
    }
    else if (property == GameEvent.PROPERTY_NAME) {
      return new SelectionInList<ArrayList<GameEvent>>(Organizer.getInstance()
          .getCurrentTournament().getGameEvents(),
          new PropertyAdapter<GameEventEntry>(this.entry,
              GameEventEntry.PROPERTY_GAMEEVENT));
    }
    else if (property == EventDialogPModel.PROPERTY_TEAMFORLIST) {
      ArrayListModel<Team> teams = new ArrayListModel<Team>();
      teams.add(this.sentry.getHomeTeam());
      teams.add(this.sentry.getAwayTeam());
      return new SelectionInList<ArrayListModel<Team>>(teams,
          new PropertyAdapter<EventDialogPModel>(this,
              EventDialogPModel.PROPERTY_TEAMFORLIST));
    }
    return null;
  }

  public ListModel<String> getListModel(int listModel) {
    switch (listModel) {
      case PLAYER_LIST:
        return playerListModel;
      case ASSIST_LIST:
        return assistListModel;
      case ALL_PLAYERS_LIST:
        return allPlayerListModel;
    }
    return null;
  }

  public Team getTeamForList() {
    return teamForList;
  }

  public void setTeamForList(Team teamForList) {
    this.teamForList = teamForList;
    this.allPlayers.clear();
    this.allPlayers.addAll(teamForList.getPlayers());
  }

  public ListSelectionModel getListSelectionModel(int list) {
    switch (list) {
      case PLAYER_LIST:
        return playerSelModel;
      case ASSIST_LIST:
        return assistSelModel;
      case ALL_PLAYERS_LIST:
        return allPlayerSelModel;
    }
    return null;
  }

  public boolean isSecondaryVisible() {
    return entry.getEvent().isSecondaryPlayer();
  }

  public String getSecondaryText() {
    return entry.getEvent().getSecondaryPlayerText();
  }

  public boolean isHomeTeam() {
    return homeTeam;
  }

  public void setHomeTeam(boolean homeTeam) {
    this.homeTeam = homeTeam;
  }
}
