package com.easytournament.basic.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.valueholder.HistoryFile;
import com.easytournament.basic.valueholder.Sport;


public class HistoryXMLHandler {  

  public static ArrayList<HistoryFile> readHistory(Document doc) {
    ArrayList<HistoryFile> history = new ArrayList<HistoryFile>();

    if (doc != null) {
      Element filesEl = doc.getRootElement();
      List<Element> filesEls = filesEl.getChildren("file");
      HashMap<String,Sport> sportMap = Organizer.getInstance().getSports();
      for (Element file : filesEls) {
        String path = file.getAttributeValue("path");
        File f = new File(path);
        if (f.exists()) {
          HistoryFile hf = new HistoryFile();
          hf.setName(file.getAttributeValue("name"));
          hf.setSportid(file.getAttributeValue("sportid"));
          Sport s = sportMap.get(hf.getSportid());
          if (s == null)
            hf.setSport(file.getAttributeValue("sport"));
          else
            hf.setSport(s.getName());
          hf.setLastModified(new Date(f.lastModified()));
          hf.setPath(f);
          history.add(hf);
        }
      }
    }
    return history;
  }

  public static Element writeHistory(ArrayList<HistoryFile> history) {
    Element files = new Element("files");
    for (HistoryFile hf : history) {
      Element hfEl = new Element("file");
      hfEl.setAttribute("name", hf.getName());
      hfEl.setAttribute("sport", hf.getSport());
      hfEl.setAttribute("sportid", hf.getSportid());
      hfEl.setAttribute("path", hf.getPath().getAbsolutePath());

      files.addContent(hfEl);
    }
    return files;
  }
}
