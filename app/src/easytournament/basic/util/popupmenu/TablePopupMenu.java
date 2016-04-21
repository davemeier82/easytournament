package easytournament.basic.util.popupmenu;

import javax.swing.JPopupMenu;

public class TablePopupMenu extends JPopupMenu {

  protected int row;
  protected int col;
  
  public int getRow() {
    return row;
  }
  public void setRow(int row) {
    this.row = row;
  }
  public int getCol() {
    return col;
  }
  public void setCol(int col) {
    this.col = col;
  }
  
}
