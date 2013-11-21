package com.easytournament.webapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.easytournament.webapp.entity.id.PlayerTournamentId;

@SuppressWarnings("serial")
@Entity
@Table(name = "player_tournament")
public class PlayerTournament implements Serializable {
  
  @Column(name = "accepted", columnDefinition = "BIT", length = 1)
  private Boolean accepted;
  
  @ManyToOne
  @JoinColumn(name="playerid", insertable = false, updatable = false)
  private Player player;
  
  @ManyToOne
  @JoinColumn(name="tournamentid", insertable = false, updatable = false)
  private Tournament tournament;
    
  @EmbeddedId
  private PlayerTournamentId id = new PlayerTournamentId();

  public PlayerTournament() {
    super();
  }
  public PlayerTournament(Boolean accepted, Player player, Tournament tournament) {
    super();
    this.accepted = accepted;
    this.player = player;
    this.tournament = tournament;
    this.id.setPlayerid(player.getId());
    this.id.setTournamentid(tournament.getId());
    
    player.getPlayerTournament().add(this);
    tournament.getPlayerTournament().add(this);    
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
   * @return the tournament
   */
  public Tournament getTournament() {
    return tournament;
  }
  /**
   * @param tournament the tournament to set
   */
  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }
  /**
   * @return the id
   */
  public PlayerTournamentId getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(PlayerTournamentId id) {
    this.id = id;
  }
}
