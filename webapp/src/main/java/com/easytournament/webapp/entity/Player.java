package com.easytournament.webapp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Entity
public class Player implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  private String firstname;
  @NotNull
  private String lastname;
  
  private String street;
  
  private Integer zip;
  
  private String city;
  
  private String email;
  
  private String phone;
  
  private Integer userid;

  @NotNull
  @Column(name = "lastmodified", columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModified;

  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(Integer id) {
    this.id = id;
  }
  /**
   * @return the lastModified
   */
  public Date getLastModified() {
    return lastModified;
  }
  /**
   * @param lastModified the lastModified to set
   */
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }
  /**
   * @return the firstname
   */
  public String getFirstname() {
    return firstname;
  }
  /**
   * @param firstname the firstname to set
   */
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  /**
   * @return the lastname
   */
  public String getLastname() {
    return lastname;
  }
  /**
   * @param lastname the lastname to set
   */
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
  /**
   * @return the street
   */
  public String getStreet() {
    return street;
  }
  /**
   * @param street the street to set
   */
  public void setStreet(String street) {
    this.street = street;
  }
  /**
   * @return the zip
   */
  public Integer getZip() {
    return zip;
  }
  /**
   * @param zip the zip to set
   */
  public void setZip(Integer zip) {
    this.zip = zip;
  }
  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }
  /**
   * @param city the city to set
   */
  public void setCity(String city) {
    this.city = city;
  }
  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }
  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }
  /**
   * @return the phone
   */
  public String getPhone() {
    return phone;
  }
  /**
   * @param phone the phone to set
   */
  public void setPhone(String phone) {
    this.phone = phone;
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
  
}
