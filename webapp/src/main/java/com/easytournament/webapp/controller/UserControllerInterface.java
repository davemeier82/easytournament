package com.easytournament.webapp.controller;

import java.util.List;

import javax.ejb.Local;

import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTeam;
import com.easytournament.webapp.entity.UserTournament;

@Local
public interface UserControllerInterface {

  public User loadUser(Integer id);
  public User loadUser(String username);
  public void saveUser(User user);
  public boolean isUsernameExist(String username);
  public boolean isEmailExist(String email);
  public List<UserTournament> loadUserTournaments(Integer userid);
  public List<UserTeam> loadUserTeams(Integer userid);
  public List<Player> loadPlayers(Integer userid);
}
