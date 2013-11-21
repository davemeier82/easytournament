package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.TournamentControllerInterface;
import com.easytournament.webapp.controller.UserControllerInterface;
import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.PlayerTournament;
import com.easytournament.webapp.entity.Team;
import com.easytournament.webapp.entity.TeamTournament;
import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTeam;

@SuppressWarnings("serial")
@Named("tournRegBean")
@ViewScoped
public class TournamentRegistrationBean implements Serializable {

  @Inject
  private AuthenticationBean authenticationBean;

  @EJB
  private UserControllerInterface userController;

  @EJB
  private TournamentControllerInterface tournamentController;

  private List<UserTeam> teams;

  private List<Player> players;

  private Team team;

  private Player player;

  private Tournament tournament;

  public String register() {
    if (tournament != null) {
      if (tournament.isTeamTournanament()) {
        if (team != null) {
          for (TeamTournament ut : team.getTeamTournament()) {
            if (ut.getTournament().equals(tournament)) {
              return "failure";
            }
          }
          tournamentController.addTeamToTournament(tournament, team, false);
        }
      }
      else {
        if (player != null) {
          for (PlayerTournament pt : player.getPlayerTournament()) {
            if (pt.getTournament().equals(tournament)) {
              return "failure";
            }
          }
          tournamentController.addPlayerToTournament(tournament, player, false);
        }
      }
    }
    return "/app/home.jsf?faces-redirect=true";
  }

  @PostConstruct
  public void init() {
    String tournamentIdString = FacesContext.getCurrentInstance()
        .getExternalContext().getRequestParameterMap().get("tournId");
    if (tournamentIdString != null) {
      Integer tournamentId = Integer.valueOf(tournamentIdString);
      if (tournamentId != null) {
        tournament = tournamentController.loadTournament(tournamentId);
        User user = authenticationBean.getCurrentUser();

        if (tournament.isTeamTournanament()) {
          teams = userController.loadUserTeams(user.getId());
        }
        else {
          players = userController.loadPlayers(user.getId());
        }
      }
    }
  }

  /**
   * @return the teams
   */
  public List<UserTeam> getTeams() {
    return teams;
  }

  /**
   * @param teams
   *          the teams to set
   */
  public void setTeams(List<UserTeam> teams) {
    this.teams = teams;
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
   * @return the tournament
   */
  public Tournament getTournament() {
    return tournament;
  }

  /**
   * @return the players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * @param player
   *          the player to set
   */
  public void setPlayer(Player player) {
    this.player = player;
  }
}
