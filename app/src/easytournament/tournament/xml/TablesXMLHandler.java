package easytournament.tournament.xml;

import org.jdom.Element;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.tournament.calc.Calculator;


public class TablesXMLHandler {

  public static void open(Element xml) {

    ArrayListModel<AbstractGroup> groups  =  Organizer.getInstance().getCurrentTournament().getPlan()
            .getOrderedGroups();
    for(AbstractGroup g : groups) {
      Calculator.calcTableEntries(g, false);
    }
  }
}
