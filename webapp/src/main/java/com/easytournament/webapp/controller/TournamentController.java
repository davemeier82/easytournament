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
import com.easytournament.webapp.entity.Team;
import com.easytournament.webapp.entity.TeamTournament;
import com.easytournament.webapp.entity.Tournament;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.entity.UserTournament;
import com.easytournament.webapp.type.Role;

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
  public Tournament loadTournament(String link) {
    TypedQuery<Tournament> q = em.createQuery(
        "select t from Tournament t where t.link = :inlink", Tournament.class);
    q.setParameter("inlink", link);
    List<Tournament> result = q.getResultList();
    if (result.isEmpty()) {
      return null;
    }

    return result.get(0);
  }

  @Override
  public void saveTournament(User user, Tournament tournament) {
    em.persist(tournament);
    User manageduser = em.merge(user); // get managed entity
    UserTournament ut = new UserTournament(Role.OWNER, manageduser, tournament);
    tournament.getUserTournament().add(ut);
    manageduser.getUserTournament().add(ut);
    em.persist(ut);
  }

  @Override
  public List<Tournament> loadTournaments(boolean publicTournament,
      boolean closedTournament) {
    TypedQuery<Tournament> q = em
        .createQuery(
            "from Tournament t where t.publicTournament = :inpublic and t.closed = :inclosed",
            Tournament.class);
    q.setParameter("inpublic", publicTournament);
    q.setParameter("inclosed", closedTournament);
    return q.getResultList();
  }

  @Override
  public void addTeamToTournament(Tournament tournament, Team team,
      Boolean accepted) {
    Team managedteam = em.merge(team);
    TeamTournament tt = new TeamTournament(accepted, managedteam, tournament);
    tournament.getTeamTournament().add(tt);
    team.getTeamTournament().add(tt);
    em.persist(tt);
  }

  @Override
  public void addPlayerToTournament(Tournament tournament, Player player,
      Boolean accepted) {
    Player managedplayer = em.merge(player);
    PlayerTournament pt = new PlayerTournament(accepted, managedplayer,
        tournament);
    tournament.getPlayerTournament().add(pt);
    em.persist(pt);
  }

  @Override
  public void removePlayerFromTournament(PlayerTournament playerTournament) {
    PlayerTournament managedPlayerTourn = em.merge(playerTournament);
    Tournament tournament = managedPlayerTourn.getTournament();
    tournament.getPlayerTournament().remove(managedPlayerTourn);
    Player player = managedPlayerTourn.getPlayer();
    player.getPlayerTournament().remove(managedPlayerTourn);
    em.remove(managedPlayerTourn);
  }

  @Override
  public void removeTeamFromTournament(TeamTournament teamTournament) {
    TeamTournament managedTeamTournament = em.merge(teamTournament);
    Tournament tournament = managedTeamTournament.getTournament();
    tournament.getTeamTournament().remove(managedTeamTournament);
    Team team = managedTeamTournament.getTeam();
    team.getTeamTournament().remove(managedTeamTournament);
    em.remove(managedTeamTournament);
  }

  @Override
  public List<String> loadLinks() {
    TypedQuery<String> q = em.createQuery("select t.link from Tournament t",
        String.class);
    return q.getResultList();
  }
}
