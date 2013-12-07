package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.TournamentControllerInterface;
import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.type.Country;
import com.easytournament.webapp.type.Sport;

@SuppressWarnings("serial")
@Named("tournamentBean")
@RequestScoped
public class TournamentBean implements Serializable {

  @Inject
  private AuthenticationBean authenticationBean;
  
  @EJB
  private TournamentControllerInterface tournamentController;
  
  private Tournament tournament = new Tournament();
  
  private List<SelectItem> sports = new ArrayList<SelectItem>();
  
  private List<SelectItem> countries = new ArrayList<SelectItem>();
    
  public void create() {
    tournament.setLastModified(new Date());
    User currentUser = authenticationBean.getCurrentUser();
    tournamentController.saveTournament(currentUser, tournament);
  }
  
  @PostConstruct
  public void initLists(){
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
   * @param tournament the tournament to set
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
  
//  @AssertTrue(message = "The start date has to be before the end date.")
//  public boolean isStartBeforeEnd(){
//    return startTime.before(endTime);
//  }
//  
//  @AssertTrue(message = "The start date is to close too current time.")
//  public boolean isStartInFuture(){
//    return startTime.after(new Date());
//  }
//  
//  @AssertTrue(message = "The application end date has to be before the start date.")
//  public boolean isApplicationTimeBeforeStart(){
//    return applicationEndTime.before(startTime);
//  }
//  
//  @AssertTrue(message = "The application end date is to close too current time.")
//  public boolean isApplicationTimeInFuture(){
//    return applicationEndTime.after(new Date());
//  }

  

}
