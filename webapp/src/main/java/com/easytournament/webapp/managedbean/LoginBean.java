package com.easytournament.webapp.managedbean;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.easytournament.webapp.controller.UserControllerInterface;
import com.easytournament.webapp.entity.User;
import com.easytournament.webapp.security.PasswordEncripter;

@SuppressWarnings("serial")
@Named("loginBean")
@RequestScoped()
public class LoginBean implements Serializable {

  @EJB
  private UserControllerInterface userController;
  
  @Inject
  private AuthenticationBean authenticationBean;

  private String username;
  private String password;

  public String login() {

    User user = userController.loadUser(username);
    if (user != null) {
      try {
        String encryptedPassword = PasswordEncripter.encript(password,
            user.getPasswordsalt());
        if (encryptedPassword.equals(user.getPassword())) {
          authenticationBean.login(user);
          String requestedPage = authenticationBean.getRequestedPage();
          return requestedPage+"?faces-redirect=true";
        }
      }
      catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return "failure";
      }
    }

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Wrong username of password", "Wrong username of password"));
    return "failure";
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

}
