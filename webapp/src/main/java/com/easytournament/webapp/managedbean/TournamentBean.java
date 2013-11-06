package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.easytournament.webapp.controller.SportControllerInterface;
import com.easytournament.webapp.controller.TournamentControllerInterface;
import com.easytournament.webapp.entity.Sport;
import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;

@SuppressWarnings("serial")
@Named("tournamentBean")
@RequestScoped
public class TournamentBean implements Serializable {

  @Inject
  private AuthenticationBean authenticationBean;
  
  @EJB
  private SportControllerInterface sportController;
  
  @EJB
  private TournamentControllerInterface tournamentController;
  
  @Size(min = 1, max = 45)
  private String name;

  private Date startTime;

  private Date endTime;

  private Date applicationEndTime;

  @Max(127)
  @Min(2)
  private Short nTeams;

  private boolean publicTournament = true;

  private boolean closed = false;
  
  private boolean teamTournament = true;

  
  private List<SelectItem> sports = new ArrayList<SelectItem>();
  private Short sportid = -1;
  
  private List<SelectItem> countries = new ArrayList<SelectItem>();
  private Short country = -1;
  
  @Length(min = 1, max = 45)
  private String city;

  private String sport;

  @Length(min = 0, max = 512)
  private String description;
  @Length(min = 0, max = 256)
  private String address;
  @Length(min = 0, max = 128)
  private String website;
  @Length(min = 0, max = 45)
  private String contactfirstname;
  @Length(min = 0, max = 45)
  private String contactlastname;
  @Length(min = 0, max = 20)
  private String contactphone;
  @Length(min = 0, max = 45)
  private String contactemail;
  @Length(min = 0, max = 256)
  private String contactaddress;
  @Length(min = 0, max = 15)
  private String fee;
  
  
  public void create() {
    Tournament tourn = new Tournament();
    tourn.setName(name);
    tourn.setAddress(address);
    tourn.setApplicationEndTime(applicationEndTime);
    tourn.setClosed(false);
    tourn.setContactaddress(contactaddress);
    tourn.setContactemail(contactemail);
    tourn.setContactfirstname(contactfirstname);
    tourn.setContactlastname(contactlastname);
    tourn.setContactphone(contactphone);
    tourn.setDescription(description);
    tourn.setEndTime(endTime);
    tourn.setFee(fee);
    tourn.setnTeams(nTeams);
    tourn.setPublicTournament(publicTournament);
    tourn.setSportid(sportid);
    tourn.setSport(sport);
    tourn.setStartTime(startTime);
    tourn.setTeamTournanament(teamTournament);
    tourn.setWebsite(website);
    tourn.setLastModified(new Date());
    User currentUser = authenticationBean.getCurrentUser();
    tournamentController.saveTournament(currentUser, tourn);
  }
  
  @PostConstruct
  public void initSportsList(){
    List<Sport> allsports = sportController.loadSports();
    sports.add(new SelectItem(-1, ""));
    for(Sport s : allsports){
      sports.add(new SelectItem(s.getId(), s.getName()));
    }
  }
  
//  @AssertTrue(message = "The start date has to be before the end date.")
//  public boolean isStartBeforeEnd(){
//    return startTime.before(endTime);
//  }
//  
//  @AssertTrue(message = "The start date is to close too current time.")
//  public boolean isStartInFuture(){
//    return startTime.after(new Date());
//  }
//  
//  @AssertTrue(message = "The application end date has to be before the start date.")
//  public boolean isApplicationTimeBeforeStart(){
//    return applicationEndTime.before(startTime);
//  }
//  
//  @AssertTrue(message = "The application end date is to close too current time.")
//  public boolean isApplicationTimeInFuture(){
//    return applicationEndTime.after(new Date());
//  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the nTeams
   */
  public Short getnTeams() {
    return nTeams;
  }

  /**
   * @param nTeams
   *          the nTeams to set
   */
  public void setnTeams(Short nTeams) {
    this.nTeams = nTeams;
  }

  /**
   * @return the publicTournament
   */
  public boolean isPublicTournament() {
    return publicTournament;
  }

  /**
   * @param publicTournament
   *          the publicTournament to set
   */
  public void setPublicTournament(boolean publicTournament) {
    this.publicTournament = publicTournament;
  }

  /**
   * @return the closed
   */
  public boolean isClosed() {
    return closed;
  }

  /**
   * @param closed
   *          the closed to set
   */
  public void setClosed(boolean closed) {
    this.closed = closed;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param address
   *          the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * @return the website
   */
  public String getWebsite() {
    return website;
  }

  /**
   * @param webseite
   *          the website to set
   */
  public void setWebsite(String website) {
    this.website = website;
  }

  /**
   * @return the contactfirstname
   */
  public String getContactfirstname() {
    return contactfirstname;
  }

  /**
   * @param contactfirstname
   *          the contactfirstname to set
   */
  public void setContactfirstname(String contactfirstname) {
    this.contactfirstname = contactfirstname;
  }

  /**
   * @return the contactlastname
   */
  public String getContactlastname() {
    return contactlastname;
  }

  /**
   * @param contactlastname
   *          the contactlastname to set
   */
  public void setContactlastname(String contactlastname) {
    this.contactlastname = contactlastname;
  }

  /**
   * @return the contactphone
   */
  public String getContactphone() {
    return contactphone;
  }

  /**
   * @param contactphone
   *          the contactphone to set
   */
  public void setContactphone(String contactphone) {
    this.contactphone = contactphone;
  }

  /**
   * @return the contactemail
   */
  public String getContactemail() {
    return contactemail;
  }

  /**
   * @param contactemail
   *          the contactemail to set
   */
  public void setContactemail(String contactemail) {
    this.contactemail = contactemail;
  }

  /**
   * @return the contactaddress
   */
  public String getContactaddress() {
    return contactaddress;
  }

  /**
   * @param contactaddress
   *          the contactaddress to set
   */
  public void setContactaddress(String contactaddress) {
    this.contactaddress = contactaddress;
  }

  /**
   * @return the startTime
   */
  public Date getStartTime() {
    return startTime;
  }

  /**
   * @param startTime the startTime to set
   */
  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /**
   * @return the endTime
   */
  public Date getEndTime() {
    return endTime;
  }

  /**
   * @param endTime the endTime to set
   */
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  /**
   * @return the applicationEndTime
   */
  public Date getApplicationEndTime() {
    return applicationEndTime;
  }

  /**
   * @param applicationEndTime the applicationEndTime to set
   */
  public void setApplicationEndTime(Date applicationEndTime) {
    this.applicationEndTime = applicationEndTime;
  }

  /**
   * @return the sports
   */
  public List<SelectItem> getSports() {
    return sports;
  }

  /**
   * @param sports the sports to set
   */
  public void setSports(List<SelectItem> sports) {
    this.sports = sports;
  }

  /**
   * @return the fee
   */
  public String getFee() {
    return fee;
  }

  /**
   * @param fee the fee to set
   */
  public void setFee(String fee) {
    this.fee = fee;
  }

  /**
   * @return the sportid
   */
  public Short getSportid() {
    return sportid;
  }

  /**
   * @param sportid the sportid to set
   */
  public void setSportid(Short sportid) {
    this.sportid = sportid;
  }

  /**
   * @return the sport
   */
  public String getSport() {
    return sport;
  }

  /**
   * @param sport the sport to set
   */
  public void setSport(String sport) {
    this.sport = sport;
  }

  /**
   * @return the countries
   */
  public List<SelectItem> getCountries() {
    return countries;
  }

  /**
   * @param countries the countries to set
   */
  public void setCountries(List<SelectItem> countries) {
    this.countries = countries;
  }

  /**
   * @return the country
   */
  public Short getCountry() {
    return country;
  }

  /**
   * @param country the country to set
   */
  public void setCountry(Short country) {
    this.country = country;
  }

  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city the city to set
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @return the teamTournament
   */
  public boolean isTeamTournament() {
    return teamTournament;
  }

  /**
   * @param teamTournament the teamTournament to set
   */
  public void setTeamTournament(boolean teamTournament) {
    this.teamTournament = teamTournament;
  }

}
