package easytournament.basic.logging;

import java.lang.Thread.UncaughtExceptionHandler;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;


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
