package com.easytournament.webapp.controller;

import javax.ejb.Local;

import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.Team;
import com.easytournament.webapp.entity.User;

@Local
public interface PlayerControllerInterface {

  public Player loadPlayer(Integer id);

  public void addPlayerToUser(Player player, User user);

  public void addPlayerToTeam(Team team, Player player, Boolean accepted);

}
