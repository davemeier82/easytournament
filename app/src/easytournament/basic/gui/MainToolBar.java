package easytournament.basic.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JToolBar;

import easytournament.basic.action.MainMenuAction;
import easytournament.basic.model.MainMenuPModel;
import easytournament.basic.model.MainToolBarPModel;


public class MainToolBar extends JToolBar implements PropertyChangeListener {
  
  private static final long serialVersionUID = 8881716847167183945L;
  private MainToolBarPModel mtpm;
  
  private HashMap<MainMenuAction, JButton> buttons = new HashMap<MainMenuAction,JButton>();
  
  public MainToolBar(MainToolBarPModel pm, MainMenuPModel mmpm){
    this.mtpm = pm;
    mmpm.addPropertyChangeListener(this);
    this.init();
  }

  private void init() {
    this.setFloatable(false);
    JButton newt = new JButton(mtpm.getAction(MainMenuAction.NEW));
    newt.setFocusPainted(false);
    //buttons.put(MainMenuAction.NEW, newt);
    this.add(newt);
    JButton open = new JButton(mtpm.getAction(MainMenuAction.OPEN));
    //buttons.put(MainMenuAction.OPEN, open);
    this.add(open);
    JButton save = new JButton(mtpm.getAction(MainMenuAction.SAVE));
    save.setFocusPainted(false);
    save.setEnabled(false);
    buttons.put(MainMenuAction.SAVE, save);
    this.add(save);
    JButton print = new JButton(mtpm.getAction(MainMenuAction.PRINT));
    print.setFocusPainted(false);
    print.setEnabled(false);
    buttons.put(MainMenuAction.PRINT, print);
    this.add(print);
    this.addSeparator();
    JButton undo = new JButton(mtpm.getAction(MainMenuAction.UNDO));
    undo.setFocusPainted(false);
    undo.setEnabled(false);
    buttons.put(MainMenuAction.UNDO, undo);
    this.add(undo);
    JButton redo = new JButton(mtpm.getAction(MainMenuAction.REDO));
    redo.setFocusPainted(false);
    redo.setEnabled(false);
    buttons.put(MainMenuAction.REDO, redo);
    this.add(redo);
  }
  
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(MainMenuPModel.ENABLE_ALL)) {
      for (JButton jmi : buttons.values()) {
        jmi.setEnabled(true);
      }
    }
    else if (evt.getPropertyName().equals(MainMenuPModel.DISABLE_ALL)) {
      for (JButton jmi : buttons.values()) {
        jmi.setEnabled(false);
      }
    }
    else if (evt.getPropertyName().equals(MainMenuPModel.ENABLE)) {
      Collection<MainMenuAction> actions = (Collection<MainMenuAction>)evt
          .getNewValue();
      for (MainMenuAction mma : actions) {
        JButton b = buttons.get(mma);
        if(b != null)
          b.setEnabled(true);
      }

    }
    else if (evt.getPropertyName().equals(MainMenuPModel.DISABLE)) {
      Collection<MainMenuAction> actions = (Collection<MainMenuAction>)evt
          .getNewValue();
      for (MainMenuAction mma : actions) {
        JButton b = buttons.get(mma);
        if(b != null)
          b.setEnabled(false);
      }
    }
  }

}
