package com.easytournament.webapp.controller;

import javax.ejb.Local;

import com.easytournament.webapp.entity.Team;
import com.easytournament.webapp.entity.User;

@Local
public interface TeamControllerInterface {

  public Team loadTeam(Integer id);
  public void saveTeam(User user, Team tournament);

}
