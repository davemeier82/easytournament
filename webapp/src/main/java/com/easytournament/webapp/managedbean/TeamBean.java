package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;

import com.easytournament.webapp.controller.SportControllerInterface;
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
  private SportControllerInterface sportController;

  @EJB
  private TeamControllerInterface teamController;

  @Size(min = 1, max = 45)
  private String name;

  private boolean publicTeam = false;

  public void create() {
    Team team = new Team();
    team.setName(name);
    team.setPublicTeam(publicTeam);
    team.setLastModified(new Date());
    User currentUser = authenticationBean.getCurrentUser();
    teamController.saveTeam(currentUser, team);

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
}
