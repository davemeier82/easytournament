package com.easytournament.basic.valueholder;

import java.util.ArrayList;
import java.util.Date;

public class Goal {

  private Person scorer;
  private ArrayList<Person> assist;
  private Date time;
  private boolean owngoal;
  private boolean penalty; //tor im penalty-schiessen
  
  public boolean isPenalty() {
    return penalty;
  }



  public void setPenalty(boolean penalty) {
    this.penalty = penalty;
  }



  public Goal(Person scorer, Date time, boolean owngoal) {
    super();
    this.scorer = scorer;
    this.time = time;
    this.owngoal = owngoal;
  }
  
  
  
  public Goal(Person scorer, ArrayList<Person> assist, Date time,
      boolean owngoal) {
    super();
    this.scorer = scorer;
    this.assist = assist;
    this.time = time;
    this.owngoal = owngoal;
  }



  public Person getScorer() {
    return scorer;
  }
  public void setScorer(Person scorer) {
    this.scorer = scorer;
  }
  public ArrayList<Person> getAssist() {
    return assist;
  }
  public void setAssist(ArrayList<Person> assist) {
    this.assist = assist;
  }
  public Date getTime() {
    return time;
  }
  public void setTime(Date time) {
    this.time = time;
  }
  public boolean isOwngoal() {
    return owngoal;
  }
  public void setOwngoal(boolean owngoal) {
    this.owngoal = owngoal;
  }
}
