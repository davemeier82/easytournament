package com.easytournament.basic.model.wizard;

import java.util.List;

import javax.swing.Action;
import javax.swing.JPanel;

import com.jgoodies.binding.beans.Model;

public abstract class WizardModel extends Model {
  
  public static final String DISPOSE = "dispose";
  
  public static final String PANEL_CHANGED = "panelchanged";
  public static final String BUTTONS_CHANGED = "buttonschanged";
  public static final String TITLE_CHANGED = "titlechanged";
  
  public static final String NEXT_MODEL_PRESSED = "nextmodel";
  public static final String PREVIOUS_MODEL_PRESSED = "previousmodel";
  public static final String CANCEL_PRESSED = "cancel";

  public abstract List<Action> getButtonActions();
  public abstract JPanel getPanel();
  public abstract String getTitel();
  public abstract WizardModel getNextModel();
  public abstract WizardModel getPreviousModel();
  public abstract boolean hasNextModel();
  public abstract boolean hasPreviousModel(); 
}
