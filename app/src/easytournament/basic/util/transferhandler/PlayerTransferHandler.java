package easytournament.basic.util.transferhandler;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import easytournament.basic.model.listmodel.PlayerListModel;
import easytournament.basic.valueholder.Player;


public class PlayerTransferHandler extends TransferHandler {

  private int action;
  private boolean allowReordering;
  private boolean playerSink;

  public PlayerTransferHandler(int action, boolean allowReordering, boolean playerSink) {
    this.action = action;
    this.allowReordering = allowReordering;
    this.playerSink = playerSink;
  }

  public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
    if (!(comp instanceof JList)
        || !(((JList<?>)comp).getModel() instanceof AbstractListModel)) {
      return false;
    }

    for (DataFlavor f : transferFlavors) {
      if (f.equals(new DataFlavor(ListTransferData.class, "List data"))) {
        return true;
      }
    }
    return false;
  }

  protected Transferable createTransferable(JComponent c) {
    final JList<?> list = (JList<?>)c;
    final int[] selectedIndices = list.getSelectedIndices();
    return new ListModelTransferable(new ListTransferData(
        (AbstractListModel<?>)list.getModel(), selectedIndices));
  }

  public int getSourceActions(JComponent c) {
    return action;
  }

  public boolean importData(TransferHandler.TransferSupport info) {
    final Component comp = info.getComponent();
    if (!info.isDrop() || !(comp instanceof JList)) {
      return false;
    }
    final JList<?> list = (JList<?>)comp;
    final ListModel<?> lm = list.getModel();
    if (!(lm instanceof AbstractListModel)) {
      return false;
    }

    final PlayerListModel targetModel = (PlayerListModel)lm;
    final DataFlavor flavor = new DataFlavor(ListTransferData.class,
        "List data");
    if (!info.isDataFlavorSupported(flavor)) {
      return false;
    }

    final Transferable transferable = info.getTransferable();
    try {
      final ListTransferData data = (ListTransferData)transferable
          .getTransferData(flavor);

      PlayerListModel sourceModel = (PlayerListModel)data.getModel();

      if (!allowReordering && sourceModel == targetModel)
        return false;

      // get the initial insertion index
      final JList.DropLocation dropLocation = list.getDropLocation();
      int insertAt = dropLocation.getIndex();

      // get the indices sorted (we use them in reverse order, below)
      final int[] indices = data.getIndices();
      Arrays.sort(indices);

      // remove old elements from model, store them on stack
      final Stack<Player> elements = new Stack<Player>();
      int shift = 0;
      for (int i = indices.length - 1; i >= 0; i--) {
        final int index = indices[i];
        if (index < insertAt) {
          shift--;
        }
        elements.push(sourceModel.getData().get(index));
        if(sourceModel == targetModel || playerSink)
          sourceModel.getData().remove(index);
      }
      insertAt += shift;

      // insert stored elements from stack to model
      final ListSelectionModel sm = list.getSelectionModel();
      try {
        sm.setValueIsAdjusting(true);
        sm.clearSelection();
        final int anchor = insertAt;
        while (!elements.isEmpty()) {
          Player p = elements.pop();
          if(!playerSink && (sourceModel == targetModel || !targetModel.getData().contains(p)))
            targetModel.getData().add(insertAt, p);
          sm.addSelectionInterval(insertAt, insertAt++);
        }
        final int lead = insertAt - 1;
        if (!sm.isSelectionEmpty()) {
          sm.setAnchorSelectionIndex(anchor);
          sm.setLeadSelectionIndex(lead);
        }
      }
      finally {
        sm.setValueIsAdjusting(false);
      }
      return true;
    }
    catch (UnsupportedFlavorException ex) {
      return false;
    }
    catch (IOException ex) {
      return false;
    }
  }

}
