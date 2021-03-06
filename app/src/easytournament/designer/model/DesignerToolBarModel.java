package easytournament.designer.model;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.jgoodies.binding.beans.Model;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import easytournament.basic.Organizer;
import easytournament.basic.action.MainMenuAction;
import easytournament.basic.gui.MainMenuObservable;
import easytournament.basic.gui.wizard.WizardDialog;
import easytournament.basic.model.wizard.TournamentWizardModel;
import easytournament.basic.resources.Icon;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.designer.TournamentViewer;
import easytournament.designer.navigationitem.DesignerItem;
import easytournament.designer.settings.DesignerSettings;

public class DesignerToolBarModel extends Model implements
    PropertyChangeListener {

  private static DesignerToolBarModel instance;

  public static final String PROPERTY_FONTSIZE = "fontSize";

  public static final String PROPERTY_FONTTYPE = "fontType";

  public static final int CHANGE_VIEW_ACTION = 0;

  public static final int TOURNAMENT_ASSISTANT_ACTION = 1;

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
      case TOURNAMENT_ASSISTANT_ACTION:
        AbstractAction tact = new AbstractAction("", ResourceManager.getIcon(Icon.ASSISTANT_ICON_SMALL)) {
          
          @Override
          public void actionPerformed(ActionEvent e) {
            int answer = JOptionPane.showConfirmDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager.getText(Text.LOOSE_DIAG_DATA_MSG),
                ResourceManager.getText(Text.LOOSE_DATA), JOptionPane.WARNING_MESSAGE);
            if (answer != JFileChooser.APPROVE_OPTION)
              return;
            
            WizardDialog wizard = new WizardDialog(null, new TournamentWizardModel(false), true);
            wizard.setVisible(true);
          }
        };
        tact.putValue(Action.SHORT_DESCRIPTION,
            ResourceManager.getText(Text.TOURNAMENT_ASSISTANT));
        return tact;
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
