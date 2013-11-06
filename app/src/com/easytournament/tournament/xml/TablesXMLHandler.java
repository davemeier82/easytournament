package com.easytournament.tournament.xml;

import org.jdom.Element;


import com.easytournament.basic.Organizer;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.tournament.calc.Calculator;
import com.jgoodies.common.collect.ArrayListModel;


public class TablesXMLHandler {

  public static void open(Element xml) {

    ArrayListModel<AbstractGroup> groups  =  Organizer.getInstance().getCurrentTournament().getPlan()
            .getOrderedGroups();
    for(AbstractGroup g : groups) {
      Calculator.calcTableEntries(g, false);
    }
  }
}
