package easytournament.tournament.model.listmodel;

import java.util.Collections;

import javax.swing.AbstractListModel;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.util.comperator.TeamNameComperator;
import easytournament.basic.valueholder.Team;

public class TeamListModel extends AbstractListModel<String> {

  private static final long serialVersionUID = -4419435795695572019L;
  private ArrayListModel<Team> data;
  private boolean ordered;
  private TeamNameComperator tnc = new TeamNameComperator();

  public TeamListModel(ArrayListModel<Team> data, boolean ordered) {
    super();
    this.data = data;
    this.ordered = ordered;
    if(this.ordered)
      Collections.sort(data, tnc);
  }

  public Team getTeamAt(int index) {
    if(index < data.size())
    {
      return data.get(index);
    }
    return null;
  }

  public void addRow(Team team) {
    data.add(team);
    if(this.ordered)
      Collections.sort(data, tnc);
    this.fireContentsChanged(this, 0, data.size());
  }

  public void addRow(int index, Team team) {
    data.add(index, team);
    if(this.ordered)
      Collections.sort(data, tnc);
    this.fireContentsChanged(this, 0, data.size());
  }

  public void removeRow(int row) {
    data.remove(row);
    this.fireContentsChanged(this, 0, data.size());
  }

  public void removeElement(Team team) {
    data.remove(team);
    this.fireContentsChanged(this, 0, data.size());
  }

  @Override
  public int getSize() {
    return data.size();
  }

  @Override
  public String getElementAt(int index) {
    return data.get(index).getName();
  }

}
