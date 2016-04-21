package easytournament.basic.util.comperator;

import java.io.Serializable;

import easytournament.tournament.valueholder.TableEntry;

public class IntegerComparable implements Comparable<IntegerComparable>, Serializable {

  private Integer i;
  private TableEntry te;
  private boolean ascending;

  public IntegerComparable(Integer i, TableEntry te, boolean ascending) {
    this.i = i;
    this.te = te;
    this.ascending = ascending;
  }

  public int compareTo(IntegerComparable ic) {

    if (ascending) {
      if (i > ic.getI())
        return 1;
      if (i < ic.getI())
        return -1;
      return 0;
    }
    if (i < ic.getI())
      return 1;
    if (i > ic.getI())
      return -1;
    return 0;
  }

  public Integer getI() {
    return i;
  }

  public TableEntry getTe() {
    return te;
  }

}
