package easytournament.basic.xml;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.valueholder.Player;
import easytournament.basic.valueholder.Staff;
import easytournament.basic.valueholder.Team;

public class TeamsXMLHandler {
  
  public static HashMap<String,Team> teammap;
  public static HashMap<String,Player> playermap;

  public static void save(Element xml) {
    easytournament.basic.valueholder.Tournament tourn = Organizer.getInstance()
        .getCurrentTournament();

    Element teams = new Element("teams");
    for (Team team : tourn.getTeams()) {
      Element teamEl = writeTeam(team, false);
      Element playersEl = writePlayers(team.getPlayers(), false);      
      teamEl.addContent(playersEl);
      
      Element staffEl = writeStaff(team.getStaff(), false);      
      teamEl.addContent(staffEl);
      
      teams.addContent(teamEl);
    }
    xml.addContent(teams);
  }
  
  public static void exportTeams(Element xml, ArrayList<Team> teamsToSave) {

    Element teams = new Element("teams");
    for (Team team : teamsToSave) {
      Element teamEl = writeTeam(team, true);
      Element playersEl = writePlayers(team.getPlayers(), true);      
      teamEl.addContent(playersEl);
      
      Element staffEl = writeStaff(team.getStaff(), true);      
      teamEl.addContent(staffEl);
      
      teams.addContent(teamEl);
    }
    xml.addContent(teams);
  }

  private static Element writeTeam(Team team, boolean export) {
    Element teamEl = new Element("team");    
    if(!export) {
      teamEl.setAttribute("id", team.getId() + "");
      teamEl.setAttribute("randomPos", team.getRandomPosition().toString());
    }
    teamEl.setAttribute("name", team.getName() == null ? "" : team.getName());
    teamEl.setAttribute("website", team.getWebsite() == null ? "" : team.getWebsite());
    teamEl.setAttribute("cprename", team.getContactPrename() == null ? "" : team.getContactPrename());
    teamEl.setAttribute("cname", team.getContactName() == null ? "" : team.getContactName());
    teamEl.setAttribute("address", team.getAddress() == null ? "" : team.getAddress());
    teamEl.setAttribute("email", team.getEmail() == null ? "" : team.getEmail());
    teamEl.setAttribute("phone", team.getPhone() == null ? "" : team.getPhone());
    teamEl.setAttribute("notes", team.getNotes() == null ? "" : team.getNotes());
    
    if(team.getLogo()!= null){
      Element logoEl = new Element("logo");         
      Image logo = team.getLogo().getImage();
      ImageEncoder.encodeImage(logoEl, logo);        
      teamEl.addContent(logoEl);        
    }
    return teamEl;
  }
  
  public static void exportPlayers(Element xml, ArrayList<Player> players){
    xml.addContent(writePlayers(players, true));
  }

  private static Element writePlayers(ArrayList<Player> players, boolean export) {
    Element playersEl = new Element("players");  
    for(Player p : players){
      Element playerEl = new Element("player");  
      if(!export)
        playerEl.setAttribute("id", p.getId()+"");
      PersonXMLHandler.writePerson(p, playerEl);      
      playerEl.setAttribute("number", p.getNumber() == null ? "" : p.getNumber());
      playerEl.setAttribute("position", p.getPosition() == null ? "" : p.getPosition());      
      playersEl.addContent(playerEl);
    }
    return playersEl;
  }
  
  public static void exportStaff(Element xml, ArrayList<Staff> staff){
    xml.addContent(writeStaff(staff, true));
  }

  public static Element writeStaff(ArrayList<Staff> staff, boolean export) {
    Element staffEl = new Element("staff"); 
    for(Staff s : staff){
      if(!export)
        staffEl.setAttribute("id", s.getId()+"");
      Element personEl = new Element("person"); 
      PersonXMLHandler.writePerson(s, personEl);
      personEl.setAttribute("function", s.getFunction() == null ? "" : s.getFunction());
      staffEl.addContent(personEl);
    }
    return staffEl;
  }

  public static void open(Element xml) {

    ArrayListModel<Team> teamlist = new ArrayListModel<Team>();
    teammap = new HashMap<String,Team>();
    playermap = new HashMap<String, Player>();
    
    Team.CURRENT_MAX_ID = 0;
    Player.CURRENT_MAX_ID = 0;
    Staff.CURRENT_MAX_ID = 0;
    
    Element teamsElement = xml.getChild("teams");
    List<Element> teams = teamsElement.getChildren("team");
    for (Element teamEl : teams) {
      Team tm = readTeam(teamEl, false);      
      teamlist.add(tm);
      teammap.put(teamEl.getAttributeValue("id"), tm);
    }
    Organizer.getInstance().getCurrentTournament().setTeams(teamlist);
  }
  
  public static ArrayListModel<Team> importTeams(Element xml) {

    ArrayListModel<Team> teamlist = new ArrayListModel<Team>();
    
    Element teamsElement = xml.getChild("teams");
    List<Element> teams = teamsElement.getChildren("team");
    for (Element teamEl : teams) {   
      teamlist.add(readTeam(teamEl, true));
    }
    return teamlist;
  }

  private static Team readTeam(Element teamEl, boolean importing) {
    Team tm = importing?new Team(0):new Team();
    tm.setName(teamEl.getAttributeValue("name"));
    tm.setWebsite(teamEl.getAttributeValue("website"));
    if(!importing)
      tm.setRandomPosition(Integer.valueOf(teamEl.getAttributeValue("randomPos")));
    tm.setContactName(teamEl.getAttributeValue("cname"));
    tm.setContactPrename(teamEl.getAttributeValue("cprename"));
    tm.setAddress(teamEl.getAttributeValue("address"));
    tm.setEmail(teamEl.getAttributeValue("email"));
    tm.setPhone(teamEl.getAttributeValue("phone"));
    tm.setNotes(teamEl.getAttributeValue("notes"));
    Element logoEl = teamEl.getChild("logo");
    if(logoEl != null){
      tm.setLogo(ImageEncoder.decodeImage(logoEl));
    }
    
    tm.setPlayers(readPlayers(teamEl, importing));      
    tm.setStaff(readStaff(teamEl, importing));
    return tm;
  }

  public static ArrayListModel<Player> readPlayers(Element teamEl, boolean importing) {
    Element playersEl =  teamEl.getChild("players");
    List<Element> playerEls = playersEl.getChildren("player");
    ArrayListModel<Player> players = new ArrayListModel<Player>();
    for(Element pEl : playerEls){
      Player p = importing?new Player(0):new Player();
      p.setNumber(pEl.getAttributeValue("number"));
      p.setPosition(pEl.getAttributeValue("position"));
      PersonXMLHandler.readPerson(pEl, p);
      players.add(p);
      if(!importing)
        playermap.put(pEl.getAttributeValue("id"), p);
    }
    return players;
  }

  public static ArrayListModel<Staff> readStaff(Element teamEl, boolean importing) {
    Element staffEl =  teamEl.getChild("staff");
    List<Element> personEls = staffEl.getChildren("person");
    ArrayListModel<Staff> staff = new ArrayListModel<Staff>();
    for(Element pEl : personEls){
      Staff p = importing?new Staff(0):new Staff();
      p.setFunction(pEl.getAttributeValue("function"));
      PersonXMLHandler.readPerson(pEl, p);
      staff.add(p);
    }
    return staff;
  }

}
