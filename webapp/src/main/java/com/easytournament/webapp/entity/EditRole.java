package com.easytournament.webapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Entity
@Table(name = "editrole")
public class EditRole implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  private String name;
  @NotNull
  @Column(name = "tournament", columnDefinition = "BIT", length = 1)
  private Boolean editTournament;
  @NotNull
  @Column(name = "team", columnDefinition = "BIT", length = 1)
  private Boolean editTeam;

  /**
	 * 
	 */
  public EditRole() {}

  /**
   * @return
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the editTournament
   */
  public Boolean getEditTournament() {
    return editTournament;
  }

  /**
   * @param editTournament the editTournament to set
   */
  public void setEditTournament(Boolean editTournament) {
    this.editTournament = editTournament;
  }

  /**
   * @return the editTeam
   */
  public Boolean getEditTeam() {
    return editTeam;
  }

  /**
   * @param editTeam the editTeam to set
   */
  public void setEditTeam(Boolean editTeam) {
    this.editTeam = editTeam;
  }

}
