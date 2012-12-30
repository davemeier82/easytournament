package com.easytournament.basic.util.transferhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;


public class StringTransferHandler extends TransferHandler {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private int[] indices = null;
  private ArrayList<String> strings;


  public boolean canImport(TransferHandler.TransferSupport info) {
    // Check for String flavor
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
    JList<String> sourcelist = (JList<String>)c;
    indices = sourcelist.getSelectedIndices();
    strings = new ArrayList<String>();
    DefaultListModel<String> model = (DefaultListModel<String>)sourcelist.getModel();
    for (int i = 0; i < indices.length; i++) {
      strings.add(model.elementAt(indices[i]));
    }
    return new ListTransferable<ArrayList<String>>(strings);
  }

  public int getSourceActions(JComponent c) {
    return TransferHandler.MOVE;
  }

  public boolean importData(TransferHandler.TransferSupport info) {
    if (!info.isDrop()) {
      return false;
    }

    JList<String> list = (JList<String>)info.getComponent();
    DefaultListModel<String> listModel = (DefaultListModel<String>)list
        .getModel();
    JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
    int index = dl.getIndex();

    // Get the string that is being dropped.
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
    strings = (ArrayList<String>)data;
    
    //move in the same list
    for(int i = 0; i < listModel.getSize(); i++){
      if(listModel.getElementAt(i)==strings.get(0)){
  
        for(int j = 0; j < strings.size(); j++)
          if(index > i) {
            listModel.removeElement(strings.get(j));
            listModel.add(index-1, strings.get(j));            
          }
          else if (index < i){
            listModel.removeElement(strings.get(j));
            listModel.add(index+j, strings.get(j));
          }            
        return false;
      }
    }

    for (int i = 0; i < strings.size(); i++) {
      listModel.add(index++, strings.get(i));
    }
    return true;
  }

  protected void exportDone(JComponent c, Transferable data, int action) {
    JList<String> source = (JList<String>)c;
    DefaultListModel<String> listModel = (DefaultListModel<String>)source
        .getModel();

    if (action == TransferHandler.MOVE) {
      for (int i = indices.length - 1; i >= 0; i--) {
        listModel.remove(indices[i]);
      }
    }
    indices = null;
  }

}
