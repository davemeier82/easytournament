package easytournament.basic.tournamentwizard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TournamentSelector {

  public static int getMaxNumberOfTeams() {
    return 32;
  }

  public static int getMinNumberOfTeams() {
    return 4;
  }

  public static int getMaxNumberOfNockoutStages(int nTeams, boolean fullStage) {
    if (nTeams < 1) {
      return 0;
    }
    if(fullStage){
      return (int)Math.floor(Math.log(nTeams) / Math.log(2));
    }
    return (int)Math.ceil(Math.log(nTeams) / Math.log(2));
  }

  public static List<Integer> getNumberOfGroups(int nTeams, int nNockoutStages) {
    int maxGroups = (int) Math.pow(2, nNockoutStages);
    List<Integer> groupsList = new ArrayList<Integer>();
    groupsList.add(1);
    if (nTeams > 5) {
      groupsList.add(2);
    }
    if (nTeams > 11 && maxGroups >= 4) {
      groupsList.add(4);
    }
    if (nTeams > 23 && maxGroups >= 8) {
      groupsList.add(8);
    }
    return groupsList;
  }

  public static File getTournamentFile(TournamentType type, int nTeams,
      int nGroups, int nNockout, boolean bronceGame) {
    String pathname = "templates/";

    switch (type) {
      case KNOCKOUT:
        pathname += "knockout-" + nTeams + "-" + bronceGame + ".xml";
        break;
      case GROUP_KNOCKOUT:
        pathname += "groupknockout-" + nTeams + "-" + nGroups + "-" + nNockout
            + "-" + bronceGame + ".xml";
        break;
      case GROUP:
        pathname += "group-" + nTeams + ".xml";
        break;
      default:
        return null;
    }
    File tournamentFile = new File(pathname);
    return tournamentFile;
  }
}
