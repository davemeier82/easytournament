package com.easytournament.webapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.easytournament.webapp.entity.id.FriendId;

@SuppressWarnings("serial")
@Entity
@Table(name = "friend")
public class Friend implements Serializable {

  @EmbeddedId
  private FriendId id = new FriendId();

  @NotNull
  @Column(name = "accepted", columnDefinition = "BIT", length = 1)
  private Boolean accepted;

  public Friend() {
    super();
  }

  public Friend(Integer userid1, Integer userid2, Boolean accepted) {
    super();
    this.id.setUserid1(userid1);
    this.id.setUserid2(userid2);
    this.accepted = accepted;
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
   * @return the id
   */
  public FriendId getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(FriendId id) {
    this.id = id;
  }
}
