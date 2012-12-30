package com.easytournament.basic;

import java.util.Observable;

public class MainMenuObservable extends Observable {

  private static MainMenuObservable instance;

  private MainMenuObservable() {

  }

  public static MainMenuObservable getInstance() {
    if (instance == null) {
      instance = new MainMenuObservable();
    }
    return instance;
  }

  public void setChanged() {
    super.setChanged();
  }
}
