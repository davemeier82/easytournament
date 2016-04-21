package easytournament.basic.util.transferhandler;

import javax.swing.AbstractListModel;

public class ListTransferData {

    private final AbstractListModel model;
    private final int[] indices;
 
    ListTransferData(AbstractListModel model, int[] indices) {
      this.model = model;
      this.indices = indices;
    }
 
    int[] getIndices() {
      return indices;
    }
 
    public AbstractListModel getModel() {
      return model;
    }

}
