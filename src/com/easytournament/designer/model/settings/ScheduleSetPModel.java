package com.easytournament.designer.model.settings;

import com.easytournament.designer.settings.ScheduleSettings;
import com.jgoodies.binding.beans.Model;

public class ScheduleSetPModel extends Model {

  public static final String PROPERTY_SHOWREFREES = "showRefrees";
  protected boolean showRefrees;

  public ScheduleSetPModel() {
    this.showRefrees = ScheduleSettings.getInstance().isShowRefrees();
  }
  
  public void save() {
    ScheduleSettings.getInstance().setShowRefrees(this.showRefrees);
  }

  public boolean isShowRefrees() {
    return showRefrees;
  }

  public void setShowRefrees(boolean showRefrees) {
    boolean old = this.showRefrees;
    this.showRefrees = showRefrees;
    this.firePropertyChange(PROPERTY_SHOWREFREES, old, this.showRefrees);
  }

}
