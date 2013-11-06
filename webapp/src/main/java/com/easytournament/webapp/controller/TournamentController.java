package com.easytournament.webapp.controller;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTournament;

@Stateful(name = "tournamentcontroller")
@ConversationScoped
public class TournamentController implements TournamentControllerInterface {

  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  private EntityManager em;

  @Override
  public Tournament loadTournament(Integer id) {
    return em.find(Tournament.class, id);
  }

  @Override
  public void saveTournament(User user, Tournament tournament) {
      em.persist(tournament); 
      User manageduser = em.merge(user); // get managed entity
      UserTournament ut = new UserTournament(0, manageduser, tournament); 
      tournament.getUserTournament().add(ut);
      manageduser.getUserTournament().add(ut);
      em.persist(ut);
  }
}
