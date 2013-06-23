package com.easytournament.basic.settings;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.jdom.Document;
import org.jdom.Element;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.xml.XMLHandler;

public class SettingsRegistry {

  private static final File userDir = new File(new JFileChooser()
      .getFileSystemView().getDefaultDirectory(), "/EasyTournament");

  private static ArrayList<Settings> settings = new ArrayList<Settings>() {
    {
      add(GeneralSettings.getInstance()); // TODO move to config file
    }
  };

  public static void register(Settings s) {
    settings.add(s);
    File settingsFile;
    boolean writeAccess = Organizer.getInstance().hasWriteAccess();
    if(writeAccess)
    {
      settingsFile = new File("settings.xml");
    }
    else
    {
      settingsFile = new File(userDir, "/settings.xml");
    }
    // FIXME hack because of early call of generalsettings
    if (settingsFile.exists()) {
      Document doc;
      try {
        doc = XMLHandler.openXMLDoc(settingsFile);
      }
      catch (FileNotFoundException e) {
        ErrorLogger.getLogger().throwing("XMLHandler", "openXMLDoc", e);
        ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
        return;
      }
      s.read(doc == null? null : doc.getRootElement());
    }
    else {
      if(!writeAccess)
      {
      if (!userDir.exists())
        userDir.mkdirs();
      }
      s.read(null);
    }
  }

  public static boolean unregister(Settings s) {
    return settings.remove(s);
  }

  public static void readSettings() {
    
    File settingsFile;
    boolean writeAccess = Organizer.getInstance().hasWriteAccess();
    if(writeAccess)
    {
      settingsFile = new File("settings.xml");
    }
    else
    {
      settingsFile = new File(userDir, "/settings.xml");
    }
    
    if (settingsFile.exists()) {
      Document doc;
      try {
        doc = XMLHandler.openXMLDoc(settingsFile);
      }
      catch (FileNotFoundException e) {
        ErrorLogger.getLogger().throwing("XMLHandler", "openXMLDoc", e);
        ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
        return;
      }
      // TODO do file check
      for (Settings s : settings) {
        s.read(doc == null? null : doc.getRootElement());
      }
    }
    else {
      if(!writeAccess)
      {
        if (!userDir.exists())
          userDir.mkdirs();
      }
      for (Settings s : settings) {
        s.read(null);        
      }
    }
  }

  public static void saveSettings() {
    Element filetype = new Element("filetype");
    filetype.setAttribute("application", MetaInfos.FILE_APPLICATION);
    filetype.setAttribute("type", MetaInfos.FILE_SETTINGFILETYPE);
    filetype.setAttribute("version", MetaInfos.getXMLFileVersion());

    for (Settings s : settings) {
      s.save(filetype);
    }
    File settingsFile;
    if(Organizer.getInstance().hasWriteAccess())
    {
      settingsFile = new File("settings.xml");
    }
    else
    {
      settingsFile = new File(userDir, "/settings.xml");
    }
    XMLHandler.saveXMLDoc(new Document(filetype), settingsFile);
  }

  public static ArrayList<Settings> getSettings() {
    return settings;
  }
}
