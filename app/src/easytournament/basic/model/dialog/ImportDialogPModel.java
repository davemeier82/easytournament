package easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.export.ImportRegistry;
import easytournament.basic.export.Importable;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;

public class ImportDialogPModel extends Model {

  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;

  public static final int IMPORT_LIST = 0;

  public static final String DISPOSE = "dispose";

  public static final String PROPERTY_SELECTION = "selection";

  protected PropertyAdapter<ImportDialogPModel> selectionAdapter;
  protected ArrayListModel<String> importLabels = new ArrayListModel<String>();
  protected SelectionInList<ArrayListModel<String>> importSelection;

  protected String selection;
  protected HashMap<String,Importable> importables = ImportRegistry.getRegistry();

  public ImportDialogPModel() {
    for (String s : importables.keySet()) {
      importLabels.add(s);
    }
    if(importLabels.size() > 0)
      this.selection = importLabels.get(0);
    this.selectionAdapter = new PropertyAdapter<ImportDialogPModel>(this,
        PROPERTY_SELECTION, true);
    this.importSelection = new SelectionInList<ArrayListModel<String>>(
        importLabels, selectionAdapter);
  }

  public Action getAction(int a) {
    switch (a) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            ImportDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
            Importable ex = importables.get(selection);
            if (ex != null)
              ex.doImport();
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            ImportDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
    }
    return null;
  }

  public SelectionInList<?> getSelectionInList(int list) {
    switch (list) {
      case IMPORT_LIST:
        return importSelection;
    }
    return null;
  }

  public String getSelection() {
    return selection;
  }

  public void setSelection(String selection) {
    this.selection = selection;
  }

}
