package com.easytournament.basic;

public class MetaInfos {
  public static final String APP_NAME = "EasyTournament";
  public static final String APP_WEBSITE = "www.easy-tournament.com";
  public static final String AUTHOR = "David Meier";
  public static final int VERSION_MAIN = 0;
  public static final int VERSION_MAJOR = 5;
  public static final int VERSION_MINOR = 6;

  public static final String FILE_APPLICATION = "easytournament";
  public static final String FILE_MAINFILETYPE = "tournament";
  public static final String FILE_PLAYERFILETYPE = "players";
  public static final String FILE_REFREEFILETYPE = "refrees";
  public static final String FILE_STAFFFILETYPE = "staff";
  public static final String FILE_ASSISTFILETYPE = "assistants";
  public static final String FILE_TEAMFILETYPE = "teams";
  public static final String FILE_SETTINGFILETYPE = "settings";
  public static final String FILE_PLANFILETYPE = "plan";

  public static final String FILE_MAINFILEENDING = "ett";

  public static String getVersionNr() {
    return VERSION_MAIN + "." + VERSION_MAJOR + "." + VERSION_MINOR;
  }
  
  public static String getXMLFileVersion(){
    return "0.1.2";
  }  

  public static int compareVersionNr(String a, String v) {
    String[] aparts = a.split("\\.");
    String[] vparts = v.split("\\.");
    Integer amain = Integer.valueOf(aparts[0]);
    Integer vmain = Integer.valueOf(vparts[0]);
    if (amain.equals(vmain)) {
      Integer amajor = Integer.valueOf(aparts[1]);
      Integer vmajor = Integer.valueOf(vparts[1]);
      if (amajor.equals(vmajor)) {
        return Integer.valueOf(aparts[2]).compareTo(Integer.valueOf(vparts[2]));
      }
      return amajor.compareTo(vmajor);
    }
    return amain.compareTo(vmain);
  }
}
