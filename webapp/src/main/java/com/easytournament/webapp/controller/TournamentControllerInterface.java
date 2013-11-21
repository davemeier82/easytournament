package com.easytournament.webapp.controller;

import java.util.List;

import javax.ejb.Local;

import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.Team;
import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;

@Local
public interface TournamentControllerInterface {

  public Tournament loadTournament(Integer id);
  public List<Tournament> loadTournaments(boolean publicTournament, boolean closedTournament);
  public void saveTournament(User user, Tournament tournament);
  public void addTeamToTournament(Tournament tournament, Team team, Boolean accepted);
  public void addPlayerToTournament(Tournament tournament, Player team, Boolean accepted);

}