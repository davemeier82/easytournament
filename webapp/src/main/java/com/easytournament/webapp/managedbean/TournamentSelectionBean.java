package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import com.easytournament.webapp.controller.TournamentControllerInterface;
import com.easytournament.webapp.entity.Tournament;

@SuppressWarnings("serial")
@Named("tournSelectionBean")
@ViewScoped
public class TournamentSelectionBean implements Serializable {

  @EJB
  private TournamentControllerInterface tournamentController;

  private List<Tournament> tournaments;

  @PostConstruct
  public void init() {
    tournaments = tournamentController.loadTournaments(true, false);
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

}
