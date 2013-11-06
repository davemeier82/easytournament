package com.easytournament.webapp.entity.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Embeddable
public class FriendId implements Serializable {

  @NotNull
  private Integer userid1;
  @NotNull
  private Integer userid2;

  public FriendId() {
    super();
  }

  public FriendId(int userid1, int userid2) {
    super();
    this.userid1 = userid1;
    this.userid2 = userid2;
  }

  /**
   * @return the userid1
   */
  public Integer getUserid1() {
    return userid1;
  }

  /**
   * @param userid1
   *          the userid1 to set
   */
  public void setUserid1(Integer userid1) {
    this.userid1 = userid1;
  }

  /**
   * @return the userid2
   */
  public Integer getUserid2() {
    return userid2;
  }

  /**
   * @param userid2
   *          the userid2 to set
   */
  public void setUserid2(Integer userid2) {
    this.userid2 = userid2;
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
    result = prime * result + ((userid1 == null)? 0 : userid1.hashCode());
    result = prime * result + ((userid2 == null)? 0 : userid2.hashCode());
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
    FriendId other = (FriendId)obj;
    if (userid1 == null) {
      if (other.userid1 != null)
        return false;
    }
    else if (!userid1.equals(other.userid1))
      return false;
    if (userid2 == null) {
      if (other.userid2 != null)
        return false;
    }
    else if (!userid2.equals(other.userid2))
      return false;
    return true;
  }

}
