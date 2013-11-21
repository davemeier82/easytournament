package com.easytournament.webapp.entity.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Embeddable
public class TeamTournamentId implements Serializable {

  @NotNull
  private Integer tournamentid;
  @NotNull
  private Integer teamid;

  public TeamTournamentId() {
    super();
  }

  public TeamTournamentId(int teamid, int tournamentid) {
    super();
    this.teamid = teamid;
    this.tournamentid = tournamentid;
  }

  /**
   * @return the tournamentid
   */
  public Integer getTournamentid() {
    return tournamentid;
  }

  /**
   * @param tournamentid the tournamentid to set
   */
  public void setTournamentid(Integer tournamentid) {
    this.tournamentid = tournamentid;
  }

  /**
   * @return the teamid
   */
  public Integer getTeamid() {
    return teamid;
  }

  /**
   * @param teamid the teamid to set
   */
  public void setTeamid(Integer teamid) {
    this.teamid = teamid;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((teamid == null)? 0 : teamid.hashCode());
    result = prime * result
        + ((tournamentid == null)? 0 : tournamentid.hashCode());
    return result;
  }

  /* (non-Javadoc)
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
    TeamTournamentId other = (TeamTournamentId)obj;
    if (teamid == null) {
      if (other.teamid != null)
        return false;
    }
    else if (!teamid.equals(other.teamid))
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
