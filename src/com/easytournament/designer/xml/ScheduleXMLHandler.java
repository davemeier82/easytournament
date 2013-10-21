package com.easytournament.designer.xml;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Element;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEvent;
import com.easytournament.basic.valueholder.GameEventEntry;
import com.easytournament.basic.valueholder.Player;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.basic.xml.GameEventsXMLHandler;
import com.easytournament.basic.xml.RefreeXMLHandler;
import com.easytournament.basic.xml.TeamsXMLHandler;
import com.easytournament.designer.valueholder.Position;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.jgoodies.common.collect.ArrayListModel;


public class ScheduleXMLHandler {

  public static void save(Element xml) {

    com.easytournament.basic.valueholder.Tournament tourn = Organizer.getInstance()
        .getCurrentTournament();

    Element s = new Element("schedule");

    for (ScheduleEntry se : tourn.getSchedules()) {
      Element schedule = new Element("sheduleentry");
      schedule.setAttribute("home", se.getHomePos().getId() + "");
      schedule.setAttribute("away", se.getAwayPos().getId() + "");
      schedule.setAttribute("place", se.getPlace());
      schedule.setAttribute("date", se.getDate().getTime().getTime() + "");
      schedule.setAttribute("played", se.isGamePlayed()? "1" : "0");

      Element refrees = new Element("refrees");
      for (Refree r : se.getReferees()) {
        Element refEl = new Element("refree");
        refEl.setAttribute("id", r.getId() + "");
        refrees.addContent(refEl);
      }
      schedule.addContent(refrees);

      if (se.isGamePlayed()) {
        Element resultsEl = new Element("results");
        ArrayList<Integer> results = se.getResults();
        ArrayList<Boolean> checked = se.getChecked();
        for (int i = 0; i < results.size(); i++) {
          Element result = new Element("result");
          result.setAttribute("checked", checked.get(i)?"1":"0");
          result.setText(results.get(i).toString());
          resultsEl.addContent(result);
        }
        schedule.addContent(resultsEl);
        writeGameEventEntries(se.getGameEvents(), schedule);
      }
      s.addContent(schedule);
    }
    xml.addContent(s);
  }

  public static void writeGameEventEntries(
      ArrayListModel<GameEventEntry> entries, Element scheduleEl) {
    Element gEventEntries = new Element("gameevententries");
    for (GameEventEntry e : entries) {
      if(e.getEvent() != null && e.getTeam() != null)
      {
        Element gEvnetEntryEl = new Element("gameevententry");
        gEvnetEntryEl.setAttribute("gameeventid", e.getEvent().getId() + "");
        gEvnetEntryEl.setAttribute("teamid", e.getTeam().getId() + "");
        gEvnetEntryEl.setAttribute("timemin", e.getTimeMin() + "");
        gEvnetEntryEl.setAttribute("timesec", e.getTimeSec() + "");
        Element mainPlayersEl = new Element("mainplayer");
        for (Player p : e.getMainPlayers()) {
          Element playerEl = new Element("player");
          playerEl.setAttribute("id", p.getId() + "");
          mainPlayersEl.addContent(playerEl);
        }
        gEvnetEntryEl.addContent(mainPlayersEl);
        Element secPlayersEl = new Element("secondaryplayer");
        for (Player p : e.getSecondaryPlayers()) {
          Element playerEl = new Element("player");
          playerEl.setAttribute("id", p.getId() + "");
          secPlayersEl.addContent(playerEl);
        }
        gEvnetEntryEl.addContent(secPlayersEl);

        gEventEntries.addContent(gEvnetEntryEl);
      }      
    }
    scheduleEl.addContent(gEventEntries);
  }

  public static void open(Element xml) {

    Element schedElement = xml.getChild("schedule");
    HashMap<String,Position> posMap = DesignerXMLHandler.positionMap;
    HashMap<String,Refree> refMap = RefreeXMLHandler.refmap;

    ArrayListModel<ScheduleEntry> shedules = new ArrayListModel<ScheduleEntry>();

    for (Object o : schedElement.getChildren("sheduleentry")) {
      Element sEntryElement = (Element)o;
      String home = sEntryElement.getAttributeValue("home");
      String away = sEntryElement.getAttributeValue("away");
      ScheduleEntry se = new ScheduleEntry(posMap.get(home), posMap.get(away));
      se.setPlace(sEntryElement.getAttributeValue("place"));
      try {
        Calendar cal = new GregorianCalendar(ResourceManager.getLocale());
        cal.setTime(new Date(sEntryElement.getAttribute("date").getLongValue()));
        se.setDate(cal);
      }
      catch (DataConversionException e) {
        ErrorLogger.getLogger().throwing("ScheduleXMLHandler", "open", e);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
      boolean played = sEntryElement.getAttributeValue("played").equals("1");
      se.setGamePlayed(played);

      Element refsEl = sEntryElement.getChild("refrees");
      List<Element> refEls = refsEl.getChildren("refree");
      for (Element ref : refEls) {
        Refree r = refMap.get(ref.getAttributeValue("id"));
        if (r != null)
          se.getReferees().add(r);
      }
      if (played) {
        Element resultsEl = sEntryElement.getChild("results");
        List<Element> resEls = resultsEl.getChildren("result");
        for(int i = 0; i < resEls.size();)
        {
          Element resultH = resEls.get(i++);
          Element resultA = resEls.get(i++);
          String checkedH = resultH.getAttributeValue("checked");
          String checkedA = resultA.getAttributeValue("checked");
          if(checkedH == null || checkedA == null)
          {
            int scoreH = Integer.valueOf(resultH.getText());
            int scoreA = Integer.valueOf(resultA.getText());
            boolean checked = scoreH != 0 || scoreA!= 0;
            se.getChecked().add(checked); 
            se.getChecked().add(checked);            
            se.getResults().add(scoreH);
            se.getResults().add(scoreA);
          }
          else
          {
            se.getChecked().add(checkedH.equals("1")); 
            se.getChecked().add(checkedA.equals("1")); 
            se.getResults().add(Integer.valueOf(resultH.getText()));
            se.getResults().add(Integer.valueOf(resultA.getText()));
          }
        }

        ArrayListModel<GameEventEntry> gentries = readGameEventEntries(sEntryElement);
        se.setGameEvents(gentries);
        if (gentries.size() > 0) {
          GameEventEntry last = gentries.get(gentries.size() - 1);
          ArrayList<Integer> results = se.getResults();
          last.setSummedHomePoints(results.get(results.size() - 2));
          last.setSummedAwayPoints(results.get(results.size() - 1));
        }
      }

      se.getGroupAssignedTo().getSchedules().add(se);
      shedules.add(se);
    }

    Organizer.getInstance().getCurrentTournament().setSchedules(shedules);
  }

  private static ArrayListModel<GameEventEntry> readGameEventEntries(
      Element seElement) {
    HashMap<String,GameEvent> eventMap = GameEventsXMLHandler.eventMap;
    HashMap<String,Team> teamMap = TeamsXMLHandler.teammap;
    HashMap<String,Player> playerMap = TeamsXMLHandler.playermap;

    ArrayListModel<GameEventEntry> gentries = new ArrayListModel<GameEventEntry>();
    Element gEventsEl = seElement.getChild("gameevententries");
    List<Element> gEventEls = gEventsEl.getChildren("gameevententry");
    for (Element e : gEventEls) {      
      GameEvent gameEvent = eventMap.get(e.getAttributeValue("gameeventid"));
      Team team = teamMap.get(e.getAttributeValue("teamid"));
      if(gameEvent != null && team != null)
      {
        GameEventEntry gee = new GameEventEntry();
        gee.setEvent(gameEvent);
        gee.setTeam(team);
        gee.setTimeMin(Integer.parseInt(e.getAttributeValue("timemin")));
        gee.setTimeSec(Integer.parseInt(e.getAttributeValue("timesec")));
        Element mainPlayersEl = e.getChild("mainplayer");
        List<Element> mainplayers = mainPlayersEl.getChildren("player");
        for (Element mp : mainplayers) {
          Player p = playerMap.get(mp.getAttributeValue("id"));
          if (p != null)
            gee.getMainPlayers().add(p);
        }
        Element secPlayersEl = e.getChild("secondaryplayer");
        List<Element> secplayers = secPlayersEl.getChildren("player");
        for (Element sp : secplayers) {
          Player p = playerMap.get(sp.getAttributeValue("id"));
          if (p != null)
            gee.getSecondaryPlayers().add(p);
        }
        gentries.add(gee);
      } else {
        ErrorLogger.getLogger().severe("ScheduleXMLHandler.readGameEventEntries(): gameEvent or team are null!");
      }
    }

    return gentries;
  }
}
