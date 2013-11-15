package com.easytournament.webapp.controller;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import com.easytournament.webapp.entity.Team;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTeam;

@Stateful(name = "teamcontroller")
@ConversationScoped
public class TeamController implements TeamControllerInterface {

  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  private EntityManager em;

  @Override
  public Team loadTeam(Integer id) {
    return em.find(Team.class, id);
  }

  @Override
  public void saveTeam(User user, Team team) {
    em.persist(team); 
    User manageduser = em.merge(user); // get managed entity
    UserTeam ut = new UserTeam(0, manageduser, team); 
    team.getUserTeam().add(ut);
    manageduser.getUserTeam().add(ut);
    em.persist(ut);
  }
}
