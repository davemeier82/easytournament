package com.easytournament.basic.navigationitem;

import java.beans.PropertyChangeListener;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.easytournament.basic.model.MainMenuPModel;



public abstract class NavigationItem implements PropertyChangeListener, Observer {

  protected boolean active;
  protected JToolBar toolbar;

  public void init() { 
    toolbar = new JToolBar();
    toolbar.setFloatable(false);
  }
  
  public JToolBar getToolBar(){
    return toolbar;
  }

  public void activate() {
    MainMenuPModel.getInstance().disableAllItems();
    this.active = true;
  }

  public boolean deactivate() {
    this.active = false;
    return true;
  }

  public abstract NaviNode getNode();

  public abstract JComponent getPanel();

}
