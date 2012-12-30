package com.easytournament.basic.logging;

import java.lang.Thread.UncaughtExceptionHandler;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;


public class UncaughtExceptionLogger implements UncaughtExceptionHandler {

  @Override
  public void uncaughtException(Thread t, Throwable ex) {
    ErrorLogger.getLogger().throwing("UncaughtExceptionLogger",
        "uncaughtException", ex);
    ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
        ResourceManager.getText(Text.ERROR), ex.toString(), ex);
    ed.setVisible(true);
    ex.printStackTrace();
  }

}
