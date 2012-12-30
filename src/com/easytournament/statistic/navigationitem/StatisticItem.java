package com.easytournament.statistic.navigationitem;

import java.awt.BorderLayout;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import com.easytournament.basic.MainMenuObservable;
import com.easytournament.basic.Organizer;
import com.easytournament.basic.action.MainMenuAction;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.model.MainMenuPModel;
import com.easytournament.basic.navigationitem.NaviNode;
import com.easytournament.basic.navigationitem.NavigationItem;
import com.easytournament.basic.print.TablePrinter;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.xml.SportXMLHandler;
import com.easytournament.statistic.gui.DefaultStatsPanel;
import com.easytournament.statistic.gui.ManualStatsPanel;
import com.easytournament.statistic.model.DefaultStatsPModel;
import com.easytournament.statistic.model.ManualStatsPModel;


public class StatisticItem extends NavigationItem {

  protected JPanel panel;
  protected DefaultStatsPModel defstatPm;
  protected ManualStatsPModel manstatPm;
  protected DefaultStatsPanel defstatPanel;
  protected JTabbedPane tabbedPane;
  protected ManualStatsPanel manstatPanel;
  
  public void init() {
    Organizer.getInstance().setSports(SportXMLHandler.readSports());
    Organizer.getInstance().resetTournament();
    panel = new JPanel(new BorderLayout());
    tabbedPane = new JTabbedPane();
    defstatPm = new DefaultStatsPModel();
    manstatPm = new ManualStatsPModel();
    defstatPanel = new DefaultStatsPanel(defstatPm);
    manstatPanel = new ManualStatsPanel(manstatPm);
    tabbedPane.addTab(ResourceManager.getText(Text.DEFAULT_STATISTIC), defstatPanel);
    tabbedPane.addTab(ResourceManager.getText(Text.MANUAL_STATISTIC), manstatPanel);
    panel.add(tabbedPane, BorderLayout.CENTER);
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
    defstatPm.updateStatistic();
  }

  public boolean deactivate() {
    super.deactivate();
    return true;
  }

  public NaviNode getNode() {
    return new NaviNode(ResourceManager.getText(Text.STATISTICS), this);
  }

  public JComponent getPanel() {
    return panel;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    // do nothing
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
    job.setJobName(ResourceManager.getText(Text.STATISTIC));
    JTable table = null;
    if(tabbedPane.getSelectedIndex() == 0){
      table = defstatPanel.getTable();
      job.setPrintable(new TablePrinter(table, defstatPm
          .getStatPrintTitle()));
    } else {
      table = manstatPanel.getTable();
      job.setPrintable(new TablePrinter(table, manstatPm.getTableTitle()));
    }
    
    table.setRowSelectionAllowed(false);
    
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
