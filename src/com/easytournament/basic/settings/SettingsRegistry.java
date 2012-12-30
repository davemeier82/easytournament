package com.easytournament.basic.settings;

import java.io.File;

import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.jdom.Document;
import org.jdom.Element;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.Organizer;
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
    boolean writeAccess = Organizer.getInstance().isWriteAccess();
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
      Document doc = XMLHandler.openXMLDoc(settingsFile);
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
    boolean writeAccess = Organizer.getInstance().isWriteAccess();
    if(writeAccess)
    {
      settingsFile = new File("settings.xml");
    }
    else
    {
      settingsFile = new File(userDir, "/settings.xml");
    }
    
    if (settingsFile.exists()) {
      Document doc = XMLHandler.openXMLDoc(settingsFile);
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
    if(Organizer.getInstance().isWriteAccess())
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
