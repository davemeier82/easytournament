package easytournament.basic.util.filenameFilter;

import java.io.File;

public class FilenameFilter implements java.io.FilenameFilter {

  private String filename;

  public FilenameFilter(String filename) {
    super();
    this.filename = filename;
  }

  public boolean accept(File dir, String name) {
    if (new File(dir, name).isDirectory()) {
      return false;
    }
    name = name.toLowerCase();
    return name.endsWith(filename);
  }
}
