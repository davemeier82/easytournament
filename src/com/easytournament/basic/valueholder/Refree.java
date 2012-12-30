package com.easytournament.basic.valueholder;

import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.common.collect.ArrayListModel;

public class Refree extends Person implements ListDataListener {

  public static final String PROPERTY_FUNCTION = "function";
  public static final String PROPERTY_ASSIS = "assistants";

  public static int CURRENT_MAX_ID = 0;

  protected String function;
  protected ArrayListModel<Refree> assistants = new ArrayListModel<Refree>();
  protected int id;

  public Refree() {
    this("", "", "", new Date(0), (short)0, "", "", "", null, "");
  }

  public Refree(int id) {
    this(id, "", "", "", new Date(0), (short)0, "", "", "", null, "");
  }

  public Refree(String name, String prename, String address, Date bdate,
      short gender, String phone, String email, String notes,
      ImageIcon picture, String function) {
    this(CURRENT_MAX_ID++, name, prename, address, bdate, gender, phone, email,
        notes, picture, function);
  }

  public Refree(int id, String name, String prename, String address,
      Date bdate, short gender, String phone, String email, String notes,
      ImageIcon picture, String function) {
    super(name, prename, address, bdate, gender, phone, email, notes, picture);
    this.function = function;
    this.id = id;
    this.assistants.addListDataListener(this);
  }

  public void setRefree(Refree r) {
    super.setPerson(r);
    this.setFunction(r.function);
    this.setAssistants(r.getAssistants());
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    String old = this.function;
    this.function = function;
    this.firePropertyChange(PROPERTY_FUNCTION, old, this.function);
  }

  public ArrayListModel<Refree> getAssistants() {
    return assistants;
  }

  public void setAssistants(ArrayListModel<Refree> assistants) {
    this.assistants.removeListDataListener(this);
    ArrayListModel<Refree> old = this.assistants;
    this.assistants = assistants;
    this.assistants.addListDataListener(this);
    this.firePropertyChange(PROPERTY_ASSIS, old, this.assistants);
  }

  public int getId() {
    return id;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    Refree clone = (Refree)super.clone();
    ArrayListModel<Refree> rs = new ArrayListModel<Refree>();
    for (Refree r : this.assistants) {
      rs.add((Refree)r.clone());
    }
    clone.setAssistants(rs);
    return clone;
  }

  @Override
  public boolean equals(Object obj) {
    try {
      return id == ((Refree)obj).getId();
    }
    catch (Exception ex) {
      return false;
    }
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    this.firePropertyChange(PROPERTY_ASSIS, null, this.assistants);
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
    this.firePropertyChange(PROPERTY_ASSIS, null, this.assistants);
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
    this.firePropertyChange(PROPERTY_ASSIS, null, this.assistants);
  }

  public void updateId() {
    this.id = Refree.CURRENT_MAX_ID++;
  }

  @Override
  public String toString() {
    if (this.prename.length() > 0)
      return this.prename.substring(0, 1) + ". " + this.name;
    return this.name;
  }

}
