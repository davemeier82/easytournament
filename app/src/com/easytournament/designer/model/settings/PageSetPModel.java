package com.easytournament.designer.model.settings;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.settings.PageSettings;
import com.jgoodies.binding.beans.Model;


public class PageSetPModel extends Model {

  public static final int BACKGROUND_COLOR_ACTION = 0;
  public static final int PAGE_COLOR_ACTION = 1;
  public static final String PROPERTY_PORTRAIT = "portrait";

  protected PageSettings settings = PageSettings.getInstance();
  
  protected Color pageColor;
  protected Color backgroundColor;
  protected boolean portrait;
  
  public PageSetPModel(){
    init();
  }

  private void init() { 
    portrait = settings.isPortrait();    
  }

  public void save() {
    settings.setPortrait(portrait);
    settings.setPageColor(pageColor);
    settings.setBackgroundColor(backgroundColor);    
  }
  

  public Action getColorAction(int action) {
    AbstractAction a = null;

    switch (action) {
      case PAGE_COLOR_ACTION: {
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager.getText(Text.FONTCOLOR_MENU), (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              pageColor = newColor;
            }

          }
        };
        pageColor = settings.getPageColor();
        a.putValue("FG", pageColor);
        break;
      }
      case BACKGROUND_COLOR_ACTION: {
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager.getText(Text.FONTCOLOR_MENU), (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              backgroundColor = newColor;
            }

          }
        };
        backgroundColor = settings.getBackgroundColor();
        a.putValue("FG", backgroundColor);
        break;
      }
    }
    return a;
  }


  public boolean isPortrait() {
    return portrait;
  }


  public void setPortrait(boolean portrait) {
    boolean old = this.portrait;
    this.portrait = portrait;
    this.firePropertyChange(PROPERTY_PORTRAIT, old, this.portrait);
  }

}
