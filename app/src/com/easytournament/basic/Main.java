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
   * Main method to start EasyTournament
   * @param args command line arguments
   */
  public static void main(String[] args) {
    // create the splash screen
    final SplashScreen splashScreen = SplashScreen.getInstance();
    splashScreen.setProgressMax(100);
    splashScreen.setScreenVisible(true);
    splashScreen.setProgress("Setting up Look&Feel", 0);
    
    Writer out = null;
    try {
      //FIXME: hack to test if we have write access.
      File testFile = new File("test.txt");
      out = new OutputStreamWriter(new FileOutputStream(testFile));
      out.write("a");
      out.close();
      testFile.delete();
      Organizer.getInstance().setWriteAccess(true);
      
    } catch(Exception ex){
      // no write access
    }
    finally {
      // close the file in any case
      if(out!=null)
        try {
          out.close();
        }
        catch (IOException e) {//
        }
    }

    // setup the exception-handler to log all exceptions
    Thread.UncaughtExceptionHandler handler = new UncaughtExceptionLogger();
    Thread.currentThread().setUncaughtExceptionHandler(handler);

    try {
      // Apply the look and feel to each frame and dialog
      JFrame.setDefaultLookAndFeelDecorated(true);
      JDialog.setDefaultLookAndFeelDecorated(true);

      UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
      Organizer.getInstance().setSubstance(true);
    }
    catch (UnsupportedLookAndFeelException e) {
      // setting up of the look and feel failed.
      // we are using the default java look and feel
      System.out.println("Substance failed to set");
      ErrorLogger.getLogger().throwing("Main", "main", e);
    }    

    // start the main window in a new thread
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        // add an exception handler to the main window thread to log all exceptions
        Thread.UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionLogger();
        Thread.currentThread().setUncaughtExceptionHandler(exceptionHandler);

        // read the settings
        splashScreen.setProgress("Reading Settings", 10);
        SettingsRegistry.readSettings();

        // initialize the Rule-Objects
        splashScreen.setProgress("Initializing Rules", 20);
        Rule.initRules(); 

        // create and show the main window
        splashScreen.setProgress("Setting up MainFrame", 40);
        createAndShowGUI();
      }

      /**
       * creates and shows the main window
       */
      private void createAndShowGUI() {

        // create the model and the main window
        MainFramePModel pm = MainFramePModel.getInstance();
        MainFrame frame = new MainFrame(pm);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setSize(1100, 800);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // place the main window in the middle of the screen
        int x = (dim.width - frame.getWidth()) / 2;
        int y = (dim.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        
        frame.setVisible(true);
        // add model to connect it to the window actions
        frame.addWindowListener(pm);
        
        // Add the main instance to the organizer for easy
        // access from everywhere in the application
        Organizer.getInstance().setMainFrame(frame);
        
        // hide the splash screen
        splashScreen.setScreenVisible(false);
      }
    });
  }
}
