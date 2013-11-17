package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.UserControllerInterface;
import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.User;

@SuppressWarnings("serial")
@Named("myPlayersBean")
@ViewScoped
public class MyPlayersBean implements Serializable {

  @EJB
  private UserControllerInterface userController;

  @Inject
  private AuthenticationBean authenticationBean;

  private List<Player> players;

  @PostConstruct
  public void init() {
    User user = authenticationBean.getCurrentUser();
    players = userController.loadPlayers(user.getId());
  }

  /**
   * @return the players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * @param players the players to set
   */
  public void setPlayers(List<Player> players) {
    this.players = players;
  }


}
