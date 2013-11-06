package com.easytournament.basic.model.listmodel;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataListener;


import com.easytournament.basic.valueholder.Player;
import com.jgoodies.common.collect.ArrayListModel;

public class PlayerListModel extends AbstractListModel<String> {

  ArrayListModel<Player> players;

  public PlayerListModel(ArrayListModel<Player> players) {
    this.players = players;
  }

  @Override
  public void addListDataListener(ListDataListener l) {
    players.addListDataListener(l);

  }

  @Override
  public int getSize() {
    return players.size();
  }

  @Override
  public void removeListDataListener(ListDataListener l) {
    players.removeListDataListener(l);

  }

  public ArrayListModel<Player> getData() {
    return players;
  }

  @Override
  public String getElementAt(int index) {
    Player p = this.players.get(index);
    if (p != null) {
      String name = p.getNumber().length()>0?"["+p.getNumber()+"] ":"";
      if (p.getPrename().length() > 0)
        name += p.getPrename().substring(0, 1) + ". ";
      name += p.getName();
      return name;
    }
    return "";
  }

}
