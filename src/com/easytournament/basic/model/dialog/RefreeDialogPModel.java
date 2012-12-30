package com.easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.model.AssistantsTabPanelPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.Refree;
import com.easytournament.basic.valueholder.Tournament;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;

public class RefreeDialogPModel extends Model {
  
  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;
  
  public static final String DISPOSE = "dispose";
  
  protected Tournament t = Organizer.getInstance().getCurrentTournament();
  
  protected Refree refree;
  protected Refree originalRefree;
  protected boolean newRefree = false;
  protected Refree mainRefree;
  
  public RefreeDialogPModel(Refree assistant, Refree refree) {
    this.originalRefree = assistant;
    this.mainRefree = refree;
    try {
      this.refree = (Refree)assistant.clone();
    }
    catch (CloneNotSupportedException e) {
      ErrorLogger.getLogger().throwing("RefreeDialogPModel", "constructor", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
  }
  
  public RefreeDialogPModel(Refree refree, boolean newAssistant) {
    if (newAssistant) {
      this.refree =  new Refree();
      this.newRefree = true;
      this.mainRefree = refree;
    }
    else {
      this.originalRefree = refree;

      try {
        this.refree = (Refree)refree.clone();
      }
      catch (CloneNotSupportedException e) {
        ErrorLogger.getLogger().throwing("RefreeDialogPModel", "constructor", e);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
    }
  }
  
  public RefreeDialogPModel() {
    this.refree =  new Refree();
    this.newRefree = true;
  }


  public Action getAction(int a) {
    switch (a) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            if(newRefree){
              if(mainRefree == null)
                t.getRefrees().add(refree);
              else
                mainRefree.getAssistants().add(refree);
            } else {
              originalRefree.setRefree(refree);
            }
            Organizer.getInstance().setSaved(false);
            RefreeDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            RefreeDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
    }
    return null;
  }

  public PersonPanelPModel getPersonPanelPModel() {
    return new PersonPanelPModel(this.refree);
  }

  public ValueModel getRefreeValueModel(String propertyName) {
    return new PropertyAdapter<Refree>(refree, propertyName);
  }

  public AssistantsTabPanelPModel getAssistantsTabPanelPModel() {
    return new AssistantsTabPanelPModel(refree);
  }

}
