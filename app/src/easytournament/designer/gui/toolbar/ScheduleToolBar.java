package easytournament.designer.gui.toolbar;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.ComboBoxAdapter;

import easytournament.basic.model.settings.GeneralSetPModel;
import easytournament.designer.model.SchedulePanelPModel;


public class ScheduleToolBar extends JToolBar {
  
  private SchedulePanelPModel model;
  private PresentationModel<SchedulePanelPModel> pm;
  
  public ScheduleToolBar(SchedulePanelPModel model){
    this.model = model;
    this.pm = new PresentationModel<SchedulePanelPModel>(model);
    this.init();
  }

  private void init() {
    this.setFloatable(false);
    this.addSeparator();

    JButton changeViewBtn = new JButton(this.model.getAction(SchedulePanelPModel.CHANGE_TEAMVIEW_ACTION));
    changeViewBtn.setFocusPainted(false);
    this.add(changeViewBtn);
    
    ComboBoxAdapter<String> filterAdapter = new ComboBoxAdapter<String>(
            (List<String>)this.pm.getValue(SchedulePanelPModel.PROPERTY_FILTERLABELS),
            this.pm.getModel(SchedulePanelPModel.PROPERTY_FILTER));
    @SuppressWarnings("unchecked")
	JComboBox<String> filterCB = new JComboBox<String>(filterAdapter);
    filterCB.setPreferredSize(new Dimension(200, 0));
    filterCB.setMaximumSize(new Dimension(200, 100));
    filterCB.setFocusable(false);
    this.add(filterCB);
  }

}
