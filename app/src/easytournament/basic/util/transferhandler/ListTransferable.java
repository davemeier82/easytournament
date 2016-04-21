package easytournament.basic.util.transferhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;

public class ListTransferable<T extends List<?>> implements Transferable {

  private T list;

  public ListTransferable(T list) {
    super();
    this.list = list;
  }

  public Object getTransferData(DataFlavor arg0) {
    return list;

  }

  public DataFlavor[] getTransferDataFlavors() {
    try {
      return new DataFlavor[] {new DataFlavor(
          DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.ArrayList")};
    }
    catch (ClassNotFoundException e) {
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
    return new DataFlavor[0];
  }

  public boolean isDataFlavorSupported(DataFlavor arg0) {
    try {
      return arg0.equals(new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
          + ";class=java.util.ArrayList"));
    }
    catch (ClassNotFoundException e) {
      //do nothing
    }
    return false;
  }

}
