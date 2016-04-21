package com.easytournament.designer.gui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.easytournament.basic.valueholder.Team;
import com.easytournament.designer.valueholder.DuellGroup;
import com.easytournament.designer.valueholder.Position;

public class PositionListCellRenderer extends DefaultListCellRenderer {

  private static final long serialVersionUID = 100452192651676523L;
  public static final String PROPERTY_SHOW_TEAMS = "showTeams";
  
  protected boolean showTeams = false;

  public Component getListCellRendererComponent(JList list, Object value,
      int row, boolean isSelected, boolean hasFocus) {

    JLabel label = (JLabel)super.getListCellRendererComponent(list, value,
        row, isSelected, hasFocus);

    if (value != null) {
      if (showTeams) {
        Team t = null;
        Position pos = null;
        if (value instanceof Position) {
          pos = (Position)value;
          t = pos.getTeam();
        }
        if (t == null
            || pos == null
            || (pos.getPrev() != null && !pos.getPrev().getGroup()
                .isAllGamesPlayed())) {
          if (pos == null) {
            label.setText(value.toString());
          }
          else {
            try {
              DuellGroup dg = (DuellGroup)pos.getGroup();
              int idx = dg.getPositions().indexOf(value);
              label.setText((idx + 1) + ". " + dg.getName());
            }
            catch (Exception e) {
              label.setText(pos.getName());
            }
          }
        }
        else {
          label.setText(t.getName());
        }
      }
      else {
        try {
          DuellGroup dg = (DuellGroup)((Position)value).getGroup();
          int idx = dg.getPositions().indexOf(value);
          label.setText((idx + 1) + ". " + dg.getName());
        }
        catch (Exception e) {
          label.setText(((Position)value).getName());
        }
      }
    }
    return this;
  }

  
  public boolean isShowTeams() {
    return showTeams;
  }

  public void setShowTeams(boolean showTeams) {
    this.showTeams = showTeams;
  }
  
}
