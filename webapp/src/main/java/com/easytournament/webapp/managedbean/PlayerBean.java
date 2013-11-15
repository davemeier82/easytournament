package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.easytournament.webapp.controller.CountryControllerInterface;
import com.easytournament.webapp.controller.PlayerControllerInterface;
import com.easytournament.webapp.entity.Country;
import com.easytournament.webapp.entity.Player;
import com.easytournament.webapp.entity.User;

@SuppressWarnings("serial")
@Named("playerBean")
@RequestScoped
public class PlayerBean implements Serializable {

  @Inject
  private AuthenticationBean authenticationBean;
  
  @EJB
  private PlayerControllerInterface playerController;
  
  @EJB
  private CountryControllerInterface countryController;
  
  @Size(min = 1, max = 45)
  private String firstname;
  
  @Size(min = 0, max = 45)
  private String lastname;
  
  @Size(min = 0, max = 255)
  private String street;
  @Min(0)
  @Max(999999999)
  private Integer zip;
  
  @Size(min = 0, max = 45)
  private String city; 
  
  private List<SelectItem> countries = new ArrayList<SelectItem>();
  private Integer country = -1;
  
  @Size(min = 0, max = 45)
  private String email;
  
  @Size(min = 0, max = 45)
  private String phone; 
  
  public void create() {
    
    Player player =  new Player();
    player.setFirstname(firstname);
    player.setLastname(lastname);
    player.setStreet(street);
    player.setZip(zip);
    player.setCity(city);
    player.setCountry(country);
    player.setEmail(email);
    player.setPhone(phone);
    player.setLastModified(new Date());

    User currentUser = authenticationBean.getCurrentUser();
    
    playerController.addPlayerToUser(player, currentUser);
  }
  
  @PostConstruct
  public void initCountriesList(){
    List<Country> allcountries = countryController.loadCountries();
    for(Country s : allcountries){
      countries.add(new SelectItem(s.getId(), s.getName()));
    }
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
   * @return the countries
   */
  public List<SelectItem> getCountries() {
    return countries;
  }

  /**
   * @param countries the countries to set
   */
  public void setCountries(List<SelectItem> countries) {
    this.countries = countries;
  }

  /**
   * @return the country
   */
  public Integer getCountry() {
    return country;
  }

  /**
   * @param country the country to set
   */
  public void setCountry(Integer country) {
    this.country = country;
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

}
