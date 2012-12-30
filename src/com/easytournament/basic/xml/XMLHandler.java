package com.easytournament.basic.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;

public class XMLHandler {

  private static final String ENCODING = "iso-8859-1";

  public static Document openXMLDoc(File filename) {
    Document doc = null;

    if (filename != null
        && !(filename.getPath().toLowerCase().endsWith(".xml") || filename
            .getPath().toLowerCase().endsWith(".ett"))) {
      filename = new File(filename.getPath() + ".xml");
    }

    try {
      doc = new SAXBuilder().build(filename);
    }
    catch (Exception e) {
      ErrorLogger.getLogger().throwing("XMLHandler", "openXMLDoc", e);
      ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }

    return doc;
  }

  public static void saveXMLDoc(Document document, File filename) {

    Format format = Format.getPrettyFormat();
    format.setEncoding(ENCODING);
    XMLOutputter out = new XMLOutputter(format);
    FileWriter writer;
    try {
      writer = new FileWriter(filename);
      out.output(document, writer);
      writer.flush();
      writer.close();
    }
    catch (IOException e) {
      ErrorLogger.getLogger().throwing("XMLHandler", "saveXMLDoc", e);
      e.printStackTrace();
    }
  }

}
