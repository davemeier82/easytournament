package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.TournamentControllerInterface;
import com.easytournament.webapp.entity.PlayerTournament;
import com.easytournament.webapp.entity.TeamTournament;
import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTeam;
import com.easytournament.webapp.entity.UserTournament;
import com.easytournament.webapp.type.Country;
import com.easytournament.webapp.type.Sport;

@SuppressWarnings("serial")
@Named("tournamentBean")
@ViewScoped
public class TournamentBean implements Serializable {

  @Inject
  private AuthenticationBean authenticationBean;

  @EJB
  private TournamentControllerInterface tournamentController;

  private Tournament tournament = new Tournament();

  private List<SelectItem> sports = new ArrayList<SelectItem>();

  private List<SelectItem> countries = new ArrayList<SelectItem>();

  private String link;

  private TournamentBeanMode mode = TournamentBeanMode.CREATE;

  public void create() {
    setLink();
    tournament.setLastModified(new Date());
    User currentUser = authenticationBean.getCurrentUser();
    tournamentController.saveTournament(currentUser, tournament);
  }

  public void update() {
    tournamentController.updateTournament(tournament);
  }

  public void acceptPlayer(PlayerTournament playerTournament) {
    playerTournament.setAccepted(true);
    tournamentController.updatePlayerTournament(playerTournament);
  }

  public void declinePlayer(PlayerTournament playerTournament) {
    tournamentController.removePlayerFromTournament(playerTournament);
  }

  public void acceptTeam(TeamTournament teamTournament) {
    teamTournament.setAccepted(true);
    tournamentController.updateTeamTournament(teamTournament);
  }

  public void declineTeam(TeamTournament teamTournament) {
    tournamentController.removeTeamFromTournament(teamTournament);
  }

  @PostConstruct
  private void init() {
    initLists();
  }

  private void setLink() {
    List<String> links = tournamentController.loadLinks();
    Calendar cal = Calendar.getInstance();
    cal.setTime(tournament.getStartTime());
    int year = cal.get(Calendar.YEAR);

    String tournName = tournament.getName().replace(" ", "_");
    String linktemplate;
    try {
      tournName = URLEncoder.encode(tournName, "UTF-8");
      linktemplate = tournName.concat("_").concat(Integer.toString(year));
    }
    catch (UnsupportedEncodingException e) {
      linktemplate = Integer.toString(tournament.getId());
    }

    String currentLink = linktemplate;
    int _count = 0;
    while (links.contains(currentLink)) {
      if (_count == Integer.MAX_VALUE) {
        currentLink = Integer.toString(tournament.getId());
        break;
      }
      currentLink = linktemplate.concat("_").concat(Integer.toString(_count++));
    }
    tournament.setLink(currentLink);
  }

  public void initLists() {
    sports.add(new SelectItem(Sport.OTHER, ""));
    for (Sport s : Sport.values()) {
      sports.add(new SelectItem(s, s.name()));
    }

    for (Country c : Country.values()) {
      countries.add(new SelectItem(c, c.name()));
    }
  }

  /**
   * @return the tournament
   */
  public Tournament getTournament() {
    return tournament;
  }

  /**
   * @param tournament
   *          the tournament to set
   */
  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }

  /**
   * @return the sports
   */
  public List<SelectItem> getSports() {
    return sports;
  }

  /**
   * @return the countries
   */
  public List<SelectItem> getCountries() {
    return countries;
  }

  /**
   * @return the link
   */
  public String getLink() {
    return link;
  }

  /**
   * @param link
   *          the link to set
   */
  public void setLink(String link) {
    mode = TournamentBeanMode.SHOW;
    this.link = link;
    this.tournament = null;
    if (link != null) {
      Tournament t = tournamentController.loadTournament(link);
      if (t != null) {
        if (t.isPublicTournament()) {
          tournament = t;
        }
        else {
          User currentUser = authenticationBean.getCurrentUser();
          if (currentUser != null) {
            for (UserTournament ut : t.getUserTournament()) {
              if (ut.getUser().equals(currentUser)) {
                tournament = t;
                break;
              }
            }
            if (tournament == null) {
              if (t.isTeamTournanament()) {
                for (TeamTournament tt : t.getTeamTournament()) {
                  for (UserTeam ut : tt.getTeam().getUserTeam()) {
                    if (ut.getUser().equals(currentUser)) {
                      tournament = t;
                      break;
                    }
                  }
                  if (tournament != null) {
                    break;
                  }
                }
              }
              else {
                for (PlayerTournament pt : t.getPlayerTournament()) {
                  if (pt.getPlayer().getUser().equals(currentUser)) {
                    tournament = t;
                    break;
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * @return the mode
   */
  public TournamentBeanMode getMode() {
    return mode;
  }

  /**
   * @param mode
   *          the mode to set
   */
  public void setMode(TournamentBeanMode mode) {
    this.mode = mode;
  }

  public void editParticipants() {
    if (tournament.isTeamTournanament()) {
      setMode(TournamentBeanMode.MANAGE_TEAM);
    }
    else {
      setMode(TournamentBeanMode.MANAGE_PLAYER);
    }
  }

  // @AssertTrue(message = "The start date has to be before the end date.")
  // public boolean isStartBeforeEnd(){
  // return startTime.before(endTime);
  // }
  //
  // @AssertTrue(message = "The start date is to close too current time.")
  // public boolean isStartInFuture(){
  // return startTime.after(new Date());
  // }
  //
  // @AssertTrue(message =
  // "The application end date has to be before the start date.")
  // public boolean isApplicationTimeBeforeStart(){
  // return applicationEndTime.before(startTime);
  // }
  //
  // @AssertTrue(message =
  // "The application end date is to close too current time.")
  // public boolean isApplicationTimeInFuture(){
  // return applicationEndTime.after(new Date());
  // }

}
