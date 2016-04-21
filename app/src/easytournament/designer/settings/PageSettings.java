package easytournament.designer.settings;

import java.awt.Color;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;

import org.jdom.Element;

import easytournament.basic.model.dialog.SettingsDialogPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.settings.Settings;
import easytournament.basic.settings.SettingsTreeNode;
import easytournament.designer.gui.settings.PageSettingsPanel;
import easytournament.designer.model.settings.PageSetPModel;
import easytournament.designer.util.comperator.Utils;


public class PageSettings implements Settings {

  private PageSetPModel pm;
  private PageSettingsPanel panel;
  private static PageSettings instance;

  protected Color pageColor = Color.WHITE;
  protected Color backgroundColor = Color.GRAY;
  protected boolean portrait;

  public static PageSettings getInstance() {
    if (instance == null)
      instance = new PageSettings();
    return instance;
  }

  private void init() {
    pageColor = Color.WHITE;
    backgroundColor = Color.GRAY;
    portrait = false;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (pm != null && evt.getPropertyName() == SettingsDialogPModel.PROPERTY_OK) {
      pm.save();
      pm = null;
      panel = null;
    }
    if (pm != null
        && evt.getPropertyName() == SettingsDialogPModel.PROPERTY_CANCEL) {
      pm = null;
      panel = null;
    }
  }

  @Override
  public SettingsTreeNode getNode() {
    SettingsTreeNode node = new SettingsTreeNode(ResourceManager.getText(Text.PAGE), this);
    return node;
  }

  @Override
  public JComponent getPanel() {

    if (panel == null) {
      pm = new PageSetPModel();
      panel = new PageSettingsPanel(pm);
    }
    return panel;
  }

  @Override
  public void read(Element xml) {
    if (xml == null) {
      init();
    }
    else {
      Element pEl = xml.getChild("page");
      portrait = pEl.getAttributeValue("portrait").equals("1");
      pageColor = Color.decode(pEl.getAttributeValue("pageColor"));
      backgroundColor = Color.decode(pEl.getAttributeValue("bgColor"));
    }

  }

  @Override
  public void save(Element xml) {
    Element e = new Element("page");
    e.setAttribute("portrait", portrait?"1":"0");
    e.setAttribute("pageColor", Utils.toHex(pageColor));
    e.setAttribute("bgColor", Utils.toHex(backgroundColor));
    xml.addContent(e);
  }

  public Color getPageColor() {
    return pageColor;
  }

  public void setPageColor(Color pageColor) {
    this.pageColor = pageColor;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public boolean isPortrait() {
    return portrait;
  }

  public void setPortrait(boolean portrait) {
    this.portrait = portrait;
  }

}
