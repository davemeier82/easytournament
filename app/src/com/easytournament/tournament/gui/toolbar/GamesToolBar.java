package com.easytournament.tournament.gui.toolbar;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JToolBar;

import com.easytournament.tournament.model.GamesPanelPModel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.ComboBoxAdapter;


public class GamesToolBar extends JToolBar {
  
  private GamesPanelPModel model;
  private PresentationModel<GamesPanelPModel> pm;
  
  public GamesToolBar(GamesPanelPModel model){
    this.model = model;
    this.pm = new PresentationModel<GamesPanelPModel>(model);
    this.init();
  }

  private void init() {
    this.setFloatable(false);
    this.addSeparator();
    
    ComboBoxAdapter<String> filterAdapter = new ComboBoxAdapter<String>(
            (List<String>)this.pm.getValue(GamesPanelPModel.PROPERTY_FILTERLABELS),
            this.pm.getModel(GamesPanelPModel.PROPERTY_FILTER));
    @SuppressWarnings("unchecked")
	JComboBox<String> filterCB = new JComboBox<String>(filterAdapter);
    filterCB.setPreferredSize(new Dimension(200, 0));
    filterCB.setMaximumSize(new Dimension(200, 100));
    filterCB.setFocusable(false);
    this.add(filterCB);
  }

}
