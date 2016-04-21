package easytournament.basic.valueholder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.resources.ResourceManager;
import easytournament.designer.valueholder.ScheduleEntry;
import easytournament.designer.valueholder.TournamentPlan;


public class Tournament extends Model implements PropertyChangeListener, ListDataListener {

  private static final long serialVersionUID = -7705455729268412011L;
  public static final String PROPERTY_PLAN = "plan";
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_DESC = "description";
  public static final String PROPERTY_LOC = "location";
  public static final String PROPERTY_FNAME = "firstname";
  public static final String PROPERTY_LNAME = "lastname";
  public static final String PROPERTY_ADDRESS = "address";
  public static final String PROPERTY_PHONE = "phone";
  public static final String PROPERTY_EMAIL = "email";
  public static final String PROPERTY_NOTES = "notes";
  public static final String PROPERTY_BEGIN = "begin";
  public static final String PROPERTY_END = "end";
  public static final String PROPERTY_LOGO = "logo";
  public static final String PROPERTY_SPORT = "sport";
  public static final String PROPERTY_TEAMS = "teams";
  public static final String PROPERTY_UNASS_TEAMS = "unassignedteams";
  public static final String PROPERTY_REFREES = "refrees";
  public static final String PROPERTY_SCHEDULES = "schedules";
  public static final String PROPERTY_WEBSITE = "website"; 
  
  private String name;
  private String description;
  private String location;
  private String firstname;
  private String lastname;
  private String address;
  private String phone;
  private String email;
  private String notes;
  private Calendar begin;
  private Calendar end;
  private ImageIcon logo;
  protected String website;
  private ArrayListModel<Team> teams = new ArrayListModel<Team>();
  private ArrayListModel<Team> unassignedteams = new ArrayListModel<Team>();
  private ArrayListModel<Refree> refrees = new ArrayListModel<Refree>();
  private ArrayListModel<ScheduleEntry> schedules = new ArrayListModel<ScheduleEntry>();
  private Sport sport = new Sport();
  private TournamentPlan plan; //TODO reference to designer not good

  public Tournament() {
    reset();
    this.teams.addListDataListener(this);
    this.unassignedteams.addListDataListener(this);
    this.refrees.addListDataListener(this);
    this.schedules.addListDataListener(this);    
    this.sport.addPropertyChangeListener(this);
  }
  
  public void reset(){
    setName("");
    setWebsite("");
    setDescription("");
    setLocation("");
    setFirstname("");
    setLastname("");
    setAddress("");
    setPhone("");
    setEmail("");
    setNotes("");
    setBegin(new GregorianCalendar(ResourceManager.getLocale()));
    setEnd(new GregorianCalendar(ResourceManager.getLocale()));
    setLogo(null);
    this.teams.clear();
    this.unassignedteams.clear();
    this.refrees.clear();
    this.schedules.clear();
    this.sport.reset();
    
    setPlan(new easytournament.designer.valueholder.TournamentPlan()); //TODO reference in designer not good
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    String old = this.description;
    this.description = description;
    this.firePropertyChange(PROPERTY_DESC, old, this.description);
  }

  public ImageIcon getLogo() {
    return this.logo;
  }

  public void setLogo(ImageIcon logo) {
    ImageIcon old = this.logo;
    this.logo = logo;
    this.firePropertyChange(PROPERTY_LOGO, old, this.logo);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }

  public ArrayListModel<Team> getTeams() {
    return this.teams;
  }

  public void setTeams(ArrayListModel<Team> teams) {
    this.teams.clear();
    this.teams.addAll(teams);
  }

  public TournamentPlan getPlan() {
    return this.plan;
  }

  public void setPlan(TournamentPlan plan) {
    if(this.plan != null)
      this.plan.removePropertyChangeListener(this);
    TournamentPlan old = this.plan;
    this.plan = plan;
    if(this.plan != null)
      this.plan.addPropertyChangeListener(this);
    this.firePropertyChange(PROPERTY_PLAN, old, plan);
  }

  public ArrayListModel<Refree> getRefrees() {
    return this.refrees;
  }

  public void setRefrees(ArrayListModel<Refree> refees) {
    this.refrees.clear();
    this.refrees.addAll(refees);
  }

  public ArrayListModel<Team> getUnassignedteams() {
    return this.unassignedteams;
  }

  public void setUnassignedteams(ArrayListModel<Team> unassignedteams) {
    this.unassignedteams = unassignedteams;
  }

  public ArrayListModel<ScheduleEntry> getSchedules() {
    return this.schedules;
  }

  public void setSchedules(ArrayListModel<ScheduleEntry> schedules) {
    this.schedules.clear();
    this.schedules.addAll(schedules);
  }

  public SportSettings getSettings() {
    return this.sport.getSettings();
  }

  public void setSettings(SportSettings settings) {
    this.sport.getSettings().setSportSettings(settings);
  }

  public ArrayListModel<Rule> getDefaultRules() {
    return this.sport.getRules();
  }

  public void setDefaultRules(ArrayListModel<Rule> defaultRules) {
    ArrayListModel<Rule> rules = this.sport.getRules();
    rules.clear();
    rules.addAll(defaultRules);
  }

  public ArrayListModel<GameEvent> getGameEvents() {
    return this.sport.getGameEvents();
  }

  public void setGameEvents(ArrayListModel<GameEvent> gameEvents) {
    ArrayListModel<GameEvent> events = this.getGameEvents();
    events.clear();
    events.addAll(gameEvents);
  }

  public Sport getSport() {
    return this.sport;
  }

  public void setSport(Sport sport) {
    this.sport.setSport(sport);
    this.firePropertyChange(PROPERTY_SPORT, null, this.sport);
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    String old = this.location;
    this.location = location;
    this.firePropertyChange(PROPERTY_LOC, old, this.location);
  }

  public String getFirstname() {
    return this.firstname;
  }

  public void setFirstname(String firstname) {
    String old = this.firstname;
    this.firstname = firstname;
    this.firePropertyChange(PROPERTY_FNAME, old, this.firstname);
  }

  public String getLastname() {
    return this.lastname;
  }

  public void setLastname(String lastname) {
    String old = this.lastname;
    this.lastname = lastname;
    this.firePropertyChange(PROPERTY_LNAME, old, this.lastname);
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    String old = this.address;
    this.address = address;
    this.firePropertyChange(PROPERTY_ADDRESS, old, this.address);
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    String old = this.phone;
    this.phone = phone;
    this.firePropertyChange(PROPERTY_PHONE, old, this.phone);
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    String old = this.email;
    this.email = email;
    this.firePropertyChange(PROPERTY_EMAIL, old, this.email);
  }

  public String getNotes() {
    return this.notes;
  }

  public void setNotes(String notes) {
    String old = this.notes;
    this.notes = notes;
    this.firePropertyChange(PROPERTY_NOTES, old, this.notes);
  }

  public Calendar getBegin() {
    return this.begin;
  }

  public void setBegin(Calendar begin) {
    Calendar old = this.begin;
    this.begin = begin;
    this.firePropertyChange(PROPERTY_BEGIN, old, this.begin);
  }
  
  public void setBeginTime(Calendar time){
    this.begin.set(Calendar.HOUR, time.get(Calendar.HOUR));
    this.begin.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
    this.begin.set(Calendar.SECOND, time.get(Calendar.SECOND));
    this.begin.set(Calendar.MILLISECOND, time.get(Calendar.MILLISECOND));
    this.begin.set(Calendar.AM_PM, time.get(Calendar.AM_PM));
    this.firePropertyChange(PROPERTY_BEGIN, null, this.begin);
  }

  public Calendar getEnd() {
    return this.end;
  }

  public void setEnd(Calendar end) {
    Calendar old = this.end;
    this.end = end;
    this.firePropertyChange(PROPERTY_END, old, this.end);
  }
  
  public void setEndTime(Calendar time){
    this.end.set(Calendar.HOUR, time.get(Calendar.HOUR));
    this.end.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
    this.end.set(Calendar.SECOND, time.get(Calendar.SECOND));
    this.end.set(Calendar.MILLISECOND, time.get(Calendar.MILLISECOND));
    this.end.set(Calendar.AM_PM, time.get(Calendar.AM_PM));
    this.firePropertyChange(PROPERTY_END, null, this.end);
  }
  
  public String getWebsite() {
    return this.website;
  }

  public void setWebsite(String website) {
    String old = this.website;
    this.website = website;
    this.firePropertyChange(PROPERTY_WEBSITE, old, this.website);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getSource() instanceof Sport)
      this.firePropertyChange(PROPERTY_SPORT, null, this.sport);
    else if(evt.getSource() instanceof TournamentPlan){
      this.firePropertyChange(PROPERTY_PLAN, null, this.plan);
    }
    
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    if(e.getSource() == this.teams){
      this.firePropertyChange(PROPERTY_TEAMS, null, this.teams);
    } else if(e.getSource() == this.schedules){
      this.firePropertyChange(PROPERTY_SCHEDULES, null, this.schedules);
    } else if(e.getSource() == this.schedules){
      this.firePropertyChange(PROPERTY_UNASS_TEAMS, null, this.unassignedteams);
    } else if(e.getSource() == this.refrees) {
      this.firePropertyChange(PROPERTY_REFREES, null, this.refrees);
    }
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    if(e.getSource() == this.teams){
      this.firePropertyChange(PROPERTY_TEAMS, null, this.teams);
    } else if(e.getSource() == this.schedules){
      this.firePropertyChange(PROPERTY_SCHEDULES, null, this.schedules);
    } else if(e.getSource() == this.schedules){
      this.firePropertyChange(PROPERTY_UNASS_TEAMS, null, this.unassignedteams);
    } else if(e.getSource() == this.refrees) {
      this.firePropertyChange(PROPERTY_REFREES, null, this.refrees);
    }
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    if(e.getSource() == this.teams){
      this.firePropertyChange(PROPERTY_TEAMS, null, this.teams);
    } else if(e.getSource() == this.schedules){
      this.firePropertyChange(PROPERTY_SCHEDULES, null, this.schedules);
    } else if(e.getSource() == this.schedules){
      this.firePropertyChange(PROPERTY_UNASS_TEAMS, null, this.unassignedteams);
    } else if(e.getSource() == this.refrees) {
      this.firePropertyChange(PROPERTY_REFREES, null, this.refrees);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.address == null)? 0 : this.address.hashCode());
    result = prime * result + ((this.begin == null)? 0 : this.begin.hashCode());
    result = prime * result
        + ((this.description == null)? 0 : this.description.hashCode());
    result = prime * result + ((this.email == null)? 0 : this.email.hashCode());
    result = prime * result + ((this.end == null)? 0 : this.end.hashCode());
    result = prime * result + ((this.firstname == null)? 0 : this.firstname.hashCode());
    result = prime * result + ((this.lastname == null)? 0 : this.lastname.hashCode());
    result = prime * result + ((this.location == null)? 0 : this.location.hashCode());
    result = prime * result + ((this.logo == null)? 0 : this.logo.hashCode());
    result = prime * result + ((this.name == null)? 0 : this.name.hashCode());
    result = prime * result + ((this.notes == null)? 0 : this.notes.hashCode());
    result = prime * result + ((this.phone == null)? 0 : this.phone.hashCode());
    result = prime * result + ((this.plan == null)? 0 : this.plan.hashCode());
    result = prime * result + ((this.refrees == null)? 0 : this.refrees.hashCode());
    result = prime * result + ((this.schedules == null)? 0 : this.schedules.hashCode());
    result = prime * result + ((this.sport == null)? 0 : this.sport.hashCode());
    result = prime * result + ((this.teams == null)? 0 : this.teams.hashCode());
    result = prime * result
        + ((this.unassignedteams == null)? 0 : this.unassignedteams.hashCode());
    result = prime * result + ((this.website == null)? 0 : this.website.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Tournament other = (Tournament)obj;
    if (this.address == null) {
      if (other.address != null)
        return false;
    }
    else if (!this.address.equals(other.address))
      return false;
    if (this.begin == null) {
      if (other.begin != null)
        return false;
    }
    else if (!this.begin.equals(other.begin))
      return false;
    if (this.description == null) {
      if (other.description != null)
        return false;
    }
    else if (!this.description.equals(other.description))
      return false;
    if (this.email == null) {
      if (other.email != null)
        return false;
    }
    else if (!this.email.equals(other.email))
      return false;
    if (this.end == null) {
      if (other.end != null)
        return false;
    }
    else if (!this.end.equals(other.end))
      return false;
    if (this.firstname == null) {
      if (other.firstname != null)
        return false;
    }
    else if (!this.firstname.equals(other.firstname))
      return false;
    if (this.lastname == null) {
      if (other.lastname != null)
        return false;
    }
    else if (!this.lastname.equals(other.lastname))
      return false;
    if (this.location == null) {
      if (other.location != null)
        return false;
    }
    else if (!this.location.equals(other.location))
      return false;
    if (this.logo == null) {
      if (other.logo != null)
        return false;
    }
    else if (!this.logo.equals(other.logo))
      return false;
    if (this.name == null) {
      if (other.name != null)
        return false;
    }
    else if (!this.name.equals(other.name))
      return false;
    if (this.notes == null) {
      if (other.notes != null)
        return false;
    }
    else if (!this.notes.equals(other.notes))
      return false;
    if (this.phone == null) {
      if (other.phone != null)
        return false;
    }
    else if (!this.phone.equals(other.phone))
      return false;
    if (this.plan == null) {
      if (other.plan != null)
        return false;
    }
    else if (!this.plan.equals(other.plan))
      return false;
    if (this.refrees == null) {
      if (other.refrees != null)
        return false;
    }
    else if (!this.refrees.equals(other.refrees))
      return false;
    if (this.schedules == null) {
      if (other.schedules != null)
        return false;
    }
    else if (!this.schedules.equals(other.schedules))
      return false;
    if (this.sport == null) {
      if (other.sport != null)
        return false;
    }
    else if (!this.sport.equals(other.sport))
      return false;
    if (this.teams == null) {
      if (other.teams != null)
        return false;
    }
    else if (!this.teams.equals(other.teams))
      return false;
    if (this.unassignedteams == null) {
      if (other.unassignedteams != null)
        return false;
    }
    else if (!this.unassignedteams.equals(other.unassignedteams))
      return false;
    if (this.website == null) {
      if (other.website != null)
        return false;
    }
    else if (!this.website.equals(other.website))
      return false;
    return true;
  }
  
  
}
