package com.easytournament.basic.util.filechooser;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

public class Utils {
  public final static String jpeg = "jpeg";
  public final static String jpg = "jpg";
  public final static String gif = "gif";
  public final static String tiff = "tiff";
  public final static String tif = "tif";
  public final static String png = "png";

  /*
   * Get the extension of a file.
   */
  public static String getExtension(File f) {
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 &&  i < s.length() - 1) {
          ext = s.substring(i+1).toLowerCase();
      }
      return ext;
  }

  /** Returns an ImageIcon, or null if the path was invalid. */
  protected static ImageIcon createImageIcon(String path) {
      java.net.URL imgURL = Utils.class.getResource(path);
      if (imgURL != null) {
          return new ImageIcon(imgURL);
      }
    System.err.println("Couldn't find file: " + path);//TODO: error/logging ?
    return null;
  }
  
  public static ImageIcon getScaledImage(ImageIcon image, double scale)  
  {  
      int w = (int)(image.getIconWidth() * scale);  
      int h = (int)(image.getIconHeight() * scale);  
      BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);  
      Graphics2D g2 = bi.createGraphics();  
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,  
                          RenderingHints.VALUE_INTERPOLATION_BICUBIC);  
      AffineTransform at = AffineTransform.getScaleInstance(scale, scale);  
      g2.drawImage(image.getImage(), at, null);  
      g2.dispose();  
      return new ImageIcon(bi);  
  }
}
