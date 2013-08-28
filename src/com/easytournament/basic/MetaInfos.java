/* MetaInfos.java - Meta Informations Class
 * Copyright (c) 2013 David Meier
 * david.meier@easy-tournament.com
 * www.easy-tournament.com
 * 
 * This source code must not be used, copied or modified in any way 
 * without the permission of David Meier.
 */

package com.easytournament.basic;

/**
 * This class defines some important application based constants
 * @author David Meier
 */
public class MetaInfos {
  /**
   * The name of the application
   */
  public static final String APP_NAME = "EasyTournament";
  /**
   * URL to website of the application
   */
  public static final String APP_WEBSITE = "www.easy-tournament.com";
  /**
   * the author of the application
   */
  public static final String AUTHOR = "David Meier";
  /**
   * main version number
   */
  public static final int VERSION_MAIN = 0;
  /**
   * major version number (new features)
   */
  public static final int VERSION_MAJOR = 8;
  /**
   * minor version number (bugfixes)
   */
  public static final int VERSION_MINOR = 0;

  /**
   * save file application identifier
   */
  public static final String FILE_APPLICATION = "easytournament";
  /**
   * tournament save file type
   */
  public static final String FILE_MAINFILETYPE = "tournament";
  /**
   * player save file type
   */
  public static final String FILE_PLAYERFILETYPE = "players";
  /**
   * refree save file type
   */
  public static final String FILE_REFREEFILETYPE = "refrees";
  /**
   * staff save file type
   */
  public static final String FILE_STAFFFILETYPE = "staff";
  /**
   * assistance save file type
   */
  public static final String FILE_ASSISTFILETYPE = "assistants";
  /**
   * team save file type
   */
  public static final String FILE_TEAMFILETYPE = "teams";
  /**
   * settings save file type
   */
  public static final String FILE_SETTINGFILETYPE = "settings";
  /**
   * plan / diagram save file type
   */
  public static final String FILE_PLANFILETYPE = "plan";

  /**
   * save file ending
   */
  public static final String FILE_MAINFILEENDING = "ett";

  /**
   * Returns the application version number as string
   * @return application version string
   */
  public static String getVersionNr() {
    return VERSION_MAIN + "." + VERSION_MAJOR + "." + VERSION_MINOR;
  }

  /**
   * Returns the version of the xml (save) file as string
   * @return version of xml save file
   */
  public static String getXMLFileVersion() {
    return "0.1.2";
  }

  /**
   * Compares two version number strings
   * @param version1
   *          the first version number
   * @param version2
   *          the second version number
   * @return -1 if version1 is smaller than version2, 0 if both are equal, 1
   *         else
   */
  public static int compareVersionNr(String version1, String version2) {
    String[] partsV1 = version1.split("\\.");
    String[] vpartV2 = version2.split("\\.");
    Integer mainV1 = Integer.valueOf(partsV1[0]);
    Integer mainV2 = Integer.valueOf(vpartV2[0]);
    if (mainV1.equals(mainV2)) {
      // main version numbers are equal -> compare major
      Integer majorV1 = Integer.valueOf(partsV1[1]);
      Integer majorV2 = Integer.valueOf(vpartV2[1]);
      if (majorV1.equals(majorV2)) {
        // major version numbers are equal -> compare minor
        return Integer.valueOf(partsV1[2]).compareTo(
            Integer.valueOf(vpartV2[2]));
      }
      // return -1 if version1 < version2, else +1
      return majorV1.compareTo(majorV2);
    }
    // return -1 if version1 < version2, else +1
    return mainV1.compareTo(mainV2);
  }
}
