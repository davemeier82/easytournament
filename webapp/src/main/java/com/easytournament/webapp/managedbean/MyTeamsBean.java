package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.UserControllerInterface;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTeam;

@SuppressWarnings("serial")
@Named("myTeamsBean")
@ViewScoped
public class MyTeamsBean implements Serializable {

  @EJB
  private UserControllerInterface userController;

  @Inject
  private AuthenticationBean authenticationBean;

  private List<UserTeam> teams;

  @PostConstruct
  public void init() {
    User user = authenticationBean.getCurrentUser();
    teams = userController.loadUserTeams(user.getId());
  }

  /**
   * @return the teams
   */
  public List<UserTeam> getTeams() {
    return teams;
  }

  /**
   * @param teams the teams to set
   */
  public void setTeams(List<UserTeam> teams) {
    this.teams = teams;
  }


}
