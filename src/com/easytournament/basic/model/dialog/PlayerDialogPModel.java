package com.easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Player;
import com.easytournament.basic.valueholder.Team;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;

public class PlayerDialogPModel extends Model {
  
  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;
  
  public static final String DISPOSE = "dispose";
  
  protected Player player;
  protected Player originalPlayer;
  protected boolean newPlayer = false;
  protected Team team;

  
  public PlayerDialogPModel(Player player) {
    this.originalPlayer = player;
    try {
      this.player = (Player)player.clone();
    }
    catch (CloneNotSupportedException e) {
      ErrorLogger.getLogger().throwing("PlayerDialogPModel", "constructor", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
  }
  
  public PlayerDialogPModel(Team team) {
    this.player =  new Player();
    this.newPlayer = true;
    this.team = team;
  }


  public Action getAction(int a) {
    switch (a) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            if(newPlayer){
              team.getPlayers().add(player);
            } else {
              originalPlayer.setPlayer(player);
            }

            PlayerDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            PlayerDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
    }
    return null;
  }

  public PersonPanelPModel getPersonPanelPModel() {
    return new PersonPanelPModel(this.player);
  }

  public ValueModel getPlayerValueModel(String propertyName) {
    return new PropertyAdapter<Player>(player, propertyName);
  }

}
