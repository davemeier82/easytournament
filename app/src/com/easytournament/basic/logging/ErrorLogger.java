package com.easytournament.basic.logging;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;

public class ErrorLogger {

  private static Logger logger;

  private ErrorLogger() {}

  public static Logger getLogger() {
    if (logger == null) {
      logger = Logger.getLogger("ErrorLogger");

      FileHandler fh;
      try {
        File dir;
        if(Organizer.getInstance().hasWriteAccess())
        {
          dir = new File("logs");          
        }
        else {
          dir = new File(new JFileChooser()
          .getFileSystemView().getDefaultDirectory(), "/EasyTournament/logs");
        }

        if(!dir.exists()){
          dir.mkdirs();
        } 
        fh = new FileHandler(dir.getPath() + "/errorlog.txt", 1000000, 1, true);
        logger.addHandler(fh);
        logger.setLevel(Level.ALL);
        fh.setFormatter(new ErrorLogFormatter());
      }
      catch (Exception e) {
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }

    }
    return logger;
  }

}
