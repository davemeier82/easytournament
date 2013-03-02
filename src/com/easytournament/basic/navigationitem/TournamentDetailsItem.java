package com.easytournament.basic.navigationitem;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jdom.Element;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.action.MainMenuAction;
import com.easytournament.basic.gui.EventsPanel;
import com.easytournament.basic.gui.RulesPanel;
import com.easytournament.basic.gui.TSportsSettingsPanel;
import com.easytournament.basic.gui.TournamentPanel;
import com.easytournament.basic.model.EventsPanelPModel;
import com.easytournament.basic.model.MainMenuPModel;
import com.easytournament.basic.model.RulesPanelPModel;
import com.easytournament.basic.model.TPanelPModel;
import com.easytournament.basic.model.TSSettingsPanelPModel;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEvent;
import com.easytournament.basic.xml.SportXMLHandler;
import com.easytournament.basic.xml.TournamentXMLHandler;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.easytournament.tournament.calc.Calculator;

public class TournamentDetailsItem extends NavigationItem {

  private JPanel panel;
  private Organizer organizer = Organizer.getInstance();
  private EventsPanelPModel eventPModel;

  public void init() {
    organizer.setSports(SportXMLHandler.readSports());
    organizer.resetTournament();
    panel = new JPanel(new BorderLayout());
    JTabbedPane tabbedPane = new JTabbedPane();
    TournamentPanel tpanel = new TournamentPanel(new TPanelPModel());
    tabbedPane.addTab(ResourceManager.getText(Text.GENERALINFO), new JScrollPane(tpanel));
    tabbedPane.addTab(ResourceManager.getText(Text.GAME_DUR_POINTS), new JScrollPane(
        new TSportsSettingsPanel(new TSSettingsPanelPModel())));
    tabbedPane.addTab(ResourceManager.getText(Text.RULES), new JScrollPane(new RulesPanel(
        new RulesPanelPModel())));
    eventPModel = new EventsPanelPModel();
    tabbedPane.addTab(ResourceManager.getText(Text.GAMEEVENTS), new JScrollPane(new EventsPanel(
        eventPModel)));
    panel.add(tabbedPane, BorderLayout.CENTER);
    super.init();
  }

  public NaviNode getNode() {
    return new NaviNode(ResourceManager.getText(Text.TOURN_DETAILS), this);
  }

  public JComponent getPanel() {
    return panel;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(NavTreeActions.SAVE.name())) {
      TournamentXMLHandler.save((Element)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(NavTreeActions.OPEN.name())) {
      organizer.resetTournament();
      TournamentXMLHandler.open((Element)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(NavTreeActions.NEW.name())) {
      GameEvent.CURRENT_MAX_ID = 0;
      organizer.resetTournament();
    }

  }

  public void activate() {
    super.activate();
    ArrayList<MainMenuAction> enable = new ArrayList<MainMenuAction>();
    enable.add(MainMenuAction.SAVE);
    enable.add(MainMenuAction.SAVEAS);
    enable.add(MainMenuAction.CLOSE);
    enable.add(MainMenuAction.EXPORT);
    enable.add(MainMenuAction.IMPORT);
    MainMenuPModel.getInstance().enableItems(enable);
  }

  public boolean deactivate() {
    if(eventPModel != null && eventPModel.isEventsChanged()){
      for(ScheduleEntry se :organizer.getCurrentTournament().getSchedules()){
        se.updateScore();
      }
      eventPModel.setEventsChanged(false);
    }
    
    for (AbstractGroup g : Organizer.getInstance().getCurrentTournament()
        .getPlan().getOrderedGroups()) {
      Calculator.calcTableEntries(g, false);
    } // TODO recalc only on changes
    super.deactivate();
    return true;
  }

  @Override
  public void update(Observable o, Object arg) {
    // do nothing

  }
}
