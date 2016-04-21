package easytournament.basic.model.dialog;

import javax.swing.Action;

import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.bean.ObservableBean;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.gameevent.GameEventType;

public interface GEventDialogPModel extends ObservableBean {

  public static final String DISPOSE = "dispose";
  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;

  public ValueModel getGameEventValueModel(String propertyname);

  public SelectionInList<ArrayListModel<GameEventType>> getTypeSelectionInList();
  
  public Action getAction(int action);

  public boolean isNameEditable();

}
