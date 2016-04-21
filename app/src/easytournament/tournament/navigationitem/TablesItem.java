package easytournament.tournament.navigationitem;

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
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.tournament.export.GamesExportable;
import easytournament.tournament.export.TablesExportable;
import easytournament.tournament.gui.GamesPanel;
import easytournament.tournament.gui.TablesPanel;
import easytournament.tournament.model.GamesPanelPModel;
import easytournament.tournament.print.TablesPrintable;
import easytournament.tournament.xml.TablesXMLHandler;


public class TablesItem extends NavigationItem {

  private TablesPanel panel;
  
  @Override
  public void init() {
    ExportRegistry.register(ResourceManager.getText(Text.TABLES), new TablesExportable(this));
    super.init();
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
  }

  public boolean deactivate() {
    super.deactivate();
    return true;
  }

  public NaviNode getNode() {
    return new NaviNode(ResourceManager.getText(Text.TABLES), this);
  }

  public JComponent getPanel() {
    this.panel = new TablesPanel();
    return new JScrollPane(panel);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(NavTreeActions.OPEN.name())) {
      TablesXMLHandler.open((Element)evt.getNewValue());
      if (active) {
        panel.init();
        panel.getParent().validate();
        panel.repaint();
      }
    }
    if (evt.getPropertyName().equals(NavTreeActions.NEW.name())) {
      if (active) {
        panel.init();
        panel.getParent().validate();
        panel.repaint();
      }
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
        }
      }
    }
  }

  private void print() {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName(ResourceManager.getText(Text.TABLES));
    ArrayList<JTable> tables = panel.getTables();
    for (JTable table : tables)
      table.setRowSelectionAllowed(false);
    job.setPrintable(new TablesPrintable(tables, Organizer.getInstance()
        .getCurrentTournament().getPlan().getOrderedGroups()));
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
    for (JTable table : tables)
      table.setRowSelectionAllowed(true);
  }
}
