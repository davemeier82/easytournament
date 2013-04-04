package com.easytournament.basic.settings;

import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.jdom.Element;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.SplashScreen;
import com.easytournament.basic.gui.settings.GeneralSetPanel;
import com.easytournament.basic.model.dialog.SettingsDialogPModel;
import com.easytournament.basic.model.settings.GeneralSetPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;

public class GeneralSettings implements Settings {

  private GeneralSetPModel pm;
  private GeneralSetPanel panel;
  private static GeneralSettings instance;
  private ArrayList<String> availableLangs;
  private Locale newLocale;
  private boolean checkUpdate = true;

  private GeneralSettings() {
    init();
  }

  public static GeneralSettings getInstance() {
    if (instance == null)
      instance = new GeneralSettings();
    return instance;
  }

  private void init() {
    availableLangs = new ArrayList<String>();
    File currentDir = new File("res/text");
    String[] fileNames = currentDir.list();
    for (String s : fileNames) {
      if (s.startsWith("basictext_")) {
        availableLangs.add(s.substring(10, s.indexOf(".")));
      }
    }
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
        ResourceManager.getText(Text.GENERAL), this);
    return node;
  }

  @Override
  public JComponent getPanel() {

    if (panel == null) {
      pm = new GeneralSetPModel();
      panel = new GeneralSetPanel(pm);
    }
    return panel;
  }

  @Override
  public void read(Element xml) {
    if (xml == null) {
      String[] languages = new String[availableLangs.size()];
      HashMap<String,String> nameMap = new HashMap<String,String>();
      for (int i = 0; i < availableLangs.size(); i++) {
        languages[i] = (new Locale(availableLangs.get(i))).getDisplayName();
        nameMap.put(languages[i], availableLangs.get(i));
      }
      SplashScreen.getInstance().setVisible(false);
      String s = (String)JOptionPane.showInputDialog(null,
          ResourceManager.getText(Text.SELECT_LANGUAGE),
          ResourceManager.getText(Text.LANGUAGE), JOptionPane.QUESTION_MESSAGE,
          null, languages, "English");
      SplashScreen.getInstance().setVisible(true);
      if (s == null)
        System.exit(0);
      newLocale = new Locale(nameMap.get(s));

    }
    else {
      Element e = xml.getChild("general");
      Element languageEl = e.getChild("language");
      newLocale = new Locale(languageEl.getText());
      Element updateElement = e.getChild("checkupdate");
      if (updateElement != null)
        checkUpdate = updateElement.getText().equals("1");
    }
    ResourceManager.setLocale(newLocale);
    if (checkUpdate && hasNewVersion()) {
      SplashScreen.getInstance().setVisible(false);
      if (Desktop.isDesktopSupported()) {
        int answer = JOptionPane.showConfirmDialog(Organizer.getInstance()
            .getMainFrame(), ResourceManager.getText(Text.DOWNLOAD_NEW_APP_VERSION),
            ResourceManager.getText(Text.NEW_APP_VERSION),
            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
          Desktop desktop = Desktop.getDesktop();
          try {
            desktop.browse(new URI(MetaInfos.APP_WEBSITE));
          }
          catch (Exception ex) {// do nothing
          }
        }
      }
      else {
        JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.NEW_APP_VERSION_TEXT),
            ResourceManager.getText(Text.NEW_APP_VERSION),            
            JOptionPane.INFORMATION_MESSAGE);
      }
      SplashScreen.getInstance().setVisible(true);
    }
  }

  public boolean hasNewVersion() {

    URL url;
    try {
      url = new URL("http://easy-tournament.com/version.php");

      URLConnection conn = url.openConnection();

      conn.connect();
      // Get the response
      BufferedReader rd = new BufferedReader(new InputStreamReader(
          conn.getInputStream()));
      StringBuffer sb = new StringBuffer();
      String line;
      while ((line = rd.readLine()) != null) {
        sb.append(line);
      }
      rd.close();
      // System.out.println(sb.toString());
      String downloadableVersion = sb.toString();
      if(MetaInfos.compareVersionNr(MetaInfos.getVersionNr(), downloadableVersion)  < 0)
        return true;

    }
    catch (Exception ex) {
      // do nothing
    }
    return false;
  }

  @Override
  public void save(Element xml) {
    Element e = new Element("general");
    Element languageEl = new Element("language");
    languageEl.setText(newLocale.toString()); // FIXME: set to toLanguageTag()
                                              // if compiled with java 1.7
    e.addContent(languageEl);
    Element updateEl = new Element("checkupdate");
    updateEl.setText(checkUpdate?"1":"0");
    e.addContent(updateEl);
    xml.addContent(e);
  }

  public Locale getNewLocale() {
    return newLocale;
  }

  public void setNewLocale(Locale newLocale) {
    this.newLocale = newLocale;
  }

  public ArrayList<String> getAvailableLangs() {
    return availableLangs;
  }

  public boolean isCheckUpdate() {
    return checkUpdate;
  }

  public void setCheckUpdate(boolean checkUpdate) {
    this.checkUpdate = checkUpdate;
  }

}
