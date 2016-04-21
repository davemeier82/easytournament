package easytournament.tournament.navigationitem;

import java.awt.Dimension;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

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
import easytournament.tournament.export.GamesExportable;
import easytournament.tournament.export.TablesExportable;
import easytournament.tournament.gui.GamesPanel;
import easytournament.tournament.gui.toolbar.GamesToolBar;
import easytournament.tournament.model.GamesPanelPModel;
import easytournament.tournament.model.tablemodel.GamesTableModel;


public class GamesItem extends NavigationItem {

  private GamesPanel panel;
  private GamesPanelPModel pm;

  @Override
  public void init() {
	pm = new GamesPanelPModel();
    panel = new GamesPanel(pm);
    ExportRegistry.register(ResourceManager.getText(Text.RESULTS), new GamesExportable(this, panel));
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
    toolbar = new GamesToolBar(pm);
  }

  public boolean deactivate() {
    super.deactivate();
    return true;
  }

  public NaviNode getNode() {
    return new NaviNode(ResourceManager.getText(Text.GAMES), this);
  }

  public JComponent getPanel() {
    // panel.init();
    return new JScrollPane(panel);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(NavTreeActions.OPEN.name())) {
      // panel.init();
      if (active) {
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
    DefaultTableCellRenderer stcr = null;
    DefaultTableCellRenderer dtcr = null;
    if(Organizer.getInstance().isSubstance()) {    
     stcr = new SubstanceDefaultTableCellRenderer();
     dtcr = new SubstanceDefaultTableCellRenderer();
    } else {
      stcr = new DefaultTableCellRenderer();
      dtcr = new DefaultTableCellRenderer();
    }    
    
    dtcr.setHorizontalAlignment(SwingConstants.CENTER);
    JTable table = panel.getTable();

    TableColumnModel tcm = table.getColumnModel();
    GamesTableModel tm = (GamesTableModel)table.getModel();
    tm.setCheckBoxColumnVisible(false);
    tcm.getColumn(1).setCellRenderer(dtcr);
    tcm.getColumn(2).setCellRenderer(dtcr);
    tcm.getColumn(5).setCellRenderer(dtcr);
    tcm.getColumn(6).setCellRenderer(dtcr);
    table.validate();
    Dimension dim = table.getSize();
    dim.width -= 30;
    table.setSize(dim);
    table.setMaximumSize(dim);
    table.validate();
    
    String subtitle = ResourceManager.getText(Text.GAMES);
    if(!pm.getFilter().equals(ResourceManager.getText(Text.NOFILTER))){
    	subtitle += " " + pm.getFilter();
    }

    table.setRowSelectionAllowed(false);
    job.setJobName(subtitle);    
    
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
    tm.setCheckBoxColumnVisible(true);
    tcm.getColumn(0).setMaxWidth(30);
    tcm.getColumn(1).setCellRenderer(null);
    tcm.getColumn(2).setCellRenderer(dtcr);
    tcm.getColumn(3).setCellRenderer(dtcr);
    tcm.getColumn(5).setCellRenderer(stcr);
    tcm.getColumn(6).setCellRenderer(dtcr);
    tcm.getColumn(7).setCellRenderer(dtcr);
    table.setRowSelectionAllowed(true);
  }
}
