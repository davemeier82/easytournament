package com.easytournament.basic.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.BundleControl;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Sport;

public class SportXMLHandler {

  public static void saveTournamentSport(Element tournamentEL, Sport s) {
    Element sportEl = new Element("sport");
    sportEl.setAttribute("id", s.getId() == null? "" : s.getId());
    sportEl.setAttribute("name", s.getName() == null? "" : s.getName());
    sportEl.setAttribute("edited", s.isEdited()? "1" : "0");

    RulesXMLHandler.saveTSettings(sportEl, s.getSettings());
    RulesXMLHandler.saveRules(sportEl, s.getRules());
    GameEventsXMLHandler.saveGameEvents(sportEl, s.getGameEvents());

    tournamentEL.addContent(sportEl);
  }

  public static Sport readSport(Element tournamentEL, File path) {
    Element sportEl = tournamentEL.getChild("sport");
    Sport s = new Sport();
    s.setId(sportEl.getAttributeValue("id"));
    ResourceBundle txtbundle = null;
    ResourceBundle iconbundle = null;
    txtbundle = ResourceBundle.getBundle(sportEl.getAttributeValue("textrb"),
        ResourceManager.getLocale(), new BundleControl(path));
    s.setTextResourceBundle(txtbundle);
    iconbundle = ResourceBundle.getBundle(sportEl.getAttributeValue("iconrb"),
        ResourceManager.getLocale(), new BundleControl(path));
    s.setIconResourceBundle(iconbundle);
    s.setPath(path);
    s.setSettings(RulesXMLHandler.readSettings(sportEl));
    s.setRules(RulesXMLHandler.readRules(sportEl));
    s.setGameEvents(GameEventsXMLHandler.readGameEvent(sportEl, txtbundle,
        iconbundle, path));

    return s;
  }

  public static Sport readTorunamentSport(Element tournamentEL) {
    Element sportEl = tournamentEL.getChild("sport");
    Sport s = Organizer.getInstance().getSports()
        .get(sportEl.getAttributeValue("id"));
    boolean edited = sportEl.getAttributeValue("edited").equals("1");
    if (s == null) {
      s = new Sport();
      s.setName(sportEl.getAttributeValue("name"));
    }
    else if (edited) {
      try {
        s = (Sport)s.clone();
      }
      catch (CloneNotSupportedException e) {
        ErrorLogger.getLogger().throwing("SportXMLHandler",
            "readTorunamentSport", e);
        e.printStackTrace();
      }
    }

    s.setId(sportEl.getAttributeValue("id"));
    s.setEdited(edited);

    // always take the settings of the file
    s.setSettings(RulesXMLHandler.readSettings(sportEl));
    s.setRules(RulesXMLHandler.readRules(sportEl));
    s.setGameEvents(GameEventsXMLHandler.readGameEvent(sportEl, false));

    return s;
  }

  public static Sport readSportXML(File xmlfile, File sportpath) {
    Document doc = null;

    try {
      doc = new SAXBuilder().build(xmlfile); // TODO validate application and
                                             // type
      Element root = doc.getRootElement();
      return readSport(root, sportpath);
    }
    catch (JDOMException e) {
      ErrorLogger.getLogger().throwing("SportXMLHandler", "readSportXML", e);
    }
    catch (IOException e) {
      ErrorLogger.getLogger().throwing("SportXMLHandler", "readSportXML", e);
    }
    return null;
  }

  public static HashMap<String,Sport> readSports() {
    HashMap<String,Sport> sports = new HashMap<String,Sport>();
    File currentDir = new File("sports");
    String[] fileNames = currentDir.list();
    for (String s : fileNames) {
      File dir = new File(currentDir, s);
      if (dir.isDirectory()) {
        try {
          File xml = new File(dir.getPath(), "sport.xml");
          if (xml.isFile()) {
            Sport sport = readSportXML(xml, dir.getAbsoluteFile());
            if (sport != null && !sport.getId().equals("")) {
              sports.put(sport.getId(), sport);
            }
          }
        }
        catch (Exception e) {
          ErrorLogger.getLogger().throwing("SportXMLHandler", "readSports", e);
          ErrorDialog ed = new ErrorDialog(Organizer.getInstance()
              .getMainFrame(), ResourceManager.getText(Text.ERROR),
              e.toString(), e);
          ed.setVisible(true);
          e.printStackTrace();
        }
      }
    }
    return sports;
  }
}
