package com.easytournament.webapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@SuppressWarnings("serial")
@Entity
@Table(name="tournament")
public class Tournament implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  @Length(min = 1, max = 45)
  private String name;
  @Column(columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startTime;
  @Column(columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endTime;
  @NotNull
  @Max(127)
  @Min(2)
  @Column(columnDefinition = "TINYINT")
  private Short nTeams;
  @NotNull
  @Column(name = "public", columnDefinition = "BIT", length = 1)
  private boolean publicTournament;
  @NotNull
  @Column(name = "closed", columnDefinition = "BIT", length = 1)
  private boolean closed;
  @Column(name = "applicationend", columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date applicationEndTime;
  @Column(name = "lastmodified", columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModified;
  @NotNull
  @Max(127)
  @Min(0)
  @Column(columnDefinition = "TINYINT")
  private Short sportid;
  @Length(min = 1, max = 45)
  private String sport;
  @Length(min = 0, max = 511)
  private String description;
  @Length(min = 0, max = 255)
  private String address;
  @NotNull
  @Max(999999999)
  @Min(0)
  private Integer zip;
  @NotNull
  private String city;
  @NotNull
  private Integer country;
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
  @Length(min = 0, max = 255)
  private String contactaddress;
  @Length(min = 0, max = 15)
  private String fee;
  @Column(name = "websiteok", columnDefinition = "BIT", length = 1)
  private boolean websiteok;
  @Column(name = "teams", columnDefinition = "BIT", length = 1)
  private boolean teamTournanament;
  @OneToMany(mappedBy="tournament", fetch=FetchType.LAZY)
  List<UserTournament> userTournament = new ArrayList<UserTournament>();
  @OneToMany(mappedBy="tournament", fetch=FetchType.LAZY)
  List<PlayerTournament> playerTournament = new ArrayList<PlayerTournament>();
  @OneToMany(mappedBy="tournament", fetch=FetchType.LAZY)
  List<TeamTournament> teamTournament = new ArrayList<TeamTournament>();
  
  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Integer id) {
    this.id = id;
  }

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
   * @return the startTime
   */
  public Date getStartTime() {
    return startTime;
  }

  /**
   * @param startTime
   *          the startTime to set
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
   * @param endTime
   *          the endTime to set
   */
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
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
   * @return the applicationEndTime
   */
  public Date getApplicationEndTime() {
    return applicationEndTime;
  }

  /**
   * @param applicationEndTime
   *          the applicationEndTime to set
   */
  public void setApplicationEndTime(Date applicationEndTime) {
    this.applicationEndTime = applicationEndTime;
  }

  /**
   * @return the lastModified
   */
  public Date getLastModified() {
    return lastModified;
  }

  /**
   * @param lastModified
   *          the lastModified to set
   */
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * @return the sport
   */
  public String getSport() {
    return sport;
  }

  /**
   * @param sport
   *          the sport to set
   */
  public void setSport(String sport) {
    this.sport = sport;
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
   * @param website
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
   * @return the websiteok
   */
  public boolean isWebsiteok() {
    return websiteok;
  }

  /**
   * @param websiteok
   *          the websiteok to set
   */
  public void setWebsiteok(boolean websiteok) {
    this.websiteok = websiteok;
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
   * @return the teamTournanament
   */
  public boolean isTeamTournanament() {
    return teamTournanament;
  }

  /**
   * @param teamTournanament the teamTournanament to set
   */
  public void setTeamTournanament(boolean teamTournanament) {
    this.teamTournanament = teamTournanament;
  }

  /**
   * @return the userTournament
   */
  public List<UserTournament> getUserTournament() {
    return userTournament;
  }

  /**
   * @param userTournament the userTournament to set
   */
  public void setUserTournament(List<UserTournament> userTournament) {
    this.userTournament = userTournament;
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
   * @return the country
   */
  public Integer getCountry() {
    return country;
  }

  /**
   * @param country the country to set
   */
  public void setCountry(Integer country) {
    this.country = country;
  }

  /**
   * @return the zip
   */
  public Integer getZip() {
    return zip;
  }

  /**
   * @param zip the zip to set
   */
  public void setZip(Integer zip) {
    this.zip = zip;
  }

  /**
   * @return the playerTournament
   */
  public List<PlayerTournament> getPlayerTournament() {
    return playerTournament;
  }

  /**
   * @param playerTournament the playerTournament to set
   */
  public void setPlayerTournament(List<PlayerTournament> playerTournament) {
    this.playerTournament = playerTournament;
  }

  /**
   * @return the teamTournament
   */
  public List<TeamTournament> getTeamTournament() {
    return teamTournament;
  }

  /**
   * @param teamTournament the teamTournament to set
   */
  public void setTeamTournament(List<TeamTournament> teamTournament) {
    this.teamTournament = teamTournament;
  }

}
