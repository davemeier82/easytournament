package com.easytournament.webapp.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.easytournament.webapp.entity.id.UserTeamId;

@SuppressWarnings("serial")
@Entity
@Table(name = "user_team")
public class UserTeam implements Serializable {

  private Integer roleid;

  @ManyToOne
  @JoinColumn(name = "userid", insertable = false, updatable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "teamid", insertable = false, updatable = false)
  private Team team;

  @EmbeddedId
  private UserTeamId id = new UserTeamId();

  public UserTeam() {
    super();
  }

  public UserTeam(Integer roleid, User user, Team team) {
    super();
    this.roleid = roleid;
    this.user = user;
    this.team = team;
    this.id.setUserid(user.getId());
    this.id.setTeamid(team.getId());

    user.getUserTeam().add(this);
    team.getUserTeam().add(this);
  }

  /**
   * @return the roleid
   */
  public Integer getRoleid() {
    return roleid;
  }

  /**
   * @param roleid
   *          the roleid to set
   */
  public void setRoleid(Integer roleid) {
    this.roleid = roleid;
  }

  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * @param user
   *          the user to set
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * @return the id
   */
  public UserTeamId getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(UserTeamId id) {
    this.id = id;
  }

  /**
   * @return the team
   */
  public Team getTeam() {
    return team;
  }

  /**
   * @param team
   *          the team to set
   */
  public void setTeam(Team team) {
    this.team = team;
  }

}
