package com.easytournament.webapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.easytournament.webapp.entity.id.TeamTournamentId;

@SuppressWarnings("serial")
@Entity
@Table(name = "team_tournament")
public class TeamTournament implements Serializable {

  @Column(name = "accepted", columnDefinition = "BIT", length = 1)
  private Boolean accepted;

  @ManyToOne
  @JoinColumn(name = "teamid", insertable = false, updatable = false)
  private Team team;

  @ManyToOne
  @JoinColumn(name = "tournamentid", insertable = false, updatable = false)
  private Tournament tournament;

  @EmbeddedId
  private TeamTournamentId id = new TeamTournamentId();

  public TeamTournament() {
    super();
  }

  public TeamTournament(Boolean accepted, Team team, Tournament tournament) {
    super();
    this.accepted = accepted;
    this.team = team;
    this.tournament = tournament;
    this.id.setTeamid(team.getId());
    this.id.setTournamentid(tournament.getId());

    team.getTeamTournament().add(this);
    tournament.getTeamTournament().add(this);
  }

  /**
   * @return the accepted
   */
  public Boolean getAccepted() {
    return accepted;
  }

  /**
   * @param accepted
   *          the accepted to set
   */
  public void setAccepted(Boolean accepted) {
    this.accepted = accepted;
  }

  /**
   * @return the team
   */
  public Team getTeam() {
    return team;
  }

  /**
   * @param team
   *          the team to set
   */
  public void setTeam(Team team) {
    this.team = team;
  }

  /**
   * @return the tournament
   */
  public Tournament getTournament() {
    return tournament;
  }

  /**
   * @param tournament
   *          the tournament to set
   */
  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }

  /**
   * @return the id
   */
  public TeamTournamentId getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(TeamTournamentId id) {
    this.id = id;
  }

}
