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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
public class Team implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  @Size(max=45)
  private String name;
  @NotNull
  @Column(name = "public", columnDefinition = "BIT", length = 1)
  private boolean publicTeam;
  @NotNull
  @Column(name = "closed", columnDefinition = "BIT", length = 1)
  private boolean closed;
  @NotNull
  @Column(name = "lastmodified", columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModified;

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
  private List<UserTeam> userTeam = new ArrayList<UserTeam>();

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
  private List<PlayerTeam> playerTeam = new ArrayList<PlayerTeam>();

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
  private List<TeamTournament> teamTournament = new ArrayList<TeamTournament>();

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
   * @return the publicTeam
   */
  public boolean isPublicTeam() {
    return publicTeam;
  }

  /**
   * @param publicTeam
   *          the publicTeam to set
   */
  public void setPublicTeam(boolean publicTeam) {
    this.publicTeam = publicTeam;
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
   * @return the userTeam
   */
  public List<UserTeam> getUserTeam() {
    return userTeam;
  }

  /**
   * @param userTeam
   *          the userTeam to set
   */
  public void setUserTeam(List<UserTeam> userTeam) {
    this.userTeam = userTeam;
  }

  /**
   * @return the playerTeam
   */
  public List<PlayerTeam> getPlayerTeam() {
    return playerTeam;
  }

  /**
   * @param playerTeam
   *          the playerTeam to set
   */
  public void setPlayerTeam(List<PlayerTeam> playerTeam) {
    this.playerTeam = playerTeam;
  }

  /**
   * @return the teamTournament
   */
  public List<TeamTournament> getTeamTournament() {
    return teamTournament;
  }

  /**
   * @param teamTournament
   *          the teamTournament to set
   */
  public void setTeamTournament(List<TeamTournament> teamTournament) {
    this.teamTournament = teamTournament;
  }

}
