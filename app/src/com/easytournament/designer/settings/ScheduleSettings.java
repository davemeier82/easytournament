package com.easytournament.designer.settings;

import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;

import org.jdom.Element;

import com.easytournament.basic.model.dialog.SettingsDialogPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.settings.Settings;
import com.easytournament.basic.settings.SettingsTreeNode;
import com.easytournament.designer.gui.settings.ScheduleSetPanel;
import com.easytournament.designer.model.settings.ScheduleSetPModel;
import com.jgoodies.binding.beans.Model;

public class ScheduleSettings extends Model implements Settings {

  public static final String PROPERTY_SHOWREFREES = "showRefree";
  private ScheduleSetPModel pm;
  private ScheduleSetPanel panel;
  private static ScheduleSettings instance;

  protected boolean showRefrees = true;

  private ScheduleSettings() {}

  public static ScheduleSettings getInstance() {
    if (instance == null)
      instance = new ScheduleSettings();
    return instance;
  }

  private void init() {
    showRefrees = true;
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
    SettingsTreeNode node = new SettingsTreeNode(
        ResourceManager.getText(Text.SCHEDULE), this);
    return node;
  }

  @Override
  public JComponent getPanel() {

    if (panel == null) {
      pm = new ScheduleSetPModel();
      panel = new ScheduleSetPanel(pm);
    }
    return panel;
  }

  @Override
  public void read(Element xml) {
    if (xml == null) {
      init();
    }
    else {
      Element desEl = xml.getChild("schedule");
      this.showRefrees = desEl.getAttributeValue("showRefrees").equals("1");
    }
  }

  @Override
  public void save(Element xml) {
    Element e = new Element("schedule");
    e.setAttribute("showRefrees", this.showRefrees? "1" : "0");

    xml.addContent(e);
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
