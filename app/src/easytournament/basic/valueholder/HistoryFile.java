package easytournament.basic.valueholder;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class HistoryFile implements Serializable {

  protected String name;
  protected String sport;
  protected String sportid;
  protected Date lastModified;
  protected File path;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getSport() {
    return sport;
  }
  public void setSport(String sport) {
    this.sport = sport;
  }
  public String getSportid() {
    return sportid;
  }
  public void setSportid(String sportid) {
    this.sportid = sportid;
  }
  public Date getLastModified() {
    return lastModified;
  }
  public void setLastModified(Date date) {
    this.lastModified = date;
  }
  public File getPath() {
    return path;
  }
  public void setPath(File path) {
    this.path = path;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((path == null)? 0 : path.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HistoryFile other = (HistoryFile)obj;
    if (path == null) {
      if (other.path != null)
        return false;
    }
    else if (!path.equals(other.path))
      return false;
    return true;
  }
  
  
}
