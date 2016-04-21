package easytournament.basic.model;


import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;

import easytournament.basic.Organizer;
import easytournament.basic.valueholder.SportSettings;
import easytournament.basic.valueholder.Tournament;

public class TSSettingsPanelPModel extends Model {

  protected SportSettings settings;
  protected Tournament tournament;
  
  public TSSettingsPanelPModel(){
    this.tournament = Organizer.getInstance().getCurrentTournament();
    this.settings = tournament.getSettings();
  }

  public ValueModel getSettingsValueModel(String propertyName) {
    return new PropertyAdapter<SportSettings>(settings, propertyName, true);
    //TODO set sport to Edited true on changes...but how?
    //TODO set saved to false;
  }
}
