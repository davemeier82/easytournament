package com.easytournament.webapp.entity.id;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class UserTournamentId implements Serializable {
  
  private Integer userid;
  
  private Integer tournamentid;
  
  public UserTournamentId(){
    super();
  }
  
  public UserTournamentId(int userid, int tournamentid) {
    super();
    this.userid = userid;
    this.tournamentid = tournamentid;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((tournamentid == null)? 0 : tournamentid.hashCode());
    result = prime * result + ((userid == null)? 0 : userid.hashCode());
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
    UserTournamentId other = (UserTournamentId)obj;
    if (tournamentid == null) {
      if (other.tournamentid != null)
        return false;
    }
    else if (!tournamentid.equals(other.tournamentid))
      return false;
    if (userid == null) {
      if (other.userid != null)
        return false;
    }
    else if (!userid.equals(other.userid))
      return false;
    return true;
  }

  /**
   * @return the userid
   */
  public Integer getUserid() {
    return userid;
  }

  /**
   * @param userid the userid to set
   */
  public void setUserid(Integer userid) {
    this.userid = userid;
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
  
}
