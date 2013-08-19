package com.easytournament.designer.navigationitem;

import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdom.Element;

import com.easytournament.basic.Organizer;
import com.easytournament.basic.action.MainMenuAction;
import com.easytournament.basic.export.ExportRegistry;
import com.easytournament.basic.gui.MainMenuObservable;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.model.MainMenuPModel;
import com.easytournament.basic.navigationitem.NavTreeActions;
import com.easytournament.basic.navigationitem.NaviNode;
import com.easytournament.basic.navigationitem.NavigationItem;
import com.easytournament.basic.print.TablePrinter;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.settings.SettingsRegistry;
import com.easytournament.designer.export.ScheduleExportable;
import com.easytournament.designer.gui.SchedulePanel;
import com.easytournament.designer.gui.toolbar.ScheduleToolBar;
import com.easytournament.designer.model.SchedulePanelPModel;
import com.easytournament.designer.settings.ScheduleSettings;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.designer.xml.ScheduleXMLHandler;
import com.easytournament.tournament.calc.Calculator;

public class ScheduleItem extends NavigationItem {
  protected SchedulePanelPModel spm;
  protected SchedulePanel panel;

  @Override
  public void init() {
    spm = new SchedulePanelPModel();
    panel = new SchedulePanel(spm);
    ExportRegistry.register(ResourceManager.getText(Text.SCHEDULE),
        new ScheduleExportable(this, this.panel));
    SettingsRegistry.register(ScheduleSettings.getInstance());
  }

  public void activate() {
    super.activate();
    ArrayList<MainMenuAction> enable = new ArrayList<MainMenuAction>();
    enable.add(MainMenuAction.SAVE);
    enable.add(MainMenuAction.SAVEAS);
    enable.add(MainMenuAction.CLOSE);
    enable.add(MainMenuAction.PRINT);
    enable.add(MainMenuAction.EXPORT);
    enable.add(MainMenuAction.IMPORT);
    MainMenuPModel.getInstance().enableItems(enable);
    toolbar = new ScheduleToolBar(spm);
  }

  public boolean deactivate() {
    super.deactivate();
    if (spm != null)
      spm.stopCellEditing();
      if(spm.isDataChanged()) {
      for (AbstractGroup g : Organizer.getInstance().getCurrentTournament()
          .getPlan().getOrderedGroups()) {
        Calculator.calcTableEntries(g, false);
      }
      spm.setDataChanged(false);
    }
    return true;
  }

  public NaviNode getNode() {
    return new NaviNode(ResourceManager.getText(Text.SCHEDULE), this);
  }

  public JComponent getPanel() {
    return new JScrollPane(panel);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(NavTreeActions.SAVE.name())) {
      ScheduleXMLHandler.save((Element)evt.getNewValue());
    }
    else if (evt.getPropertyName().equals(NavTreeActions.OPEN.name())) {
      ScheduleXMLHandler.open((Element)evt.getNewValue());
    }
  }

  @Override
  public void update(Observable o, Object val) {
    if (o instanceof MainMenuObservable) {
      if (val instanceof MainMenuAction) {
        MainMenuAction action = (MainMenuAction)val;
        switch (action) {
          case PRINT:
            print();
            break;
          default:
        }
      }
    }
  }

  private void print() {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName(ResourceManager.getText(Text.SCHEDULE));
    JTable table = panel.getTable();
    table.setRowSelectionAllowed(false);
    
    String subtitle = ResourceManager.getText(Text.SCHEDULE);
    if(!spm.getFilter().equals(ResourceManager.getText(Text.NOFILTER))){
    	subtitle += " " + spm.getFilter();
    }
    
    job.setPrintable(new TablePrinter(table, subtitle));
    if (job.printDialog()) {
      try {
        job.print();
      }
      catch (Exception e) {
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
    }
    table.setRowSelectionAllowed(true);
  }
}
