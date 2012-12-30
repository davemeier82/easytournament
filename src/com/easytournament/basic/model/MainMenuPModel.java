package com.easytournament.basic.model;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.*;


import com.easytournament.basic.MainMenuObservable;
import com.easytournament.basic.action.MainMenuAction;
import com.jgoodies.binding.beans.Model;

/**
 * 
 * @author D. Meier
 */
public class MainMenuPModel extends Model {

  private static final long serialVersionUID = 1L;
  public static final String ENABLE_ALL = "enableAll";
  public static final String DISABLE_ALL = "disableALL";
  public static final String ENABLE = "enable";
  public static final String DISABLE = "disable";
  
  private static MainMenuPModel instance;
  
  

  private MainMenuPModel() { }

  public static MainMenuPModel getInstance() {
    if (instance == null) {
      instance = new MainMenuPModel();
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
    action.putValue(Action.NAME, MainMenuAction.getText(a));
    action.putValue(Action.SMALL_ICON, MainMenuAction.getIcon(a));
    return action;
  }

  public void enableAllItems(){
    this.firePropertyChange(ENABLE_ALL, 1, 2);
  }
  
  public void disableAllItems(){
    this.firePropertyChange(DISABLE_ALL, 1, 2);
  }
  
  public void enableItems(Collection<MainMenuAction> items){
    this.firePropertyChange(ENABLE, null, items);
  }
  
  public void disableItems(Collection<MainMenuAction> items){
    this.firePropertyChange(DISABLE, null, items);
  }


}
