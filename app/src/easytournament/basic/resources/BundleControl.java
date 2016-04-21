package easytournament.basic.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class BundleControl extends Control {
  
  protected File path;
  protected static int ID = 0;
  
  public BundleControl(File path) {
    super();
    this.path = path;
  }

  @Override
  public ResourceBundle newBundle(String baseName, Locale locale,
      String format, ClassLoader loader, boolean reload)
      throws IllegalAccessException, InstantiationException, IOException {

    if (!format.equals("java.properties")) {
      return null;
    }

    String bundleName = toBundleName(baseName, locale);
    ResourceBundle bundle = null;

    InputStreamReader reader = null;
    FileInputStream fis = null;
    try {

      File file = new File(this.path, bundleName+".properties");

      if (file.isFile()) { // Also checks for existance
        fis = new FileInputStream(file);
        reader = new InputStreamReader(fis, Charset.forName("iso-8859-1"));
        bundle = new PropertyResourceBundle(reader);
      }
    }
    finally {
      if (reader != null)
        reader.close();
      if (fis != null)
        fis.close();
    }
    return bundle;
  }

}
