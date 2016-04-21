package easytournament.basic.util.transferhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import easytournament.basic.valueholder.Rule;



public class RuleTransferHandler extends TransferHandler {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private int[] indices = null;
  private ArrayList<Rule> rules;


  public boolean canImport(TransferHandler.TransferSupport info) {
    // Check for Rule flavor
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
    JList<Rule> sourcelist = (JList<Rule>)c;
    indices = sourcelist.getSelectedIndices();
    rules = new ArrayList<Rule>();
    DefaultListModel<Rule> model = (DefaultListModel<Rule>)sourcelist.getModel();
    for (int i = 0; i < indices.length; i++) {
      rules.add(model.elementAt(indices[i]));
    }
    return new ListTransferable<ArrayList<Rule>>(rules);
  }

  public int getSourceActions(JComponent c) {
    return TransferHandler.MOVE;
  }

  public boolean importData(TransferHandler.TransferSupport info) {
    if (!info.isDrop()) {
      return false;
    }

    JList<Rule> list = (JList<Rule>)info.getComponent();
    DefaultListModel<Rule> listModel = (DefaultListModel<Rule>)list
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
    rules = (ArrayList<Rule>)data;
    
    //move in the same list
    for(int i = 0; i < listModel.getSize(); i++){
      if(listModel.getElementAt(i)==rules.get(0)){
  
        for(int j = 0; j < rules.size(); j++)
          if(index > i) {
            listModel.removeElement(rules.get(j));
            listModel.add(index-1, rules.get(j));            
          }
          else if (index < i){
            listModel.removeElement(rules.get(j));
            listModel.add(index+j, rules.get(j));
          }            
        return false;
      }
    }

    for (int i = 0; i < rules.size(); i++) {
      listModel.add(index++, rules.get(i));
    }
    return true;
  }

  protected void exportDone(JComponent c, Transferable data, int action) {
    JList<Rule> source = (JList<Rule>)c;
    DefaultListModel<Rule> listModel = (DefaultListModel<Rule>)source
        .getModel();

    if (action == TransferHandler.MOVE) {
      for (int i = indices.length - 1; i >= 0; i--) {
        listModel.remove(indices[i]);
      }
    }
    indices = null;
  }

}
