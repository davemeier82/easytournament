package com.easytournament.webapp.entity.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Embeddable
public class UserTeamId implements Serializable {

  @NotNull
  private Integer userid;
  @NotNull
  private Integer teamid;

  public UserTeamId() {
    super();
  }

  public UserTeamId(int userid, int teamid) {
    super();
    this.userid = userid;
    this.teamid = teamid;
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
    UserTeamId other = (UserTeamId)obj;
    if (teamid == null) {
      if (other.teamid != null)
        return false;
    }
    else if (!teamid.equals(other.teamid))
      return false;
    if (userid == null) {
      if (other.userid != null)
        return false;
    }
    else if (!userid.equals(other.userid))
      return false;
    return true;
  }


}
