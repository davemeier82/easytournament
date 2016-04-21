package easytournament.basic.valueholder;

import java.util.Date;

import javax.swing.ImageIcon;

public class Player extends Person {

  public static final String PROPERTY_NR = "number";
  public static final String PROPERTY_POSITION = "position";

  public static int CURRENT_MAX_ID = 0;

  protected String number;
  protected String position;
  protected int id;

  public Player() {
    this("", "", "", new Date(0), (short)0, "", "", "", null, "", "");
  }

  public Player(int id) {
    this(id, "", "", "", new Date(0), (short)0, "", "", "", null, "", "");
  }

  public Player(String name, String prename, String address, Date bdate,
      short gender, String phone, String email, String notes,
      ImageIcon picture, String number, String position) {
    this(CURRENT_MAX_ID++, name, prename, address, bdate, gender, phone, email,
        notes, picture, number, position);
  }

  public Player(int id, String name, String prename, String address,
      Date bdate, short gender, String phone, String email, String notes,
      ImageIcon picture, String number, String position) {
    super(name, prename, address, bdate, gender, phone, email, notes, picture);
    this.number = number;
    this.position = position;
    this.id = id;
  }

  public void setPlayer(Player p) {
    this.setPerson(p);
    this.setNumber(p.number);
    this.setPosition(p.position);
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    String old = this.number;
    this.number = number;
    this.firePropertyChange(PROPERTY_NR, old, this.number);
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    String old = this.position;
    this.position = position;
    this.firePropertyChange(PROPERTY_POSITION, old, this.position);
  }

  public int getId() {
    return this.id;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Player) {
      return id == ((Player)obj).getId();
    }
    return false;
  }

  public void updateId() {
    this.id = Player.CURRENT_MAX_ID++;
  }

  @Override
  public String toString() {
    String name = "";
    if (this.prename.length() > 0)
      name += this.prename.substring(0, 1) + ". ";
    name += this.name;
    return name;

  }
}
