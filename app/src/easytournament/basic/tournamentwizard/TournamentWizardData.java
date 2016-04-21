package easytournament.basic.tournamentwizard;

public class TournamentWizardData {

  private String name = "";
  private TournamentType type = TournamentType.GROUP_KNOCKOUT;
  private int nTeams = 16;
  private boolean addBronceMedalGame;
  private int nGroups = 1;
  private int nStages = 1;
  private boolean newTounament;

  public TournamentWizardData(boolean newTounament) {
    this.newTounament = newTounament;
  }

  /**
   * @return the type
   */
  public TournamentType getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(TournamentType type) {
    this.type = type;
  }

  /**
   * @return the nTeams
   */
  public int getnTeams() {
    return nTeams;
  }

  /**
   * @param nTeams
   *          the nTeams to set
   */
  public void setnTeams(int nTeams) {
    this.nTeams = nTeams;
  }

  /**
   * @return the addBronceMedalGame
   */
  public boolean isAddBronceMedalGame() {
    return addBronceMedalGame;
  }

  /**
   * @param addBronceMedalGame
   *          the addBronceMedalGame to set
   */
  public void setAddBronceMedalGame(boolean addBronceMedalGame) {
    this.addBronceMedalGame = addBronceMedalGame;
  }

  /**
   * @return the nGroups
   */
  public int getnGroups() {
    return nGroups;
  }

  /**
   * @param nGroups
   *          the nGroups to set
   */
  public void setnGroups(int nGroups) {
    this.nGroups = nGroups;
  }

  /**
   * @return the nStages
   */
  public int getnStages() {
    return nStages;
  }

  /**
   * @param nStages
   *          the nStages to set
   */
  public void setnStages(int nStages) {
    this.nStages = nStages;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the newTounament
   */
  public boolean isNewTounament() {
    return newTounament;
  }

}
