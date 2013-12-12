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
import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTournament;

@SuppressWarnings("serial")
@Named("tournSelectionBean")
@ViewScoped
public class TournamentSelectionBean implements Serializable {

  @Inject
  private AuthenticationBean authenticationBean;

  @EJB
  private UserControllerInterface userController;

  @EJB
  private TournamentControllerInterface tournamentController;

  private List<Tournament> tournaments;

  private Tournament currentTournament;

  @PostConstruct
  public void init() {
    tournaments = tournamentController.loadTournaments(true, false);
    User currentUser = authenticationBean.getCurrentUser();
    if (currentUser != null) {
      // The user can also see his private tournaments
      currentUser = userController.loadUser(currentUser.getId());
      List<UserTournament> _userTournaments = currentUser.getUserTournament();
      for (UserTournament ut : _userTournaments) {
        Tournament t = ut.getTournament();
        if (!t.isClosed() && !tournaments.contains(t)) {
          tournaments.add(t);
        }
      }
    }

  }

  /**
   * @return the tournaments
   */
  public List<Tournament> getTournaments() {
    return tournaments;
  }

  /**
   * @param tournaments
   *          the tournaments to set
   */
  public void setTournaments(List<Tournament> tournaments) {
    this.tournaments = tournaments;
  }

  /**
   * @return the currentTournament
   */
  public Tournament getCurrentTournament() {
    return currentTournament;
  }

  /**
   * @param currentTournament
   *          the currentTournament to set
   */
  public void setCurrentTournament(Tournament currentTournament) {
    this.currentTournament = currentTournament;
  }

}
