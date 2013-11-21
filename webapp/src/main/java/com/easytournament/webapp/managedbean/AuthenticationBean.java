package com.easytournament.webapp.managedbean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.easytournament.webapp.entity.User;

@SuppressWarnings("serial")
@Named("authBean")
@SessionScoped()
public class AuthenticationBean implements Serializable {

  public static final String AUTH_KEY = "user.id";

  private User user = null;

  private String requestedPage = "/app/home.jsf";

  public void login(User user) {
    this.user = user;
  }

  public String logout() {
    user = null;
    HttpSession session = (HttpSession)FacesContext.getCurrentInstance()
        .getExternalContext().getSession(false);
    session.invalidate();
    return "/login.jsf?faces-redirect=true";
  }

  public boolean isLoggedIn() {
    return user != null;
  }

  User getCurrentUser() {
    return user;
  }

  public void setRequestedPage(String requestedPage) {
    if (!requestedPage.endsWith("login.jsf")) {
      this.requestedPage = requestedPage;
    }
  }

  /**
   * @return the requestedPage
   */
  public String getRequestedPage() {
    return requestedPage;
  }

}
