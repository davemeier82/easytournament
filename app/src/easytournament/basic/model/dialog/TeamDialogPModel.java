package easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Random;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.model.PlayersTabPanelPModel;
import easytournament.basic.model.StaffTabPanelPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.filechooser.ImageFilter;
import easytournament.basic.util.filechooser.ImagePreview;
import easytournament.basic.util.filechooser.Utils;
import easytournament.basic.valueholder.Player;
import easytournament.basic.valueholder.Staff;
import easytournament.basic.valueholder.Team;
import easytournament.basic.valueholder.Tournament;

public class TeamDialogPModel extends Model {

  public static final int OK_ACTION = 0;
  public static final int CANCEL_ACTION = 1;
  public static final int LOGO_ACTION = 2;
  public static final int RESET_ICON_ACTION = 3;
  
  public static final String DISPOSE = "dispose";
 
  protected Team team;
  protected Team originalTeam;
  protected PlayersTabPanelPModel playersTabPM;
  protected StaffTabPanelPModel staffTabPM;
  protected boolean newTeam = false;
  protected ArrayListModel<Player> playerClones;
  protected ArrayListModel<Staff> staffClones;
  protected Set<Integer> randomPositions;
  
  protected Tournament t = Organizer.getInstance().getCurrentTournament();

  public TeamDialogPModel(Set<Integer> randomPositions) {
    this.randomPositions = randomPositions;
    this.team = new Team();
    this.playersTabPM = new PlayersTabPanelPModel(this.team);
    this.staffTabPM = new StaffTabPanelPModel(this.team);
    this.newTeam = true;
  }

  public TeamDialogPModel(Team team) {
    this.originalTeam = team;
    try {
      this.team = (Team) team.clone();
      playerClones = new ArrayListModel<Player>();
      for(Player p : team.getPlayers()){
        playerClones.add((Player) p.clone());
      }
      staffClones = new ArrayListModel<Staff>();
      for(Staff s : team.getStaff()){
        staffClones.add((Staff) s.clone());
      }
    }
    catch (CloneNotSupportedException e) {
      ErrorLogger.getLogger().throwing("TeamDialogPModel", "constructor", e);
      ErrorDialog ed = new ErrorDialog(
          Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }
    this.playersTabPM = new PlayersTabPanelPModel(this.team);
    this.staffTabPM = new StaffTabPanelPModel(this.team);
  }

  public Action getAction(int a) {
    switch (a) {
      case OK_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.OK)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            if(TeamDialogPModel.this.newTeam){
              Random r = new Random();
              int randPos = r.nextInt(Integer.MAX_VALUE);
              while(randomPositions.contains(randPos)){
                randPos++;
              }
              team.setRandomPosition(randPos);
              randomPositions.add(randPos);
              t.getTeams().add(team);
              t.getUnassignedteams().add(team);
            }else {
              originalTeam.setTeam(team);     
            }
            Organizer.getInstance().setSaved(false);
            TeamDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case CANCEL_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.CANCEL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            if(!TeamDialogPModel.this.newTeam){
              ArrayListModel<Player> ps = originalTeam.getPlayers();
              for(int i = 0; i < playerClones.getSize(); i++){
                ps.get(i).setPlayer(playerClones.get(i));
              }
              ArrayListModel<Staff> ss = originalTeam.getStaff();
              for(int i = 0; i < staffClones.getSize(); i++){
                ss.get(i).setStaff(staffClones.get(i));
              }
            }
            
            TeamDialogPModel.this.firePropertyChange(DISPOSE, "1", "2");
          }
        };
      case LOGO_ACTION:
        return new AbstractAction(null, team.getLogo()) {

          @Override
          public void actionPerformed(ActionEvent e) {

            JFileChooser fc = new JFileChooser();

            fc.setFileFilter(new ImageFilter());
            fc.setAcceptAllFileFilterUsed(false);

            fc.setAccessory(new ImagePreview(fc));

            int returnVal = fc.showDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager.getText(Text.SELECT_LOGO));

            if (returnVal == JFileChooser.APPROVE_OPTION) {
              File file = fc.getSelectedFile();
              if(file != null){
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                
                double scale = Math.min(200.0/icon.getIconWidth(), 200.0/icon.getIconHeight()); //TODO do not hardcode size
                
                ImageIcon scaled = Utils.getScaledImage(icon, scale);
                putValue(Action.SMALL_ICON, scaled);
                putValue(Action.SHORT_DESCRIPTION, null);
                team.setLogo(scaled);
              }
            }
          }
        };
      case RESET_ICON_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.RESET_LOGO),
            ResourceManager.getIcon(easytournament.basic.resources.Icon.DELETE_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            putValue(Action.SMALL_ICON, null);
            putValue(Action.SHORT_DESCRIPTION, ResourceManager.getText(Text.SELECT_LOGO));
            team.setLogo(null);
          }
        };
    }
    return null;
  }

  public ValueModel getTeamValueModel(String propertyName) {
    return new PropertyAdapter<Team>(team, propertyName);
  }

  public PlayersTabPanelPModel getPlayersTabPanelPModel() {
    return this.playersTabPM;
  }
  
  public StaffTabPanelPModel getStaffTabPanelPModel() {
    return this.staffTabPM;
  }
}
