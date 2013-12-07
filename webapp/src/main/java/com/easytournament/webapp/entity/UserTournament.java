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

import com.easytournament.webapp.entity.id.UserTournamentId;
import com.easytournament.webapp.type.Role;

@SuppressWarnings("serial")
@Entity
@Table(name = "user_tournament")
public class UserTournament implements Serializable {
  
  @NotNull
  @Enumerated(EnumType.STRING)
  private Role role;
  
  @ManyToOne
  @JoinColumn(name="userid", insertable = false, updatable = false)
  private User user;
  
  @ManyToOne
  @JoinColumn(name="tournamentid", insertable = false, updatable = false)
  private Tournament tournament;
    
  @EmbeddedId
  private UserTournamentId id = new UserTournamentId();

  public UserTournament() {
    super();
  }
  public UserTournament(Role role, User user, Tournament tournament) {
    super();
    this.role = role;
    this.user = user;
    this.tournament = tournament;
    this.id.setUserid(user.getId());
    this.id.setTournamentid(tournament.getId());
    
    user.getUserTournament().add(this);
    tournament.getUserTournament().add(this);    
  }
  /**
   * @return the roleid
   */
  public Role getRoleid() {
    return role;
  }
  /**
   * @param roleid the roleid to set
   */
  public void setRoleid(Role roleid) {
    this.role = roleid;
  }
  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }
  /**
   * @param user the user to set
   */
  public void setUser(User user) {
    this.user = user;
  }
  /**
   * @return the tournament
   */
  public Tournament getTournament() {
    return tournament;
  }
  /**
   * @param tournament the tournament to set
   */
  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }
  /**
   * @return the id
   */
  public UserTournamentId getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(UserTournamentId id) {
    this.id = id;
  }

}
