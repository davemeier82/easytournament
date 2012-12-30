package com.easytournament.designer.model;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.easytournament.basic.MainMenuObservable;
import com.easytournament.basic.action.MainMenuAction;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.TournamentViewer;
import com.easytournament.designer.navigationitem.DesignerItem;
import com.easytournament.designer.settings.DesignerSettings;
import com.jgoodies.binding.beans.Model;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public class DesignerToolBarModel extends Model implements
    PropertyChangeListener {

  private static DesignerToolBarModel instance;

  public static final String PROPERTY_FONTSIZE = "fontSize";

  public static final String PROPERTY_FONTTYPE = "fontType";

  public static final int CHANGE_VIEW_ACTION = 0;

  private List<String> fonts = new ArrayList<String>();

  private String[] fontSizes = new String[] {"6pt", "8pt", "9pt", "10pt",
      "12pt", "14pt", "18pt", "24pt", "30pt", "36pt", "48pt", "60pt"};

  private String fontSize;
  private String fontType;

  private DesignerToolBarModel() {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    fonts.addAll(Arrays.asList(env.getAvailableFontFamilyNames()));
    fontType = DesignerSettings.getInstance().getGroupFontType();
    fontSize = DesignerSettings.getInstance().getGroupFontSize();
    DesignerSettings.getInstance().addPropertyChangeListener(this);
  }

  public static DesignerToolBarModel getInstance() {
    if (instance == null) {
      instance = new DesignerToolBarModel();
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

  public Action getAction(int a) {
    switch (a) {
      case CHANGE_VIEW_ACTION:
        AbstractAction act = new AbstractAction("",
            ResourceManager.getIcon(Icon.CHANGE_VIEW_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            DesignerItem.getTournamentViewer().setTeamView(
                !DesignerItem.getTournamentViewer().isTeamView());

          }
        };
        act.putValue(Action.SHORT_DESCRIPTION,
            ResourceManager.getText(Text.SWITCH_TEAM_DESIGN_VIEW));
        return act;
    }
    return null;
  }

  public String getFontSize() {
    return fontSize;
  }

  public void setFontSize(String fontSize) {
    String old = this.fontSize;
    this.fontSize = fontSize;
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    if (fontSize != null && graph != null && !graph.isSelectionEmpty()) {
      graph.setCellStyles(mxConstants.STYLE_FONTSIZE,
          fontSize.replace("pt", ""));
    }
    this.firePropertyChange(PROPERTY_FONTSIZE, old, this.fontSize);
  }

  public String[] getFontSizes() {
    return fontSizes;
  }

  public String getFontType() {
    return fontType;
  }

  public void setFontType(String fontType) {
    String old = this.fontType;
    this.fontType = fontType;
    mxGraph graph = TournamentViewer.getGraphComponent().getGraph();
    if (fontType != null && graph != null && !graph.isSelectionEmpty()) {
      graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, this.fontType);
    }

    this.firePropertyChange(PROPERTY_FONTTYPE, old, this.fontSize);
  }

  public List<String> getFonts() {
    return fonts;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == DesignerSettings.PROPERTY_GROUP_FONTSIZE) {
      this.setFontSize((String)evt.getNewValue());
    }
    else if (evt.getPropertyName() == DesignerSettings.PROPERTY_GROUP_FONTTYPE) {
      this.setFontType((String)evt.getNewValue());
    }
  }

}
