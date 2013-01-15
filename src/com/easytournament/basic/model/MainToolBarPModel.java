package com.easytournament.basic.model;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;


import com.easytournament.basic.action.MainMenuAction;
import com.easytournament.basic.gui.MainMenuObservable;
import com.jgoodies.binding.beans.Model;

public class MainToolBarPModel extends Model {

  private static final long serialVersionUID = 8702701616864554535L;
  private static MainToolBarPModel instance;

  private MainToolBarPModel() {}

  public static MainToolBarPModel getInstance() {
    if (instance == null) {
      instance = new MainToolBarPModel();
    }
    return instance;
  }

  public Action getAction(final MainMenuAction a) {
    AbstractAction action = new AbstractAction() {

      private static final long serialVersionUID = -1473172470239203847L;

      @Override
      public void actionPerformed(ActionEvent arg0) {
        MainMenuObservable.getInstance().setChanged();
        MainMenuObservable.getInstance().notifyObservers(a);
      }
    };
    action.putValue(Action.SHORT_DESCRIPTION, MainMenuAction.getText(a));
    action.putValue(Action.SMALL_ICON, MainMenuAction.getIcon(a));
    return action;
  }

}
