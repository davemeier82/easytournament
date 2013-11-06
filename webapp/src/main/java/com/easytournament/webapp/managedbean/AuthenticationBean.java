package com.easytournament.webapp.managedbean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.easytournament.webapp.entity.User;

@SuppressWarnings("serial")
@Named("authBean")
@SessionScoped()
public class AuthenticationBean implements Serializable {

  public static final String AUTH_KEY = "user.id";

  private User user = null;

  public void login(User user) {
    this.user = user;
  }

  public void logout() {
    user = null;
  }


  public boolean isLoggedIn() {
    return user != null;
  }

  User getCurrentUser(){
    return user;
  }

}
