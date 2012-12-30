package com.easytournament.basic.valueholder;

import java.util.Date;

import javax.swing.ImageIcon;

import com.jgoodies.binding.beans.Model;


public class Person extends Model implements Cloneable {
  
  public static final short MALE = 0;
  public static final short FEMALE = 1;
  
  public static final String PROPERTY_GENDER = "gender";
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_PRENAME = "prename";
  public static final String PROPERTY_ADDRESS = "address";
  public static final String PROPERTY_PHONE = "phone";
  public static final String PROPERTY_EMAIL = "email";
  public static final String PROPERTY_BDATE = "bdate";
  public static final String PROPERTY_PICTURE = "picture";
  public static final String PROPERTY_NOTES = "notes";
  
  protected short gender  = MALE;
  protected String name;
  protected String prename;
  protected String address;
  protected String phone;
  protected String email;
  protected Date bdate;
  protected ImageIcon picture;  
  protected String notes;

  public Person(String name, String prename, String address,
      Date bdate, short gender, String phone, String email, String notes, ImageIcon picture) {
    this.gender = gender;
    this.name = name;
    this.prename = prename;
    this.address = address;
    this.bdate = bdate;
    this.picture = picture;
    this.phone = phone;
    this.email = email;
    this.notes = notes;
  }

  public Person() { 
    this("", "", "", new Date(0), (short)0, "", "", "", null);
  }
  
  public void setPerson(Person p){
    this.setGender(p.gender);
    this.setName(p.name);
    this.setPrename(p.prename);
    this.setAddress(p.address);
    this.setBdate(p.bdate);
    this.setPicture(p.picture);
    this.setPhone(p.phone);
    this.setEmail(p.email);
    this.setNotes(p.notes);
  }

  public Date getBdate() {
    return bdate;
  }

  public void setBdate(Date bdate) {
    Date old = this.bdate;
    this.bdate = bdate;
    this.firePropertyChange(PROPERTY_BDATE, old, this.bdate);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    this.firePropertyChange(PROPERTY_NAME, old, this.name);
  }

  public ImageIcon getPicture() {
    return picture;
  }

  public void setPicture(ImageIcon picture) {
    ImageIcon old = this.picture;
    this.picture = picture;
    this.firePropertyChange(PROPERTY_PICTURE, old, this.picture);
  }

  public String getPrename() {
    return prename;
  }

  public void setPrename(String prename) {
    String old = this.prename;
    this.prename = prename;
    this.firePropertyChange(PROPERTY_PRENAME, old, this.prename);
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    String old = this.address;
    this.address = address;
    this.firePropertyChange(PROPERTY_ADDRESS, old, this.address);
  }

  public short getGender() {
    return gender;
  }

  public void setGender(short gender) {
    short old = this.gender;
    this.gender = gender;
    this.firePropertyChange(PROPERTY_GENDER, old, this.gender);
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    String old = this.phone;
    this.phone = phone;
    this.firePropertyChange(PROPERTY_PHONE, old, this.phone);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    String old = this.email;
    this.email = email;
    this.firePropertyChange(PROPERTY_EMAIL, old, this.email);
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    String old = this.notes;
    this.notes = notes;
    this.firePropertyChange(PROPERTY_NOTES, old, this.notes);
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

}
