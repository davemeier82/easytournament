package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.TeamControllerInterface;
import com.easytournament.webapp.entity.Team;
import com.easytournament.webapp.entity.User;

@SuppressWarnings("serial")
@Named("teamBean")
@RequestScoped
public class TeamBean implements Serializable {

  @Inject
  private AuthenticationBean authenticationBean;

  @EJB
  private TeamControllerInterface teamController;

  private Team team = new Team();

  public void create() {
    team.setLastModified(new Date());
    User currentUser = authenticationBean.getCurrentUser();
    teamController.saveTeam(currentUser, team);

  }

  /**
   * @return the team
   */
  public Team getTeam() {
    return team;
  }

  /**
   * @param team the team to set
   */
  public void setTeam(Team team) {
    this.team = team;
  }
}
