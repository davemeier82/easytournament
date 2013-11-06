package com.easytournament.basic.model.settings;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.settings.GeneralSettings;
import com.jgoodies.binding.beans.Model;

public class GeneralSetPModel extends Model {

  private static final long serialVersionUID = 1L;
  public static final String PROPERTY_LANG = "lang";
  public static final String PROPERTY_LANGLABELS = "langLabels";
  public static final String PROPERTY_CHECKUPDATE = "checkUpdate";

  private String[] langLabels;
  private Map<String,Locale> langs;
  private String lang;
  private boolean checkUpdate;

  /**
   * Constructor
   */
  public GeneralSetPModel() {
    retrieveData();
  }

  /**
   * 
   */
  private void retrieveData() {
    retrieveLangData();
  }

  private void retrieveLangData() {
    langs = new HashMap<String,Locale>();
    GeneralSettings gset = GeneralSettings.getInstance();
    ArrayList<String> availLangs = gset.getAvailableLangs();
    langLabels = new String[availLangs.size()];
    for (int i = 0; i < availLangs.size(); i++) {
      Locale l = new Locale(availLangs.get(i));
      langLabels[i] = l.getDisplayName(ResourceManager.getLocale());
      langs.put(langLabels[i], l);
    }
    lang = gset.getNewLocale().getDisplayName(ResourceManager.getLocale());
    
    checkUpdate = gset.isCheckUpdate();
  }

  /**
   * save changed preferences
   */
  public void save() {
    GeneralSettings gset = GeneralSettings.getInstance();
    gset.setNewLocale(langs.get(lang));
    gset.setCheckUpdate(checkUpdate);
  }

  class LangAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
      showRestartDialog();
    }
  }

  /**
   * @return
   */
  public String getLang() {
    return lang;
  }

  /**
   * @param lang
   */
  public void setLang(String lang) {
    String old = this.lang;
    this.lang = lang;
    this.firePropertyChange(PROPERTY_LANG, old, this.lang);
    showRestartDialog();
  }

  /**
   * @return
   */
  public String[] getLangLabels() {
    return langLabels;
  }

  private void showRestartDialog() {
    String messageText = ResourceManager.getText(Text.RESTART_MESSAGE);
    JOptionPane.showMessageDialog(Organizer.getInstance().getMainFrame(),
        messageText, ResourceManager.getText(Text.RESTART),
        JOptionPane.INFORMATION_MESSAGE);
  }

  public boolean isCheckUpdate() {
    return checkUpdate;
  }

  public void setCheckUpdate(boolean checkUpdate) {
    this.checkUpdate = checkUpdate;
  }
  

}
