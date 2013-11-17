package com.easytournament.webapp.controller;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTeam;
import com.easytournament.webapp.entity.UserTournament;

@Stateful(name = "usercontroller")
@ConversationScoped
public class UserController implements UserControllerInterface {

  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  private EntityManager em;

  @Override
  public User loadUser(Integer id) {
    return em.find(User.class, id);
  }

  @Override
  public User loadUser(String username) {
    Query q = em.createQuery("from User usr where usr.username = :inusername");
    q.setParameter("inusername", username);
    List<?> results = q.getResultList();
    if (results.isEmpty()) {
      return null;
    }
    try {
      User user = (User)results.get(0);
      return user;
    }
    catch (ClassCastException ex) {
      return null;
    }
  }

  @Override
  public void saveUser(User user) {
    em.persist(user);
  }

  @Override
  public boolean isUsernameExist(String username) {
    Query q = em.createQuery("from User usr where usr.username = :inusername");
    q.setParameter("inusername", username);
    List<?> results = q.getResultList();
    if (results == null || results.size() <= 0)
      return false;
    return true;
  }

  @Override
  public boolean isEmailExist(String email) {
    Query q = em.createQuery("from User usr where usr.email = :inemail");
    q.setParameter("inemail", email);
    List<?> results = q.getResultList();
    if (results == null || results.size() <= 0)
      return false;
    return true;
  }

  @Override
  public List<UserTournament> loadUserTournaments(Integer userid) {
    User user = loadUser(userid);
    return user.getUserTournament();
  }

  @Override
  public List<UserTeam> loadUserTeams(Integer userid) {
    User user = loadUser(userid);
    return user.getUserTeam();
  }

  @Override
  public List<Player> loadPlayers(Integer userid) {
    User user = loadUser(userid);
    return user.getPlayers();
  }

}
