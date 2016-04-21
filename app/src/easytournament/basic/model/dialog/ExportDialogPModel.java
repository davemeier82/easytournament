package easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.export.ExportRegistry;
import easytournament.basic.export.Exportable;
import easytournament.basic.navigationitem.NavTreeItems;
import easytournament.basic.navigationitem.NavigationItem;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;

public class ExportDialogPModel extends Model {

  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;

  public static final int EXPORT_LIST = 0;

  public static final String DISPOSE = "dispose";

  public static final String PROPERTY_SELECTION = "selection";

  protected PropertyAdapter<ExportDialogPModel> selectionAdapter;
  protected ArrayListModel<String> exportLabels = new ArrayListModel<String>();
  protected SelectionInList<ArrayListModel<String>> exportSelection;

  protected String selection;
  protected HashMap<String,Exportable> exportables = ExportRegistry.getRegistry();
  private NavigationItem activeModule;

  public ExportDialogPModel(NavigationItem activeModule) {
    this.activeModule = activeModule;
    for (String s : exportables.keySet()) {
      exportLabels.add(s);
    }
    if(exportLabels.size() > 0)
      this.selection = exportLabels.get(0);
    this.selectionAdapter = new PropertyAdapter<ExportDialogPModel>(this,
        PROPERTY_SELECTION, true);
    this.exportSelection = new SelectionInList<ArrayListModel<String>>(
        exportLabels, selectionAdapter);
  }

  public Action getAction(int a) {
    switch (a) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            ExportDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
            Exportable ex = exportables.get(selection);
            if (ex != null)
              ex.doExport(activeModule == ex.getModule());
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            ExportDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
    }
    return null;
  }

  public SelectionInList<?> getSelectionInList(int list) {
    switch (list) {
      case EXPORT_LIST:
        return exportSelection;
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
