package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.UserControllerInterface;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTournament;

@SuppressWarnings("serial")
@Named("myTournBean")
@ViewScoped
public class MyTournamentsBean implements Serializable {

  @EJB
  private UserControllerInterface userController;

  @Inject
  private AuthenticationBean authenticationBean;

  private List<UserTournament> tournaments;

  @PostConstruct
  public void init() {
    User user = authenticationBean.getCurrentUser();
    tournaments = userController.loadUserTournaments(user.getId());
  }

  /**
   * @return the tournaments
   */
  public List<UserTournament> getTournaments() {
    return tournaments;
  }

  /**
   * @param tournaments
   *          the tournaments to set
   */
  public void setTournaments(List<UserTournament> tournaments) {
    this.tournaments = tournaments;
  }

}
