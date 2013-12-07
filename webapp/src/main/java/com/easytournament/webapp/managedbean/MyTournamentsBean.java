package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.TournamentControllerInterface;
import com.easytournament.webapp.controller.UserControllerInterface;
import com.easytournament.webapp.entity.PlayerTournament;
import com.easytournament.webapp.entity.TeamTournament;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTournament;

@SuppressWarnings("serial")
@Named("myTournBean")
@ViewScoped
public class MyTournamentsBean implements Serializable {

  @EJB
  private UserControllerInterface userController;

  @EJB
  private TournamentControllerInterface tournamentController;

  @Inject
  private AuthenticationBean authenticationBean;

  private List<UserTournament> tournaments;

  private List<TeamTournament> registredTeamTournaments;

  private List<PlayerTournament> registredPlayerTournaments;

  private PlayerTournament currentPlayerTournament;

  private TeamTournament currentTeamTournament;

  @PostConstruct
  public void init() {
    User user = authenticationBean.getCurrentUser();
    tournaments = userController.loadUserTournaments(user.getId());
    registredTeamTournaments = userController.loadRegistredTeamTournaments(user
        .getId());
    registredPlayerTournaments = userController
        .loadRegistredPlayerTournaments(user.getId());
  }

  public void signOff() {
    User user = authenticationBean.getCurrentUser();
    if (currentPlayerTournament != null) {
      tournamentController.removePlayerFromTournament(currentPlayerTournament);
      registredPlayerTournaments = userController
          .loadRegistredPlayerTournaments(user.getId());
    }
    else if (currentTeamTournament != null) {
      tournamentController.removeTeamFromTournament(currentTeamTournament);
      registredTeamTournaments = userController
          .loadRegistredTeamTournaments(user.getId());
    }
  }

  public void setCurrentPlayerTournament(PlayerTournament playerTournament) {
    this.currentPlayerTournament = playerTournament;
    this.currentTeamTournament = null;
  }

  public void setCurrentTeamTournament(TeamTournament teamTournament) {
    this.currentPlayerTournament = null;
    this.currentTeamTournament = teamTournament;
  }

  /**
   * @return the tournaments
   */
  public List<UserTournament> getTournaments() {
    return tournaments;
  }

  /**
   * @return the registredTeamTournaments
   */
  public List<TeamTournament> getRegistredTeamTournaments() {
    return registredTeamTournaments;
  }

  /**
   * @return the registredPlayerTournaments
   */
  public List<PlayerTournament> getRegistredPlayerTournaments() {
    return registredPlayerTournaments;
  }

  /**
   * @return the currentPlayerTournament
   */
  public PlayerTournament getCurrentPlayerTournament() {
    return currentPlayerTournament;
  }

  /**
   * @return the currentTeamTournament
   */
  public TeamTournament getCurrentTeamTournament() {
    return currentTeamTournament;
  }

}
