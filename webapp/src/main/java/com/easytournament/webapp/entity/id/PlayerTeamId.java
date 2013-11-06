package com.easytournament.webapp.entity.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Embeddable
public class PlayerTeamId implements Serializable {

  @NotNull
  private Integer playerid;
  @NotNull
  private Integer tournamentid;

  public PlayerTeamId() {
    super();
  }

  public PlayerTeamId(int playerid, int tournamentid) {
    super();
    this.playerid = playerid;
    this.tournamentid = tournamentid;
  }

  /**
   * @return the playerid
   */
  public Integer getPlayerid() {
    return playerid;
  }

  /**
   * @param playerid
   *          the playerid to set
   */
  public void setPlayerid(Integer playerid) {
    this.playerid = playerid;
  }

  /**
   * @return the tournamentid
   */
  public Integer getTournamentid() {
    return tournamentid;
  }

  /**
   * @param tournamentid
   *          the tournamentid to set
   */
  public void setTournamentid(Integer tournamentid) {
    this.tournamentid = tournamentid;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((playerid == null)? 0 : playerid.hashCode());
    result = prime * result
        + ((tournamentid == null)? 0 : tournamentid.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PlayerTeamId other = (PlayerTeamId)obj;
    if (playerid == null) {
      if (other.playerid != null)
        return false;
    }
    else if (!playerid.equals(other.playerid))
      return false;
    if (tournamentid == null) {
      if (other.tournamentid != null)
        return false;
    }
    else if (!tournamentid.equals(other.tournamentid))
      return false;
    return true;
  }

}
