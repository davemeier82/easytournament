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

import com.easytournament.webapp.controller.PlayerControllerInterface;
import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.type.Country;

@SuppressWarnings("serial")
@Named("playerBean")
@RequestScoped
public class PlayerBean implements Serializable {

  @Inject
  private AuthenticationBean authenticationBean;

  @EJB
  private PlayerControllerInterface playerController;

  private Player player = new Player();

  private List<SelectItem> countries = new ArrayList<SelectItem>();

  public void create() {
    player.setLastModified(new Date());
    User currentUser = authenticationBean.getCurrentUser();
    playerController.addPlayerToUser(player, currentUser);
  }

  @PostConstruct
  public void initCountriesList() {
    for (Country c : Country.values()) {
      countries.add(new SelectItem(c, c.name()));
    }
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

  /**
   * @return the countries
   */
  public List<SelectItem> getCountries() {
    return countries;
  }

}
