package easytournament.basic.resources;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.rule.RuleType;


public class ResourceManager {

  private static Locale locale = Locale.ROOT;
  private static ResourceBundle rb = ResourceBundle.getBundle("basictext",
      locale, new BundleControl(new File("res/text")));
  private static ResourceBundle iconRb = ResourceBundle.getBundle("basicicon",
      locale, new BundleControl(new File("res/icons")));
  private static ResourceBundle pathRb = ResourceBundle.getBundle("path",
      locale, new BundleControl(new File("res/others")));
  private static ResourceBundle helpRb = ResourceBundle.getBundle("help",
      locale, new BundleControl(new File("help")));
  public static String iconsDir = new java.io.File("").getAbsolutePath()
      + "/res/icons/";
  public static String pathDir = new java.io.File("").getAbsolutePath()
      + "/res/others/";
  public static String helpDir = (new File("").toURI())+"help/";

  public static void setLocale(Locale l) {
    locale = l;
    rb = ResourceBundle.getBundle("basictext", l, new BundleControl(new File(
        "res/text")));
    helpRb = ResourceBundle.getBundle("help",
        l, new BundleControl(new File("help")));
    pathRb = ResourceBundle.getBundle("path",
        l, new BundleControl(new File("res/others")));
    iconRb = ResourceBundle.getBundle("basicicon",
        locale, new BundleControl(new File("res/icons")));
  }

  public static Locale getLocale() {
    return locale;
  }

  public static String getText(Text resid) {
    return rb.getString(resid.name());
  }
  
  public static String getRuleName(RuleType r) {
    return rb.getString(r.name());
  }

  public static ImageIcon getIcon(Icon resid) {
    try {
      String imgURL = iconsDir + iconRb.getString(resid.name());
      return new ImageIcon(imgURL);
    }
    catch (Exception e) {
      ErrorLogger.getLogger().throwing("ResourceManager", "getIcon", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
    return null;
  }

  public static String getPath(Path resid) {
    return pathDir + pathRb.getString(resid.name());
  }
  
  public static URI getHelp(Help helpid) throws URISyntaxException {
    return new URI(helpDir+helpRb.getString(helpid.name()));
  }

}
