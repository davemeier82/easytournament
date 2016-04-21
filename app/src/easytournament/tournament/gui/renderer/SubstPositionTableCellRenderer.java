package easytournament.tournament.gui.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import easytournament.basic.valueholder.Team;
import easytournament.designer.valueholder.DuellGroup;
import easytournament.designer.valueholder.Position;

public class SubstPositionTableCellRenderer
    extends SubstanceDefaultTableCellRenderer {

  private static final long serialVersionUID = 100452192651676523L;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {

    JLabel label = (JLabel)super.getTableCellRendererComponent(table, value,
        isSelected, hasFocus, row, column);

    if (value != null && value instanceof Position) {
      Team t = null;
      Position pos = (Position)value;
      t = pos.getTeam();

      if (t == null || (pos.getPrev() != null
          && !pos.getPrev().getGroup().isAllGamesPlayed())) {

        try {
          DuellGroup dg = (DuellGroup)pos.getGroup();
          int idx = dg.getPositions().indexOf(value);
          label.setText((idx + 1) + ". " + dg.getName());
        }
        catch (Exception e) {
          label.setText(pos.getName());
        }
      }
      else {
        label.setText(t.getName());
      }
    }
    return this;
  }

}
