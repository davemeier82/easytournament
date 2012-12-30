package com.easytournament.designer.gui.toolbar;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.easytournament.designer.model.SchedulePanelPModel;


public class ScheduleToolBar extends JToolBar {
  
  private SchedulePanelPModel pm;
  
  public ScheduleToolBar(SchedulePanelPModel pm){
    this.pm = pm;
    this.init();
  }

  private void init() {
    this.setFloatable(false);
    this.addSeparator();

    JButton changeViewBtn = new JButton(pm.getAction(SchedulePanelPModel.CHANGE_TEAMVIEW_ACTION));
    changeViewBtn.setFocusPainted(false);
    this.add(changeViewBtn);
  }

}
