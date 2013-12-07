package com.easytournament.webapp.controller;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.PlayerTournament;
import com.easytournament.webapp.entity.TeamTournament;
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
    TypedQuery<User> q = em.createQuery(
        "from User usr where usr.username = :inusername", User.class);
    q.setParameter("inusername", username);
    List<User> results = q.getResultList();
    if (results.isEmpty()) {
      return null;
    }
    try {
      User user = results.get(0);
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
    TypedQuery<User> q = em.createQuery(
        "from User usr where usr.username = :inusername", User.class);
    q.setParameter("inusername", username);
    List<User> results = q.getResultList();
    if (results == null || results.size() <= 0)
      return false;
    return true;
  }

  @Override
  public boolean isEmailExist(String email) {
    TypedQuery<User> q = em.createQuery(
        "from User usr where usr.email = :inemail", User.class);
    q.setParameter("inemail", email);
    List<User> results = q.getResultList();
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

  @Override
  public List<TeamTournament> loadRegistredTeamTournaments(Integer userid) {
    TypedQuery<TeamTournament> q = em
        .createQuery(
            "select tt from Tournament t join t.teamTournament tt join tt.team tm join tm.userTeam ut join ut.user usr where usr.id = :inuserid",
            TeamTournament.class);
    q.setParameter("inuserid", userid);
    return q.getResultList();
  }

  @Override
  public List<PlayerTournament> loadRegistredPlayerTournaments(Integer userid) {
    TypedQuery<PlayerTournament> q = em
        .createQuery(
            "select pt from Tournament t join t.playerTournament pt join pt.player p join p.user usr where usr.id = :inuserid",
            PlayerTournament.class);
    q.setParameter("inuserid", userid);
    return q.getResultList();
  }
}
