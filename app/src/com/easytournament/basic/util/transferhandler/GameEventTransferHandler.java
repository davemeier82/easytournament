package com.easytournament.basic.util.transferhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.easytournament.basic.model.tablemodel.GameEventsTableModel;
import com.easytournament.basic.util.transferhandler.ListTransferable;
import com.easytournament.basic.valueholder.GameEvent;


public class GameEventTransferHandler extends TransferHandler {

  private static final long serialVersionUID = 1L;
  private int[] indices = null;
  private ArrayList<GameEvent> valuelist;
  private int sourceAction = TransferHandler.COPY;
  private boolean canImport;

  public GameEventTransferHandler(int sourceAction, boolean canImport) {
    super();
    this.sourceAction = sourceAction;
    this.canImport = canImport;
  }

  public boolean canImport(TransferHandler.TransferSupport info) {
    if (canImport) {
      try {
        if (!info.isDataFlavorSupported(new DataFlavor(
            DataFlavor.javaJVMLocalObjectMimeType
                + ";class=java.util.ArrayList"))) {
          return false;
        }
      }
      catch (ClassNotFoundException e) {
        return false;
      }
      return true;
    }
    return false;
  }

  protected Transferable createTransferable(JComponent c) {
    JTable table = (JTable)c;
    indices = table.getSelectedRows();
    valuelist = new ArrayList<GameEvent>();
    GameEventsTableModel model = (GameEventsTableModel)table.getModel();
    for (int i = 0; i < indices.length; i++) {
      valuelist.add(model.getEventAt(indices[i]));
    }
    return new ListTransferable<ArrayList<GameEvent>>(valuelist);
  }

  public int getSourceActions(JComponent c) {
    return this.sourceAction;
  }

  @SuppressWarnings({"unchecked", "cast"})
  public boolean importData(TransferHandler.TransferSupport info) {
    if (!info.isDrop()) {
      return false;
    }

    JTable list = (JTable)info.getComponent();
    GameEventsTableModel listModel = (GameEventsTableModel)list.getModel();

    Transferable t = info.getTransferable();
    Object data;
    try {
      data = t
          .getTransferData(new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
              + ";class=java.util.ArrayList"));
    }
    catch (Exception e) {
      return false;
    }

    if (data instanceof ArrayList) {
      ArrayList data2 = (ArrayList)data;
      if (data2.size() > 0 && data2.get(0) instanceof GameEvent)
        valuelist = (ArrayList<GameEvent>)data2;
      else
        return false;
    }
    else {
      return false;
    }

    JTable.DropLocation dropLocation = (JTable.DropLocation)info
        .getDropLocation();
    int drow = dropLocation.getRow();

    for (int i = 0; i < valuelist.size(); i++) {
      GameEvent ge = valuelist.get(i);
      ge.updateId();
      listModel.addRow(drow, ge);
    }
    return true;
  }

  protected void exportDone(JComponent c, Transferable data, int action) {
    JTable source = (JTable)c;
    GameEventsTableModel tableModel = (GameEventsTableModel)source.getModel();

    if (action == TransferHandler.MOVE) {
      for (int i = indices.length - 1; i >= 0; i--) {
        tableModel.removeRow(indices[i]);
      }
      tableModel.fireTableDataChanged();
      source.repaint();

    }
    indices = null;

  }

}
