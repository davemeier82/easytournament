package easytournament.designer.util.comperator;

import java.awt.Color;

public class Utils {
  public static String toHex(Color c) {
    String rgb = Integer.toHexString(c.getRGB());
    return "#" + rgb.substring(2, rgb.length());
  }
}
