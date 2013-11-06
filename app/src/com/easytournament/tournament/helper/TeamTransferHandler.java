package com.easytournament.tournament.helper;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import com.easytournament.basic.util.transferhandler.ListTransferable;
import com.easytournament.basic.valueholder.Team;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.easytournament.tournament.model.listmodel.TeamListModel;


public class TeamTransferHandler extends TransferHandler {

  private static final long serialVersionUID = 1L;
  private int[] indices = null;
  private ArrayList<Team> valuelist;
  private int maxSize;

  public TeamTransferHandler() {
    this.maxSize = -1;
  }

  public TeamTransferHandler(AbstractGroup g) {
    this.maxSize = g.getNumStartPos();
  }

  public boolean canImport(TransferHandler.TransferSupport info) {
    try {
      if (!info
          .isDataFlavorSupported(new DataFlavor(
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

  protected Transferable createTransferable(JComponent c) {
    JList table = (JList)c;
    indices = table.getSelectedIndices();
    valuelist = new ArrayList<Team>();
    TeamListModel model = (TeamListModel)table.getModel();
    for (int i = 0; i < indices.length; i++) {
      valuelist.add(model.getTeamAt(indices[i]));
    }
    return new ListTransferable<ArrayList<Team>>(valuelist);
  }

  public int getSourceActions(JComponent c) {
    return TransferHandler.MOVE;
  }

  @SuppressWarnings( {"unchecked", "cast"})
  public boolean importData(TransferHandler.TransferSupport info) {
    if (!info.isDrop()) {
      return false;
    }

    JList list = (JList)info.getComponent();
    TeamListModel listModel = (TeamListModel)list.getModel();

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
      if (data2.size() > 0 && data2.get(0) instanceof Team)
        valuelist = (ArrayList<Team>)data2;
      else
        return false;
    }
    else {
      return false;
    }
    
    JList.DropLocation dropLocation = (JList.DropLocation) info.getDropLocation();
    int drow = dropLocation.getIndex();
    
    //move in the same list
    for(int i = 0; i < listModel.getSize(); i++){
      if(listModel.getTeamAt(i)==valuelist.get(0)){
  
        for(int j = 0; j < valuelist.size(); j++)
          if(drow > i) {
            listModel.removeElement(valuelist.get(j));
            listModel.addRow(drow-1, valuelist.get(j));
          }
          else if (drow < i) {
            listModel.removeElement(valuelist.get(j));
            listModel.addRow(drow+j, valuelist.get(j));
          }
        return false;
      }
    }
    
    if (maxSize >= 0 && listModel.getSize() + valuelist.size() > maxSize) {
      JOptionPane.showMessageDialog(null, "max. " + maxSize + " Teams",
          "Fehler", JOptionPane.ERROR_MESSAGE); //TODO lang
      return false;
    }
    for (int i = 0; i < valuelist.size(); i++) {
      listModel.addRow(drow++, valuelist.get(i));
    }
    return true;
  }

  protected void exportDone(JComponent c, Transferable data, int action) {
    JList source = (JList)c;
    TeamListModel tableModel = (TeamListModel)source.getModel();

    if (action == TransferHandler.MOVE) {
      for (int i = indices.length - 1; i >= 0; i--) {
        tableModel.removeRow(indices[i]);
      }
      //tableModel.fireTableDataChanged();
      source.repaint();

    }
    indices = null;

  }

}
