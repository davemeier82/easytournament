package com.easytournament.basic.model;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import org.jdom.Document;
import org.jdom.Element;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.Organizer;
import com.easytournament.basic.gameevent.GameEventType;
import com.easytournament.basic.gui.dialog.ErrorDialog;
import com.easytournament.basic.gui.dialog.ImportListDialog;
import com.easytournament.basic.logging.ErrorLogger;
import com.easytournament.basic.model.dialog.GEventDialogPModel;
import com.easytournament.basic.model.dialog.ImportListDialogPModel;
import com.easytournament.basic.model.tablemodel.EventImportTableModel;
import com.easytournament.basic.model.tablemodel.GameEventsTableModel;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.valueholder.GameEvent;
import com.easytournament.basic.valueholder.GameEventEntry;
import com.easytournament.basic.valueholder.Sport;
import com.easytournament.basic.valueholder.Tournament;
import com.easytournament.basic.xml.GameEventsXMLHandler;
import com.easytournament.basic.xml.XMLHandler;
import com.easytournament.designer.valueholder.ScheduleEntry;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;

public class EventsPanelPModel extends Model implements GEventDialogPModel,
    TableModelListener {

  public static final int AVAILABLE_TABLE = 0;
  public static final int USED_TABLE = 1;
  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;
  public static final int ADD_ACTION = 2;
  public static final int NEW_ACTION = 3;
  public static final int EDIT_ACTION = 4;
  public static final int DELETE_ACTION = 5;
  public static final int IMPORT_ACTION = 6;

  public static final String PROPERTY_SPORT = "sport";
  public static final String SHOW_EVENT_DIALOG = "showEventDialog";

  protected GameEventsTableModel availableEvents = new GameEventsTableModel();
  protected GameEventsTableModel usedEvents = new GameEventsTableModel();
  protected ListSelectionModel availSelModel = new DefaultListSelectionModel();
  protected ListSelectionModel usedSelModel = new DefaultListSelectionModel();
  protected Tournament t = Organizer.getInstance().getCurrentTournament();
  protected ArrayListModel<Sport> sports = new ArrayListModel<Sport>(Organizer
      .getInstance().getSports().values());
  protected SelectionInList<ArrayListModel<Sport>> sportSelection;
  protected PropertyAdapter<EventsPanelPModel> sportAdapter;
  protected Sport sport = sports.get(0);
  protected GameEvent editedGameEvent;
  protected int editedGameEventIdx = -1;
  protected GameEvent tmpEvent;
  protected boolean eventsChanged = false;

  public EventsPanelPModel() {
    retrieveData();
  }

  void retrieveData() {
    this.sportAdapter = new PropertyAdapter<EventsPanelPModel>(this,
        PROPERTY_SPORT, true);
    this.sportSelection = new SelectionInList<ArrayListModel<Sport>>(sports,
        sportAdapter);
    availableEvents.setData(sport.getGameEvents());
    usedEvents.setData(t.getGameEvents());
    usedEvents.addTableModelListener(this);
  }

  public ListSelectionModel getSelectionModel(int list) {
    switch (list) {
      case AVAILABLE_TABLE:
        return this.availSelModel;
      case USED_TABLE:
        return this.usedSelModel;
    }
    return null;
  }

  public Action getAction(int action) {
    switch (action) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {
          @Override
          public void actionPerformed(ActionEvent arg0) {
            if (editedGameEventIdx < 0) {
              usedEvents.addRow(editedGameEvent);
            } else {
              eventsChanged = true;
            }
            t.getSport().setEdited(true);
            EventsPanelPModel.this.firePropertyChange(DISPOSE, 0, 1);
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {
          @Override
          public void actionPerformed(ActionEvent arg0) {
            if(editedGameEventIdx >= 0)
              editedGameEvent.setEvent(tmpEvent);
            EventsPanelPModel.this.firePropertyChange(DISPOSE, 0, 1);
          }
        };
      case ADD_ACTION:
        return new AbstractAction(null, ResourceManager.getIcon(Icon.LEFT_ICON)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            int min = availSelModel.getMinSelectionIndex();
            int max = availSelModel.getMaxSelectionIndex();
            if (min < 0)
              return;
            for (int i = min; i <= max; i++) {
              if (availSelModel.isSelectedIndex(i)) {
                try {
                  GameEvent ge = (GameEvent)availableEvents.getEventAt(i)
                      .clone();
                  ge.updateId();
                  usedEvents.addRow(ge);
                }
                catch (CloneNotSupportedException e) {
                  ErrorLogger.getLogger().throwing("EventsPanelPModel",
                      "AbstractAction.actionPerformed", e);
                  ErrorDialog ed = new ErrorDialog(Organizer.getInstance()
                      .getMainFrame(), ResourceManager.getText(Text.ERROR),
                      e.toString(), e);
                  ed.setVisible(true);
                  e.printStackTrace();
                }
              }
            }

          }
        };
      case NEW_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.NEW_EVENT),
            ResourceManager.getIcon(Icon.ADD_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            editedGameEventIdx = -1;
            editedGameEvent = new GameEvent();
            EventsPanelPModel.this.firePropertyChange(SHOW_EVENT_DIALOG, 0, 1);

          }
        };
      case EDIT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.EDIT_EVENT),
            ResourceManager.getIcon(Icon.EDIT_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            int idx = usedSelModel.getMinSelectionIndex();
            if (idx >= 0) {
              try {
                editedGameEventIdx = idx;
                tmpEvent = (GameEvent)usedEvents.getEventAt(idx).clone();
                editedGameEvent = usedEvents.getEventAt(idx);
                if (editedGameEvent != null)
                  EventsPanelPModel.this.firePropertyChange(SHOW_EVENT_DIALOG,
                      0, 1);
              }
              catch (CloneNotSupportedException e) {
                // do nothing
              }
            }
          }
        };
      case DELETE_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.DELETE_EVENT),
            ResourceManager.getIcon(Icon.DELETE_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent arg0) {
            deleteAction();
          }
        };
      case IMPORT_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.IMPORT)) {
          @Override
          public void actionPerformed(ActionEvent arg0) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter() {
              public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".xml")
                    || f.getName().toLowerCase().endsWith(".ett")
                    || f.isDirectory();
              }

              public String getDescription() {
                return "ETT (*.ett), XML (*.xml)";
              }
            });
            int answer = chooser.showOpenDialog(Organizer.getInstance()
                .getMainFrame());

            if (answer == JFileChooser.APPROVE_OPTION) {
              Document doc;
              try {
                doc = XMLHandler.openXMLDoc(chooser.getSelectedFile());
              }
              catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(Organizer.getInstance()
                    .getMainFrame(), ResourceManager
                    .getText(Text.FILE_NOT_FOUND), ResourceManager
                    .getText(Text.FILE_NOT_FOUND), JOptionPane.ERROR_MESSAGE);
                return;
              }
              if (doc != null) {
                Element filetype = doc.getRootElement();
                String app = filetype.getAttributeValue("application");
                if (app == null || !app.equals(MetaInfos.FILE_APPLICATION)) {
                  showFileNotSupported();
                  return;
                }

                String type = filetype.getAttributeValue("type");
                if (type == null || !(type.equals(MetaInfos.FILE_MAINFILETYPE))) {
                  showFileNotSupported();
                  return;
                }
                String version = filetype.getAttributeValue("version");
                if (MetaInfos.compareVersionNr(MetaInfos.getXMLFileVersion(),
                    version) < 0) {
                  JOptionPane.showMessageDialog(
                      Organizer.getInstance().getMainFrame(),
                      ResourceManager.getText(Text.NEW_VERSION1)
                          + MetaInfos.APP_NAME
                          + ResourceManager.getText(Text.NEW_VERSION2)
                          + MetaInfos.APP_WEBSITE
                          + ResourceManager.getText(Text.NEW_VERSION3),
                      ResourceManager.getText(Text.NEW_VERSION),
                      JOptionPane.ERROR_MESSAGE);
                  return;
                }
                Element tournEl = filetype.getChild("tournament");
                ArrayList<GameEvent> ievents = GameEventsXMLHandler
                    .readGameEvent(tournEl.getChild("sport"), true);
                if (ievents.size() < 0) {
                  JOptionPane.showMessageDialog(Organizer.getInstance()
                      .getMainFrame(), ResourceManager
                      .getText(Text.NO_EVENT_FOUND_MSG), ResourceManager
                      .getText(Text.NO_EVENT_FOUND), JOptionPane.ERROR_MESSAGE);
                  return;
                }
                ArrayList<Boolean> selection = new ArrayList<Boolean>(
                    ievents.size());
                for (int i = 0; i < ievents.size(); i++)
                  selection.add(new Boolean(true));

                ImportListDialogPModel ilpm = new ImportListDialogPModel(
                    new EventImportTableModel(ievents, selection));

                new ImportListDialog(Organizer.getInstance().getMainFrame(),
                    ResourceManager.getText(Text.SELECT_EVENT_IMPORT), true,
                    ilpm);
                if (ilpm.getAnswer()) {
                  for (int i = 0; i < ievents.size(); i++) {
                    if (selection.get(i)) {
                      availableEvents.addRow(ievents.get(i));
                    }
                  }
                }

              }
              else {
                showFileNotSupported();
              }
            }
          }

          public void showFileNotSupported() {
            JOptionPane.showMessageDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager
                .getText(Text.FILE_NOT_SUPPORTED), ResourceManager
                .getText(Text.FILE_NOT_SUPPORTED), JOptionPane.ERROR_MESSAGE);
          }
        };
    }
    return null;
  }

  public TableModel getTableModel(int list) {
    switch (list) {
      case AVAILABLE_TABLE:
        return this.availableEvents;
      case USED_TABLE:
        return this.usedEvents;
    }
    return null;
  }

  public SelectionInList<ArrayListModel<Sport>> getSportSelectionInList() {
    return sportSelection;
  }

  public Sport getSport() {
    return sport;
  }

  public void setSport(Sport sport) {
    this.sport = sport;
    availableEvents.setData(sport.getGameEvents());
    availableEvents.fireTableDataChanged();
  }

  public void deleteAction() {
    boolean showWarning = false;
    for (ScheduleEntry s : t.getSchedules()) {
      if (s.isGamePlayed()) {
        showWarning = true;
        break;
      }
    }
    if (showWarning) {
      int answer = JOptionPane.showConfirmDialog(Organizer.getInstance()
          .getMainFrame(), ResourceManager.getText(Text.CONFIRM_DEL_EVENT_MSG),
          ResourceManager.getText(Text.CONFIRM_DEL_EVENT),
          JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
      if (answer == JOptionPane.OK_OPTION) {
        delete();
      }
    }
    else {
      delete();
    }
  }

  private void delete() {
    int min = usedSelModel.getMinSelectionIndex();
    int max = usedSelModel.getMaxSelectionIndex();
    if (min < 0)
      return;
    ArrayList<GameEvent> events = new ArrayList<GameEvent>();
    ArrayList<Integer> idxs = new ArrayList<Integer>();
    for (int i = max; i >= min; i--) {
      if (usedSelModel.isSelectedIndex(i)) {
        events.add(usedEvents.getEventAt(i));
        idxs.add(i);
      }
    }

    ArrayList<GameEventEntry> toDelete = new ArrayList<GameEventEntry>();
    for (ScheduleEntry s : t.getSchedules()) {
      toDelete.clear();
      for (GameEventEntry e : s.getGameEvents()) {
        if (e.getEvent() != null)
          for (GameEvent e2 : events) {
            if (e.getEvent().equals(e2)) {
              toDelete.add(e);
            }
          }
      }
      s.getGameEvents().removeAll(toDelete);
    }

    for (int i = 0; i < idxs.size(); i++) {
      usedEvents.removeRow(idxs.get(i));
    }
  }

  @Override
  public ValueModel getGameEventValueModel(String propertyName) {
    return new PropertyAdapter<GameEvent>(this.editedGameEvent, propertyName);
  }

  @Override
  public SelectionInList<ArrayListModel<GameEventType>> getTypeSelectionInList() {
    ArrayListModel<GameEventType> eventTypes = new ArrayListModel<GameEventType>(
        Arrays.asList(GameEventType.values()));
    SelectionInList<ArrayListModel<GameEventType>> typeSelection = new SelectionInList<ArrayListModel<GameEventType>>(
        eventTypes, new PropertyAdapter<GameEvent>(this.editedGameEvent,
            GameEvent.PROPERTY_TYPE));
    return typeSelection;
  }

  public void fireDoubleClickAction(int row) {
    if (row >= 0) {
      editedGameEventIdx = row;
      try {
        this.tmpEvent = (GameEvent)usedEvents.getEventAt(row).clone();
        editedGameEvent = usedEvents.getEventAt(row);
        if (editedGameEvent != null)
          EventsPanelPModel.this.firePropertyChange(SHOW_EVENT_DIALOG, 0, 1);
      }
      catch (CloneNotSupportedException e) {
        // do nothing
      }
    }
  }

  @Override
  public boolean isNameEditable() {
    return editedGameEvent.getNameResourceId() == null;
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    t.getSport().setEdited(true);
  }

  public boolean isEventsChanged() {
    return eventsChanged;
  }

  public void setEventsChanged(boolean eventsChanged) {
    this.eventsChanged = eventsChanged;
  }

}
