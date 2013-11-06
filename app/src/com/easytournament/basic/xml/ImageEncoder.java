package com.easytournament.basic.xml;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;
import org.jdom.Element;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;


public class ImageEncoder {

  public static void encodeImage(Element logoEl, Image logo) {
    BufferedImage bu = new BufferedImage(logo.getWidth(null),
        logo.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Graphics2D g2 = bu.createGraphics();
    g2.drawImage(logo, 0, 0, null);
    try {
      ImageIO.write(bu, "png", baos);
    }
    catch (IOException e) {
      ErrorLogger.getLogger().throwing("ImageEncoder", "encodeImage", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
    byte[] bytesOut = baos.toByteArray();
    String image = Base64.encodeBase64String(bytesOut);
    logoEl.setText(image);
  }

  public static ImageIcon decodeImage(Element picEl) {
    ImageIcon logo = null;
    try {
      byte[] bytesIn = Base64.decodeBase64(picEl.getText());
      ByteArrayInputStream bai = new ByteArrayInputStream(bytesIn);
      BufferedImage bu = ImageIO.read(bai);
      logo = new ImageIcon(bu);
    }
    catch (IOException e) {
      ErrorLogger.getLogger().throwing("ImageEncoder", "decodeImage", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
    return logo;
  }
}
