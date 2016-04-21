package easytournament.basic.valueholder;

import javax.swing.ImageIcon;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.designer.valueholder.Group;
import easytournament.designer.valueholder.Position;


public class Team extends Model implements Cloneable, ListDataListener {

  public static int CURRENT_MAX_ID = 0;

  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_LOGO = "logo";
  public static final String PROPERTY_PHONE = "phone";
  public static final String PROPERTY_EMAIL = "email";
  public static final String PROPERTY_ADDRESS = "address";
  public static final String PROPERTY_NOTES = "notes";
  public static final String PROPERTY_CONTACT_PRENAME = "contactPrename";
  public static final String PROPERTY_CONTACT_NAME = "contactName";  
  public static final String PROPERTY_RANDOM_POS = "randomPosition";
  public static final String PROPERTY_PLAYERS = "players";
  public static final String PROPERTY_STAFF = "staff";
  public static final String PROPERTY_GROUPS = "groups"; 
  public static final String PROPERTY_WEBSITE = "website"; 
  
  protected ArrayListModel<Player> players;
  protected ArrayListModel<Staff> staff;
  protected ArrayListModel<Group> groups = new ArrayListModel<Group>();
  protected int id;  
  protected String name;
  protected String address;
  protected ImageIcon logo;
  protected String phone;
  protected String email;
  protected String notes;
  protected String contactPrename;
  protected String contactName;
  protected Integer randomPosition;
  protected String website;
  protected Position positionAssignedTo;
  

  public Team(String name, String contactPrename, String contactName, String address, 
      String phone, String email, String website, String notes, ImageIcon logo, ArrayListModel<Player> players,
      ArrayListModel<Staff> staff) {
    this(CURRENT_MAX_ID++,name,contactPrename,contactName,address,phone,email,website,notes,logo,players,staff);
  }
  
  public Team(int id, String name, String contactPrename, String contactName, String address, 
      String phone, String email, String website, String notes, ImageIcon logo, ArrayListModel<Player> players,
      ArrayListModel<Staff> staff) {
    this.id = id;
    this.name = name;
    this.contactPrename = contactPrename;
    this.contactName = contactName;
    this.address = address;
    this.phone = phone;
    this.email = email;
    this.website = website;
    this.notes = notes;    
    this.logo = logo;
    this.players = players;
    this.staff = staff;
  }
  
  public void setTeam(Team team) {
    this.setName(team.name);
    this.setContactPrename(team.contactPrename);
    this.setContactName(team.contactName);
    this.setAddress(team.address);
    this.setPhone(team.phone);
    this.setEmail(team.email);
    this.setNotes(team.notes);
    this.setLogo(team.logo);
    this.setPlayers(team.players);
    this.setStaff(team.staff);
    this.setWebsite(team.website);
    this.groups = team.groups;
    this.firePropertyChange(PROPERTY_GROUPS, null, this.groups);
  }

  public Team() {
    this("","","","","","","","",null,new ArrayListModel<Player>(),new ArrayListModel<Staff>());
  }
  
  public Team(int id) {
    this(id,"","","","","","","","",null,new ArrayListModel<Player>(),new ArrayListModel<Staff>());
  }

  public int getId() {
    return this.id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    String old = this.address;        
    this.address = address;
    this.firePropertyChange(PROPERTY_ADDRESS, old, this.address);
  }

  public ImageIcon getLogo() {
    return logo;
  }

  public void setLogo(ImageIcon logo) {
    ImageIcon old = this.logo;
    this.logo = logo;
    this.firePropertyChange(PROPERTY_LOGO, old, this.logo);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }

  public ArrayListModel<Player> getPlayers() {
    return players;
  }

  public void setPlayers(ArrayListModel<Player> players) {
    this.players.removeListDataListener(this);
    ArrayListModel<Player> old = this.players;
    this.players = players;
    this.players.addListDataListener(this);
    this.firePropertyChange(PROPERTY_PLAYERS, old, this.players);
  }

  public ArrayListModel<Staff> getStaff() {
    return staff;
  }

  public void setStaff(ArrayListModel<Staff> staff) {
    this.staff.removeListDataListener(this);
    ArrayListModel<Staff> old = this.staff;
    this.staff = staff;
    this.staff.addListDataListener(this);
    this.firePropertyChange(PROPERTY_STAFF, old, this.staff);
  }

  public void addGroup(Group g) {
    this.groups.add(g);
    this.firePropertyChange(PROPERTY_GROUPS, null, this.groups);
  }

  public void removeGroup(Group g) {
    this.groups.remove(g);
    this.firePropertyChange(PROPERTY_GROUPS, null, this.groups);
  }
  
  public void clearGroups(){
    this.groups.clear();
    this.firePropertyChange(PROPERTY_GROUPS, null, this.groups);
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    String old = this.phone;
    this.phone = phone;
    this.firePropertyChange(PROPERTY_PHONE, old, this.phone);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    String old = this.email;
    this.email = email;
    this.firePropertyChange(PROPERTY_EMAIL, old, this.email);
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    String old = this.notes;
    this.notes = notes;
    this.firePropertyChange(PROPERTY_NOTES, old, this.notes);
  }

  public String getContactPrename() {
    return contactPrename;
  }

  public void setContactPrename(String contactPrename) {
    String old = this.contactPrename;
    this.contactPrename = contactPrename;
    this.firePropertyChange(PROPERTY_CONTACT_PRENAME, old, this.contactPrename);
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    String old = this.contactName;
    this.contactName = contactName;
    this.firePropertyChange(PROPERTY_CONTACT_NAME, old, this.contactName);
  }

  public Integer getRandomPosition() {
    return randomPosition;
  }

  public void setRandomPosition(Integer randomPosition) {
    Integer old = this.randomPosition;
    this.randomPosition = randomPosition;
    this.firePropertyChange(PROPERTY_RANDOM_POS, old, this.randomPosition);
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    //Important: do NOT clone players!!!
    Team clone = new Team(name, contactPrename, contactName, address, 
        phone, email, website, notes, logo, new ArrayListModel<Player>(this.players), new ArrayListModel<Staff>(this.staff));
    for(Group g : groups){
      clone.addGroup(g);
    }
    return clone;
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    if(e.getSource() == this.players){
      this.firePropertyChange(PROPERTY_PLAYERS, null, this.players);
    } else if(e.getSource() == this.staff){
      this.firePropertyChange(PROPERTY_STAFF, null, this.staff);
    }
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    if(e.getSource() == this.players){
      this.firePropertyChange(PROPERTY_PLAYERS, null, this.players);
    } else if(e.getSource() == this.staff){
      this.firePropertyChange(PROPERTY_STAFF, null, this.staff);
    }
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    if(e.getSource() == this.players){
      this.firePropertyChange(PROPERTY_PLAYERS, null, this.players);
    } else if(e.getSource() == this.staff){
      this.firePropertyChange(PROPERTY_STAFF, null, this.staff);
    }
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    String old = this.website;
    this.website = website;
    this.firePropertyChange(PROPERTY_WEBSITE, old, this.website);
  }

  public void setPositionAssignedTo(Position pos) {
    this.positionAssignedTo = pos;    
  }

  public Position getPositionAssignedTo() {
    return positionAssignedTo;
  }  
  
  public void updateId(){
    this.id = Team.CURRENT_MAX_ID++;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
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
    Team other = (Team)obj;
    if (id != other.id)
      return false;
    return true;
  }

}
