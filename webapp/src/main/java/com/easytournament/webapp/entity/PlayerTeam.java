package com.easytournament.webapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.easytournament.webapp.entity.id.PlayerTeamId;

@SuppressWarnings("serial")
@Entity
@Table(name = "player_team")
public class PlayerTeam implements Serializable {

  @Column(name = "accepted", columnDefinition = "BIT", length = 1)
  private Boolean accepted;

  @ManyToOne
  @JoinColumn(name = "playerid", insertable = false, updatable = false)
  private Player player;

  @ManyToOne
  @JoinColumn(name = "teamid", insertable = false, updatable = false)
  private Team team;

  @EmbeddedId
  private PlayerTeamId id = new PlayerTeamId();

  public PlayerTeam() {
    super();
  }

  public PlayerTeam(Boolean accepted, Player player, Team team) {
    super();
    this.accepted = accepted;
    this.player = player;
    this.team = team;
    this.id.setPlayerid(player.getId());
    this.id.setTeamid(team.getId());

    player.getPlayerTeam().add(this);
    team.getPlayerTeam().add(this);
  }

  /**
   * @return the accepted
   */
  public Boolean getAccepted() {
    return accepted;
  }

  /**
   * @param accepted the accepted to set
   */
  public void setAccepted(Boolean accepted) {
    this.accepted = accepted;
  }

  /**
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * @param player the player to set
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * @return the team
   */
  public Team getTeam() {
    return team;
  }

  /**
   * @param team the team to set
   */
  public void setTeam(Team team) {
    this.team = team;
  }

  /**
   * @return the id
   */
  public PlayerTeamId getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(PlayerTeamId id) {
    this.id = id;
  }



}
