/* Main.java - EasyTournament StartUp Class
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

import com.easytournament.basic.gui.MainFrame;
import com.easytournament.basic.gui.SplashScreen;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.logging.UncaughtExceptionLogger;
import com.easytournament.basic.model.MainFramePModel;
import com.easytournament.basic.settings.SettingsRegistry;
import com.easytournament.basic.valueholder.Rule;

/**
 * The Main class to start EasyTournament
 * 
 * @author David Meier
 * @version 
 */
public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    final SplashScreen splashScreen = SplashScreen.getInstance();
    splashScreen.setProgressMax(100);
    splashScreen.setScreenVisible(true);
    splashScreen.setProgress("Setting up Look&Feel", 0);
    
    Writer out = null;
    try {
      File testFile = new File("test.txt");
      out = new OutputStreamWriter(new FileOutputStream(testFile));
      out.write("a");
      out.close();
      testFile.delete();
      Organizer.getInstance().setWriteAccess(true);
      
    } catch(Exception ex){//
    }
    finally {
      if(out!=null)
        try {
          out.close();
        }
        catch (IOException e) {//
        }
    }

    Thread.UncaughtExceptionHandler handler = new UncaughtExceptionLogger();
    Thread.currentThread().setUncaughtExceptionHandler(handler);

    try {
      JFrame.setDefaultLookAndFeelDecorated(true);
      JDialog.setDefaultLookAndFeelDecorated(true);

      UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel()); // ins
      Organizer.getInstance().setSubstance(true);
    }
    catch (UnsupportedLookAndFeelException e) {
      System.out.println("Substance failed to set");
      ErrorLogger.getLogger().throwing("Main", "main", e);
    }    

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Thread.UncaughtExceptionHandler handler = new UncaughtExceptionLogger();
        Thread.currentThread().setUncaughtExceptionHandler(handler);

        splashScreen.setProgress("Reading Settings", 10);
        SettingsRegistry.readSettings();

        splashScreen.setProgress("Initializing Rules", 20);
        Rule.initRules(); // initialize the Rule-Objects

        splashScreen.setProgress("Setting up MainFrame", 40);
        createAndShowGUI();
      }

      private void createAndShowGUI() {

        MainFramePModel pm = MainFramePModel.getInstance();
        MainFrame frame = new MainFrame(pm);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setSize(1100, 800);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (dim.width - frame.getWidth()) / 2;
        int y = (dim.height - frame.getHeight()) / 2;

        frame.setLocation(x, y);
        frame.setVisible(true);
        frame.addWindowListener(pm);
        Organizer.getInstance().setMainFrame(frame);
        splashScreen.setScreenVisible(false);
      }
    });

  }

}
