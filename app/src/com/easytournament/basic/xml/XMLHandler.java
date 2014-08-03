package com.easytournament.basic.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

  private static final String ENCODING = "UTF-8";

  public static Document openXMLDoc(File filename) throws FileNotFoundException {
    Document doc = null;

    if (filename != null
        && !(filename.getPath().toLowerCase().endsWith(".xml") || filename
            .getPath().toLowerCase().endsWith(".ett"))) {
      filename = new File(filename.getPath() + ".xml");
    }

    try {
      doc = new SAXBuilder().build(filename);
    }
    catch (FileNotFoundException e) {
      throw e;
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
    FileOutputStream fileOutput;
    try {
      fileOutput = new FileOutputStream(filename);
      out.output(document, fileOutput);
      fileOutput.flush();
      fileOutput.close();
    }
    catch (IOException e) {
      ErrorLogger.getLogger().throwing("XMLHandler", "saveXMLDoc", e);
      e.printStackTrace();
    }
  }

}
