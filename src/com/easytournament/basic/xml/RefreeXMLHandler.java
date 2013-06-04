package com.easytournament.basic.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.valueholder.Refree;
import com.jgoodies.common.collect.ArrayListModel;

public class RefreeXMLHandler {
  
  public static HashMap<String,Refree> teammap;
  public static HashMap<String,Refree> refmap;

  public static void save(Element xml) {
    com.easytournament.basic.valueholder.Tournament tourn = Organizer.getInstance()
        .getCurrentTournament();

    Element refrees = new Element("refrees");
    for (Refree r : tourn.getRefrees()) {
      Element refreeEl = writeRefree(r, false);      
      refrees.addContent(refreeEl);
    }
    xml.addContent(refrees);
  }
  
  public static void exportRefree(Element xml, ArrayList<Refree> refrees) {

    Element refreesEl = new Element("refrees");
    for (Refree r : refrees) {
      Element refreeEl = writeRefree(r, false);      
      refreesEl.addContent(refreeEl);
    }
    xml.addContent(refreesEl);
  }

  private static Element writeRefree(Refree r, boolean export) {
    Element refreeEl = new Element("refree");
    if(!export)
      refreeEl.setAttribute("id", r.getId()+"");
    PersonXMLHandler.writePerson(r, refreeEl);
    Element assistantsEl = writeAssistants(r.getAssistants(), export);      
    refreeEl.addContent(assistantsEl);

    return refreeEl;
  }
  
  public static void exportAssistants(Element xml, ArrayList<Refree> assistants){
    xml.addContent(writeAssistants(assistants, true));
  }

  private static Element writeAssistants(ArrayList<Refree> assistants, boolean export) {
    Element assisEl = new Element("assistants");  
    for(Refree a : assistants){
      Element assiEl = new Element("assistant");
      if(!export)
        assiEl.setAttribute("id", a.getId()+"");
      PersonXMLHandler.writePerson(a, assiEl);      
      assiEl.setAttribute("function", a.getFunction()==null?"":a.getFunction());   
      assisEl.addContent(assiEl);
    }
    return assisEl;
  }

  public static void open(Element xml) {
    Refree.CURRENT_MAX_ID = 0;
    
    ArrayListModel<Refree> refreelist = new ArrayListModel<Refree>();
    teammap = new HashMap<String,Refree>();
    refmap = new HashMap<String,Refree>();
    
    Element teamsElement = xml.getChild("refrees");
    List<Element> refreeEls = teamsElement.getChildren("refree");
    for (Element teamEl : refreeEls) {
      Refree r = readRefree(teamEl, false);      
      refreelist.add(r);
      teammap.put(teamEl.getAttributeValue("id"), r);
    }
    Organizer.getInstance().getCurrentTournament().setRefrees(refreelist);
  }
  
  public static ArrayListModel<Refree> importRefrees(Element xml){
    ArrayListModel<Refree> refreelist = new ArrayListModel<Refree>();
    Element teamsElement = xml.getChild("refrees");
    List<Element> refreeEls = teamsElement.getChildren("refree");
    for (Element teamEl : refreeEls) { 
      refreelist.add(readRefree(teamEl, true));
    }
    return refreelist;
  }
  
  private static Refree readRefree(Element refEl, boolean importing){
    Refree r = importing?new Refree(0):new Refree();
    PersonXMLHandler.readPerson(refEl, r);
    r.setAssistants(readAssistants(refEl, importing)); 
    if(!importing)
      refmap.put(refEl.getAttributeValue("id"), r);
    return r;
  }
  
  public static ArrayListModel<Refree> importAssistants(Element xml){
    return readAssistants(xml, true);
  }

  private static ArrayListModel<Refree> readAssistants(Element refEl, boolean importing) {
    ArrayListModel<Refree> assistants = new ArrayListModel<Refree>();
    Element assisEl =  refEl.getChild("assistants");
    List<Element> assiEls = assisEl.getChildren("assistant");
    for(Element aEl : assiEls){
      Refree r = importing?new Refree(0):new Refree();
      PersonXMLHandler.readPerson(aEl, r);
      r.setFunction(aEl.getAttributeValue("function"));
      assistants.add(r);
    }
    return assistants;
  }
}
