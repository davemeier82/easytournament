package com.easytournament.webapp.controller;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.PlayerTeam;
import com.easytournament.webapp.entity.Team;
import com.easytournament.webapp.entity.User;

@Stateful(name = "playercontroller")
@ConversationScoped
public class PlayerController implements PlayerControllerInterface {

  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  private EntityManager em;

  @Override
  public Player loadPlayer(Integer id) {
    return em.find(Player.class, id);
  }

  @Override
  public void addPlayerToUser(Player player, User user) {
    User manageduser = em.merge(user); // get managed entity
    manageduser.addPlayer(player);   
    em.persist(manageduser);
  }

  @Override
  public void addPlayerToTeam(Team team, Player player, Boolean accepted) {
    em.persist(player); 
    Team managedteam = em.merge(team); // get managed entity
    PlayerTeam pt = new PlayerTeam(accepted, player, managedteam); 
    team.getPlayerTeam().add(pt);
    managedteam.getPlayerTeam().add(pt);
    em.persist(pt);
  }
}
