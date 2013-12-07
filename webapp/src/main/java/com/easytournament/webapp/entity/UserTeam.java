package com.easytournament.webapp.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.easytournament.webapp.entity.id.UserTeamId;
import com.easytournament.webapp.type.Role;

@SuppressWarnings("serial")
@Entity
@Table(name = "user_team")
public class UserTeam implements Serializable {

  @NotNull
  @Enumerated(EnumType.STRING)
  private Role role;

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

  public UserTeam(Role role, User user, Team team) {
    super();
    this.role = role;
    this.user = user;
    this.team = team;
    this.id.setUserid(user.getId());
    this.id.setTeamid(team.getId());

    user.getUserTeam().add(this);
    team.getUserTeam().add(this);
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

  /**
   * @return the role
   */
  public Role getRole() {
    return role;
  }

  /**
   * @param role the role to set
   */
  public void setRole(Role role) {
    this.role = role;
  }

}
