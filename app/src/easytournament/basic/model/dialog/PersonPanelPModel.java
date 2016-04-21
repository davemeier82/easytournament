package easytournament.basic.model.dialog;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.util.filechooser.ImageFilter;
import easytournament.basic.util.filechooser.ImagePreview;
import easytournament.basic.util.filechooser.Utils;
import easytournament.basic.valueholder.Person;

public class PersonPanelPModel extends Model {
  
  public static final String PROPERTY_GENDER = "gender";
  public static final int PICTURE_ACTION = 0;
  public static final int RESET_ICON_ACTION = 1;
  
  protected Person person;
  protected String gender;
  protected PropertyAdapter<PersonPanelPModel> genderAdapter;
  protected ArrayListModel<String> genders = new ArrayListModel<String>();
  protected SelectionInList<ArrayListModel<String>> genderSelection;

  public PersonPanelPModel(Person p) {
    this.person = p;
    this.genders.add(ResourceManager.getText(Text.MALE));
    this.genders.add(ResourceManager.getText(Text.FEMALE));
    this.gender = genders.get(person.getGender());
    
    this.genderAdapter = new PropertyAdapter<PersonPanelPModel>(this, PROPERTY_GENDER, true);
    this.genderSelection = new SelectionInList<ArrayListModel<String>>(genders, genderAdapter);
  }

  public ValueModel getPersonValueModel(String propertyName) {
    if(propertyName.equals(Person.PROPERTY_GENDER)){
      return genderAdapter;
    }
    return new PropertyAdapter<Person>(person, propertyName, true);
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
    this.person.setGender((short) genderSelection.getSelectionIndex());
  }
  
  public Action getAction(int action){
    switch(action){
      case PICTURE_ACTION:
        return new AbstractAction(null, person.getPicture()) {

          @Override
          public void actionPerformed(ActionEvent e) {

            JFileChooser fc = new JFileChooser();

            fc.setFileFilter(new ImageFilter());
            fc.setAcceptAllFileFilterUsed(false);

            fc.setAccessory(new ImagePreview(fc));

            int returnVal = fc.showDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager.getText(Text.SELECT_PICTURE));

            if (returnVal == JFileChooser.APPROVE_OPTION) {
              File file = fc.getSelectedFile();
              if(file != null){
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                
                double scale = Math.min(200.0/icon.getIconWidth(), 200.0/icon.getIconHeight()); //TODO do not hardcode size
                
                ImageIcon scaled = Utils.getScaledImage(icon, scale);
                putValue(Action.SMALL_ICON, scaled);
                putValue(Action.SHORT_DESCRIPTION, null);
                person.setPicture(scaled);
              }
            }
          }
        };
      case RESET_ICON_ACTION:
        return new AbstractAction(ResourceManager.getText(Text.RESET_PICTURE),
            ResourceManager.getIcon(easytournament.basic.resources.Icon.DELETE_ICON_SMALL)) {

          @Override
          public void actionPerformed(ActionEvent e) {
            putValue(Action.SMALL_ICON, null);
            putValue(Action.SHORT_DESCRIPTION, ResourceManager.getText(Text.SELECT_PICTURE));
            person.setPicture(null);
          }
        };
    }
    return null;
  }

  public SelectionInList<ArrayListModel<String>> getGenderSelectionInList() {
    return genderSelection;
  }  
}
