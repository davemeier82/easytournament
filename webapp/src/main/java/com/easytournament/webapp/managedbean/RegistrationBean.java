package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import com.easytournament.webapp.controller.UserControllerInterface;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.security.PasswordEncripter;
import com.easytournament.webapp.validation.Email;

@SuppressWarnings("serial")
@Named("regBean")
@RequestScoped
public class RegistrationBean implements Serializable {

  @EJB
  private UserControllerInterface userController;
  
  @Size(min = 3, max = 32)
  private String username;
  @Size(min = 1, max = 32)
  private String firstname;
  @Size(min = 1, max = 32)
  private String lastname;
  @Size(min = 8, max = 32)
  private String password;
  @Size(min = 8, max = 32)
  private String confirmpassword;
  @Size(min = 3, max = 45)
  @Email(message = "Email is not a valid")
  private String email;
  @Size(min = 3, max = 45)
  private String confirmemail;

  public String register() {

    User newUser = new User();
    newUser.setUsername(username);
    newUser.setFirstname(firstname);
    newUser.setLastname(lastname);
    newUser.setEmail(email);

    SecureRandom random;
    try {
      random = SecureRandom.getInstance("SHA1PRNG");

      // Salt generation 64 bits long
      byte[] salt = new byte[8];
      random.nextBytes(salt);
      newUser.setPasswordsalt(salt);
      newUser.setPassword(PasswordEncripter.encript(password, salt));
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return "failure";
    }
    String activationcode = Long.toString(Math.abs(random.nextLong()), 36);
    newUser.setActivationcode(activationcode);

    userController.saveUser(newUser);

    return "success";
  }

  @AssertTrue(message = "Username is already taken.")
  public boolean isUsernameAvailable() {
    return !userController.isUsernameExist(username);
  }

  @AssertTrue(message = "Eamil is already taken.")
  public boolean isEmailAvailable() {
    return !userController.isEmailExist(email);
  }

  @AssertTrue(message = "Different passwords entered!")
  public boolean isPasswordsEquals() {
    return password.equals(confirmpassword);
  }

  @AssertTrue(message = "Different emails entered!")
  public boolean isEmailsEquals() {
    return email.equals(confirmemail);
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   *          the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the firstname
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * @param firstname
   *          the firstname to set
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
   * @param lastname
   *          the lastname to set
   */
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   *          the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the confirmpassword
   */
  public String getConfirmpassword() {
    return confirmpassword;
  }

  /**
   * @param confirmpassword
   *          the confirmpassword to set
   */
  public void setConfirmpassword(String confirmpassword) {
    this.confirmpassword = confirmpassword;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   *          the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the confirmemail
   */
  public String getConfirmemail() {
    return confirmemail;
  }

  /**
   * @param confirmemail
   *          the confirmemail to set
   */
  public void setConfirmemail(String confirmemail) {
    this.confirmemail = confirmemail;
  }
}
