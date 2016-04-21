package easytournament.designer.gui.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;

import easytournament.designer.valueholder.DuellGroup;
import easytournament.designer.valueholder.Position;

public class SubstPositionListCellRenderer extends SubstanceDefaultListCellRenderer {

  private static final long serialVersionUID = 100452192651676523L;

  public Component getListCellRendererComponent(JList list, Object value,
      int row, boolean isSelected, boolean hasFocus) {

    JLabel label = (JLabel)super.getListCellRendererComponent(list, value,
        row, isSelected, hasFocus);

    if (value != null)
      try {
        DuellGroup dg = (DuellGroup)((Position)value).getGroup();
        int idx = dg.getPositions().indexOf(value);
        label.setText((idx + 1) + ". " + dg.getName());
      }
      catch (Exception e) {
        label.setText(((Position)value).getName());
      }

    return this;
  }

}
