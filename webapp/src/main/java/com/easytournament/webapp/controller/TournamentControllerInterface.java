package com.easytournament.webapp.controller;

import javax.ejb.Local;

import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;

@Local
public interface TournamentControllerInterface {

  public Tournament loadTournament(Integer id);
  public void saveTournament(User user, Tournament tournament);

}
