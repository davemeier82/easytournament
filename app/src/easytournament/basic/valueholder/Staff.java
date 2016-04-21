package easytournament.basic.valueholder;

import java.util.Date;

import javax.swing.ImageIcon;

public class Staff extends Person {
  
  public static final String PROPERTY_FUNCTION = "function";
  
  public static int CURRENT_MAX_ID = 0;
  
  protected String function;
  protected int id;

  public Staff() {
    this("", "", "", new Date(0), (short)0, "", "", "", null, "");
  }
  
  public Staff(int id) {
    this(id,"", "", "", new Date(0), (short)0, "", "", "", null, "");
  }

  public Staff(String name, String prename, String address, Date bdate,
      short gender, String phone, String email, String notes, ImageIcon picture, String function) {
    this(CURRENT_MAX_ID++,name, prename, address, bdate, gender, phone, email, notes, picture, function);
  }
  
  public Staff(int id, String name, String prename, String address, Date bdate,
      short gender, String phone, String email, String notes, ImageIcon picture, String function) {
    super(name, prename, address, bdate, gender, phone, email, notes, picture);
    this.function = function;
    this.id = id;
  }
  
  public void setStaff(Staff s){
    super.setPerson(s);
    this.setFunction(s.function);
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    String old = this.function;
    this.function = function;
    this.firePropertyChange(PROPERTY_FUNCTION, old, this.function);
  }

  public int getId() {
    return id;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Staff) {
      return id == ((Staff) obj).getId();
    }
    return false;   
  }

  public void updateId() {
    this.id = Staff.CURRENT_MAX_ID++;
  }  
  
}
