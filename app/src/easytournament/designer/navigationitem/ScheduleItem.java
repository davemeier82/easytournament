package easytournament.designer.navigationitem;

import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdom.Element;

import easytournament.basic.Organizer;
import easytournament.basic.action.MainMenuAction;
import easytournament.basic.export.ExportRegistry;
import easytournament.basic.gui.MainMenuObservable;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.model.MainMenuPModel;
import easytournament.basic.navigationitem.NavTreeActions;
import easytournament.basic.navigationitem.NaviNode;
import easytournament.basic.navigationitem.NavigationItem;
import easytournament.basic.print.TablePrinter;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.settings.SettingsRegistry;
import easytournament.designer.export.ScheduleExportable;
import easytournament.designer.gui.SchedulePanel;
import easytournament.designer.gui.toolbar.ScheduleToolBar;
import easytournament.designer.model.SchedulePanelPModel;
import easytournament.designer.settings.ScheduleSettings;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.designer.xml.ScheduleXMLHandler;
import easytournament.tournament.calc.Calculator;

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
