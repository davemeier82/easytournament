package easytournament.basic.xml;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;

import org.jdom.DataConversionException;
import org.jdom.Element;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;


public class TournamentXMLHandler {

  public static void save(Element xml) {

    easytournament.basic.valueholder.Tournament tourn = Organizer.getInstance()
        .getCurrentTournament();

    Element tournament = new Element("tournament");
    tournament.setAttribute("name", tourn.getName());
    tournament.setAttribute("website", tourn.getWebsite());
    tournament.setAttribute("description", tourn.getDescription());
    tournament.setAttribute("location", tourn.getLocation());
    tournament.setAttribute("firstname", tourn.getFirstname());
    tournament.setAttribute("lastname", tourn.getLastname());
    tournament.setAttribute("address", tourn.getAddress());
    tournament.setAttribute("phone", tourn.getPhone());
    tournament.setAttribute("email", tourn.getEmail());
    tournament.setAttribute("notes", tourn.getNotes());
    tournament.setAttribute("begin", tourn.getBegin().getTimeInMillis()+"");
    tournament.setAttribute("end", tourn.getEnd().getTimeInMillis()+"");
    ImageIcon image = tourn.getLogo();
    if(image != null) {
      Element logoEl = new Element("logo");
      ImageEncoder.encodeImage(logoEl, image.getImage());
      tournament.addContent(logoEl);
    }
    
    SportXMLHandler.saveTournamentSport(tournament, tourn.getSport());
    xml.addContent(tournament);
  }

  public static void open(Element xml) {
    Element tournament = xml.getChild("tournament");
    easytournament.basic.valueholder.Tournament t = Organizer.getInstance()
        .getCurrentTournament();
    t.setName(tournament.getAttributeValue("name"));
    t.setWebsite(tournament.getAttributeValue("website"));
    t.setDescription(tournament.getAttributeValue("description"));
    t.setSport(SportXMLHandler.readTorunamentSport(tournament));
    t.setLocation(tournament.getAttributeValue("location"));
    t.setFirstname(tournament.getAttributeValue("firstname"));
    t.setLastname(tournament.getAttributeValue("lastname"));
    t.setAddress(tournament.getAttributeValue("address"));
    t.setPhone(tournament.getAttributeValue("phone"));
    t.setEmail(tournament.getAttributeValue("email"));
    t.setNotes(tournament.getAttributeValue("notes"));
    try {
      Calendar cal = new GregorianCalendar(ResourceManager.getLocale());
      cal.setTime(new Date(tournament.getAttribute("begin").getLongValue()));
      t.setBegin(cal);
    }
    catch (DataConversionException e) {
      ErrorLogger.getLogger().throwing("TournamentXMLHandler", "open", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();   
    }
    try {
      Calendar cal = new GregorianCalendar(ResourceManager.getLocale());
      cal.setTime(new Date(tournament.getAttribute("end").getLongValue()));
      t.setEnd(cal);
    }
    catch (DataConversionException e) {
      ErrorLogger.getLogger().throwing("TournamentXMLHandler", "open", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();   
    }
    Element imgEl = tournament.getChild("logo");
    if(imgEl != null)
      t.setLogo(ImageEncoder.decodeImage(imgEl));
  }

}
