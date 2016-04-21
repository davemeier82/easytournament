package easytournament.basic.util.transferhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ListModelTransferable implements Transferable {

  private final ListTransferData data;

  public ListModelTransferable(ListTransferData data) {
    this.data = data;
  }

  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[] {new DataFlavor(ListTransferData.class, "List data")};
  }

  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return flavor.equals(new DataFlavor(ListTransferData.class, "List data"));
  }

  public Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException, IOException {
    if (!isDataFlavorSupported(flavor)) {
      throw new UnsupportedFlavorException(flavor);
    }
    return data;
  }

}
