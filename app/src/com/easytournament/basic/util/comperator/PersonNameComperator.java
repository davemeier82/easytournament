package com.easytournament.basic.util.comperator;

import java.io.Serializable;
import java.util.Comparator;

import com.easytournament.basic.valueholder.Person;


public class PersonNameComperator implements Comparator<Person>, Serializable {

  @Override
  public int compare(Person p0, Person p1) {
    int i = p0.getName().toLowerCase().compareTo(p1.getName().toLowerCase()) ;
    if(i==0)
      return p0.getPrename().toLowerCase().compareTo(p1.getPrename().toLowerCase());
    return i;
  }

}
