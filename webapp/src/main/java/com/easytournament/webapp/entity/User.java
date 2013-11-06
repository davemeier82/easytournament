package com.easytournament.webapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * Benutzer Hibernate Entity für DB-Tabelle user
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")})
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  private String username;
  @NotNull
  private String firstname;
  @NotNull
  private String lastname;
  @NotNull
  private String password;
  @NotNull
  private String activationcode;
  @NotNull
  private String email;

  private byte[] passwordsalt;

  @OneToMany(mappedBy="user", fetch=FetchType.LAZY)
  private List<UserTournament> userTournament = new ArrayList<UserTournament>();

  /**
	 * 
	 */
  public User() {}

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
   * @return
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * @param firstname
   */
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  /**
   * @return
   */
  public String getLastname() {
    return lastname;
  }

  /**
   * @param lastname
   */
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  /**
   * @return
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return
   */
  public String getActivationcode() {
    return activationcode;
  }

  /**
   * @param activationcode
   */
  public void setActivationcode(String activationcode) {
    this.activationcode = activationcode;
  }

  /**
   * @return
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the passwordsalt
   */
  public byte[] getPasswordsalt() {
    return passwordsalt;
  }

  /**
   * @param passwordsalt
   *          the passwordsalt to set
   */
  public void setPasswordsalt(byte[] passwordsalt) {
    this.passwordsalt = passwordsalt;
  }

  /**
   * @return the userTournament
   */
  public List<UserTournament> getUserTournament() {
    return userTournament;
  }

  /**
   * @param userTournament the userTournament to set
   */
  public void setUserTournament(List<UserTournament> userTournament) {
    this.userTournament = userTournament;
  }
  
  public UserTournament addTournament(Tournament t, Integer roleid) {
    UserTournament ut = new UserTournament(roleid, this, t);
    this.getUserTournament().add(ut);
    t.getUserTournament().add(ut);
    return ut;
  }

  public void setUser(User user) {
    this.activationcode = user.activationcode;
    this.email = user.email;
    this.password = user.password;
    this.firstname = user.firstname;
    this.lastname = user.lastname;
    this.username = user.username;
    this.passwordsalt = user.passwordsalt;
    this.id = user.id;
    this.userTournament = user.userTournament;
  }
}
