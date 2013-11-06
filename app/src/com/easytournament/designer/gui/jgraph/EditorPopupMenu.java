package com.easytournament.designer.gui.jgraph;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;


import com.easytournament.basic.Organizer;
import com.easytournament.basic.resources.Icon;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.designer.TournamentViewer;
import com.easytournament.designer.gui.dialog.GroupDialog;
import com.easytournament.designer.model.dialog.GroupDialogPModel;
import com.easytournament.designer.navigationitem.DesignerItem;
import com.easytournament.designer.valueholder.AbstractGroup;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.util.mxGraphActions;


public class EditorPopupMenu extends JPopupMenu {

  /**
	 * 
	 */
  private static final long serialVersionUID = -3132749140550242191L;

  private class GroupAction extends AbstractAction {

    private MouseEvent e;

    public GroupAction(String name, MouseEvent e) {
      super(name);
      this.e = e;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      try {
        mxCell cell = (mxCell)TournamentViewer.getGraphComponent().getCellAt(
            e.getX(), e.getY());
        if (cell != null) {
          if (cell.getStyle().contains(TournamentViewer.GROUP_IDENTIFIER)) {
            AbstractGroup g = (AbstractGroup)cell.getValue();
            new GroupDialog(Organizer.getInstance().getMainFrame(), true,
                new GroupDialogPModel(g));
          }
          else if (cell.getStyle().contains(TournamentViewer.TEAM_IDENTIFIER)) {
            AbstractGroup g = (AbstractGroup)cell.getParent().getValue();
            new GroupDialog(Organizer.getInstance().getMainFrame(), true,
                new GroupDialogPModel(g));
          }
        }
      }
      catch (ClassCastException cce) {/* do nothing */}

    }
  }

  public EditorPopupMenu(final TournamentViewer editor, MouseEvent e) {
    boolean selected = !TournamentViewer.getGraphComponent().getGraph()
        .isSelectionEmpty();

    mxCell cell = (mxCell)TournamentViewer.getGraphComponent().getCellAt(
        e.getX(), e.getY());
    if (cell != null
        && (cell.getStyle().contains(TournamentViewer.GROUP_IDENTIFIER) || cell.getStyle().contains(
            TournamentViewer.TEAM_IDENTIFIER))) {
      add(new GroupAction(ResourceManager.getText(Text.EDIT_GROUP), e));
    }

    add(new AbstractAction(ResourceManager.getText(Text.UNDO_MENU),
        ResourceManager.getIcon(Icon.UNDO_ICON_SMALL)) {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        editor.getUndoManager().undo();
      }
    });

    addSeparator();

    add(
        editor.bind(ResourceManager.getText(Text.CUT_MENU),
            TransferHandler.getCutAction(), Icon.CUT_ICON_SMALL)).setEnabled(
        selected);
    add(
        editor.bind(ResourceManager.getText(Text.COPY_MENU),
            TransferHandler.getCopyAction(), Icon.COPY_ICON_SMALL)).setEnabled(
        selected);
    add(editor.bind(ResourceManager.getText(Text.PASTE_MENU),
        TransferHandler.getPasteAction(), Icon.PASTE_ICON_SMALL));

    addSeparator();

    add(
        editor.bind(ResourceManager.getText(Text.DELETE_MENU),
            mxGraphActions.getDeleteAction(), Icon.DELETE_ICON_SMALL))
        .setEnabled(selected);

    addSeparator();

    add(editor.bind(ResourceManager.getText(Text.SELECT_SHAPES),
        mxGraphActions.getSelectVerticesAction()));
    add(editor.bind(ResourceManager.getText(Text.SELECT_EDGES),
        mxGraphActions.getSelectEdgesAction()));

    addSeparator();

    add(editor.bind(ResourceManager.getText(Text.SELECTALL_MENU),
        mxGraphActions.getSelectAllAction()));
    addSeparator();
    add(new JMenuItem(new AbstractAction(DesignerItem.getTournamentViewer()
        .isTeamView()? ResourceManager.getText(Text.SWITCH_DESIGN_VIEW)
        : ResourceManager.getText(Text.SWITCH_TEAM_VIEW),
        ResourceManager.getIcon(Icon.CHANGE_VIEW_ICON_SMALL)) {

      @Override
      public void actionPerformed(ActionEvent e) {
        DesignerItem.getTournamentViewer().setTeamView(
            !DesignerItem.getTournamentViewer().isTeamView());

      }
    }));
  }

  @Override
  public void show(Component invoker, int x, int y) {
    super.show(invoker, x, y);
  }

}
