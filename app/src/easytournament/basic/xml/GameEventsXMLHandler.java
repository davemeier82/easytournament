package easytournament.basic.xml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.jdom.Element;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.gameevent.GameEventType;
import easytournament.basic.valueholder.GameEvent;
import easytournament.basic.valueholder.Sport;

public class GameEventsXMLHandler {
  
  public static HashMap<String, GameEvent> eventMap;

  public static void saveGameEvents(Element sportEL, ArrayListModel<GameEvent> events) {
    Element eventsEl = new Element("gameevents");
    
    for(GameEvent e : events){
      Element eventEl = new Element("gameevent");
      eventEl.setAttribute("type", e.getType().name());      
      eventEl.setAttribute("name", e.getName());
      eventEl.setAttribute("id", e.getId()+"");
      if(e.isSecondaryPlayer())
        eventEl.setAttribute("secondary", e.getSecondaryPlayerText());
      if(e.getSportId() != null){        
        eventEl.setAttribute("sportid", e.getSportId());
        eventEl.setAttribute("nameResourceId", e.getNameResourceId());
        if(e.getIconResourceId() != null)
          eventEl.setAttribute("iconResourceId", e.getIconResourceId());
        if(e.isSecondaryPlayer()){
          eventEl.setAttribute("secondaryResourceId", e.getSecondResourceId());
        }
      }      
      eventEl.setAttribute("pointsTeam", e.getPointsForTeam()+"");
      eventEl.setAttribute("pointsOpponent", e.getPointsForOpponent()+"");
      
      eventsEl.addContent(eventEl);
    }
    sportEL.addContent(eventsEl);
  }

  /**
   * Reads GameEvents from sport.xml
   * @param sportEl
   * @param txtbundle
   * @param imgbundle
   * @param path
   * @return
   */
  public static ArrayListModel<GameEvent> readGameEvent(Element sportEl,
      ResourceBundle txtbundle, ResourceBundle imgbundle, File path) {
    ArrayListModel<GameEvent> events = new ArrayListModel<GameEvent>();
    Element gameEventsEl = sportEl.getChild("gameevents");
    List<Element> gameEventEls = gameEventsEl.getChildren("gameevent");
    String sportId = sportEl.getAttributeValue("id");
    for (Element e : gameEventEls) {
      GameEvent event = new GameEvent(0);
      event.setSportId(sportId);
      event.setType(GameEventType.valueOf(e.getAttributeValue("type")));
      String name = e.getAttributeValue("name");
      if (name != null)
        event.setName(name);
      String nameResId = e.getAttributeValue("nameResourceId");
      if (nameResId != null && txtbundle != null) {
        event.setNameResourceId(nameResId);
        event.setName(txtbundle.getString(nameResId));
      }
      String secondResId = e.getAttributeValue("secondaryResourceId");
      if (secondResId != null && txtbundle != null) {
        event.setSecondaryPlayer(true);
        event.setSecondResourceId(secondResId);
        event.setSecondaryPlayerText(txtbundle.getString(secondResId));
      }

      String imgResId = e.getAttributeValue("iconResourceId");
      if (imgResId != null && imgbundle != null) {
        event.setIconResourceId(imgResId);
        File icon = new File(path, imgbundle.getString(imgResId));
        event.setIcon(new ImageIcon(icon.getAbsolutePath()));
      }

      event
          .setPointsForTeam(Integer.parseInt(e.getAttributeValue("pointsTeam")));
      event.setPointsForOpponent(Integer.parseInt(e
          .getAttributeValue("pointsOpponent")));
      events.add(event);
    }

    return events;
  }

  /**
   * Reads GameEvents form tournament-file
   * @param sportEl
   * @return
   */
  public static ArrayListModel<GameEvent> readGameEvent(Element sportEl, boolean importing) {
    if(!importing) {
      eventMap = new HashMap<String, GameEvent>();
      GameEvent.CURRENT_MAX_ID = 0;
    }
    ArrayListModel<GameEvent> events = new ArrayListModel<GameEvent>();
    Element gameEventsEl = sportEl.getChild("gameevents");
    List<Element> gameEventEls = gameEventsEl.getChildren("gameevent");

    for (Element e : gameEventEls) {
      GameEvent event;
      if(importing)
        event = new GameEvent(0);
      else
        event = new GameEvent();
      
      String sportId = e.getAttributeValue("sportid");
      ResourceBundle txtbundle = null;
      ResourceBundle imgbundle = null;
      Sport s = null;

      if (sportId != null) {
        event.setSportId(sportId);
        s = Organizer.getInstance().getSports().get(sportId);
        if (s != null) {
          txtbundle = s.getTextResourceBundle();
          imgbundle = s.getIconResourceBundle();
        }
      }

      event.setType(GameEventType.valueOf(e.getAttributeValue("type")));

      String name = e.getAttributeValue("name");
      if (name != null)
        event.setName(name);
      String txtResId = e.getAttributeValue("nameResourceId");
      if (txtResId != null && txtbundle != null) {
        event.setNameResourceId(txtResId);
        event.setName(txtbundle.getString(txtResId));
      }
      
      String secondary = e.getAttributeValue("secondary");
      if (secondary != null) {
        event.setSecondaryPlayer(true);
        event.setSecondaryPlayerText(secondary);
      }
      String secondResId = e.getAttributeValue("secondaryResourceId");
      if (secondResId != null && txtbundle != null) {
        event.setSecondResourceId(secondResId);
        event.setSecondaryPlayerText(txtbundle.getString(secondResId));
      }
      
      String imgResId = e.getAttributeValue("iconResourceId");
      if (imgResId != null && imgbundle != null) {
        event.setIconResourceId(imgResId);
        File icon = new File(s.getPath(), imgbundle.getString(imgResId));
        event.setIcon(new ImageIcon(icon.getAbsolutePath()));
      }

      event
          .setPointsForTeam(Integer.parseInt(e.getAttributeValue("pointsTeam")));
      event.setPointsForOpponent(Integer.parseInt(e
          .getAttributeValue("pointsOpponent")));
      events.add(event);
      if(!importing) {
        eventMap.put(e.getAttributeValue("id"), event);
      }
    }
    return events;
  }
}
