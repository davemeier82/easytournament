package easytournament.basic.xml;

import org.jdom.Element;

import com.jgoodies.common.collect.ArrayListModel;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.rule.RuleType;
import easytournament.basic.valueholder.Rule;
import easytournament.basic.valueholder.SportSettings;


public class RulesXMLHandler {
  
  public static void saveTSettings(Element xml, SportSettings ts){
    Element tSettingsEl = new Element("settings");
    Element game = new Element("game");
    game.setAttribute("numgametimes", ts.getNumGameTimes()+"");
    game.setAttribute("minpergametime", ts.getMinPerGameTime()+"");
    game.setAttribute("numovertimes", ts.getNumOvertimes()+"");
    game.setAttribute("minperovertime", ts.getMinPerOvertime()+"");
    tSettingsEl.addContent(game);
    Element points = new Element("points");
    Element win = new Element("win");
    win.setAttribute("home", ts.getPointPerHomeWin()+"");
    win.setAttribute("away", ts.getPointPerAwayWin()+"");
    points.addContent(win);
    Element draw = new Element("draw");
    draw.setAttribute("home", ts.getPointPerHomeDraw()+"");
    draw.setAttribute("away", ts.getPointPerAwayDraw()+"");
    points.addContent(draw);
    Element defeat = new Element("defeat");
    defeat.setAttribute("home", ts.getPointPerHomeDefeat()+"");
    defeat.setAttribute("away", ts.getPointPerAwayDefeat()+"");
    points.addContent(defeat);
    Element winot = new Element("winot");
    winot.setAttribute("home", ts.getPointPerHomeWinOvertime()+"");
    winot.setAttribute("away", ts.getPointPerAwayWinOvertime()+"");
    points.addContent(winot);
    Element drawot = new Element("drawot");
    drawot.setAttribute("home", ts.getPointPerHomeDrawOvertime()+"");
    drawot.setAttribute("away", ts.getPointPerAwayDrawOvertime()+"");
    points.addContent(drawot);
    Element defeatot = new Element("defeatot");
    defeatot.setAttribute("home", ts.getPointPerHomeDefeatOvertime()+"");
    defeatot.setAttribute("away", ts.getPointPerAwayDefeatOvertime()+"");
    points.addContent(defeatot);
    Element winp = new Element("winp");
    winp.setAttribute("home", ts.getPointPerHomeWinPenalty()+"");
    winp.setAttribute("away", ts.getPointPerAwayWinPenalty()+"");
    points.addContent(winp);
    Element defeatp = new Element("defeatp");
    defeatp.setAttribute("home", ts.getPointPerHomeDefeatPenalty()+"");
    defeatp.setAttribute("away", ts.getPointPerAwayDefeatPenalty()+"");
    points.addContent(defeatp);
    tSettingsEl.addContent(points);
    xml.addContent(tSettingsEl);
  }
  
  public static void saveRules(Element xml, ArrayListModel<Rule> rs){
    Element rulesEl = new Element("rules");
    for (Rule r : rs) {
      Element rule = new Element("rule");
      rule.setAttribute("rule", r.getRule().name());
      rule.setAttribute("ascending", String.valueOf(r.isAscending()));
      rulesEl.addContent(rule);
    }
    xml.addContent(rulesEl);
  }
  public static SportSettings readSettings(Element xml){
    SportSettings ts = new SportSettings();
    
    Element sSettingsEl = xml.getChild("settings");
    
    Element game = sSettingsEl.getChild("game");
    ts.setNumGameTimes(Integer.parseInt(game.getAttributeValue("numgametimes")));
    ts.setMinPerGameTime(Integer.parseInt(game.getAttributeValue("minpergametime")));
    ts.setNumOvertimes(Integer.parseInt(game.getAttributeValue("numovertimes")));
    ts.setMinPerOvertime(Integer.parseInt(game.getAttributeValue("minperovertime")));

    Element points = sSettingsEl.getChild("points");
    Element win = points.getChild("win");
    ts.setPointPerHomeWin(Integer.parseInt(win.getAttributeValue("home")));
    ts.setPointPerAwayWin(Integer.parseInt(win.getAttributeValue("away")));

    Element draw = points.getChild("draw");
    ts.setPointPerHomeDraw(Integer.parseInt(draw.getAttributeValue("home")));
    ts.setPointPerAwayDraw(Integer.parseInt(draw.getAttributeValue("away")));

    Element defeat = points.getChild("defeat");
    ts.setPointPerHomeDefeat(Integer.parseInt(defeat.getAttributeValue("home")));
    ts.setPointPerAwayDefeat(Integer.parseInt(defeat.getAttributeValue("away")));

    Element winot = points.getChild("winot");
    ts.setPointPerHomeWinOvertime(Integer.parseInt(winot.getAttributeValue("home")));
    ts.setPointPerAwayWinOvertime(Integer.parseInt(winot.getAttributeValue("away")));

    Element drawot = points.getChild("drawot");
    ts.setPointPerHomeDrawOvertime(Integer.parseInt(drawot.getAttributeValue("home")));
    ts.setPointPerAwayDrawOvertime(Integer.parseInt(drawot.getAttributeValue("away")));

    Element defeatot = points.getChild("defeatot");
    ts.setPointPerHomeDefeatOvertime(Integer.parseInt(defeatot.getAttributeValue("home")));
    ts.setPointPerAwayDefeatOvertime(Integer.parseInt(defeatot.getAttributeValue("away")));

    Element winp = points.getChild("winp");
    ts.setPointPerHomeWinPenalty(Integer.parseInt(winp.getAttributeValue("home")));
    ts.setPointPerAwayWinPenalty(Integer.parseInt(winp.getAttributeValue("away")));

    Element defeatp = points.getChild("defeatp");
    ts.setPointPerHomeDefeatPenalty(Integer.parseInt(defeatp.getAttributeValue("home")));
    ts.setPointPerAwayDefeatPenalty(Integer.parseInt(defeatp.getAttributeValue("away")));

    return ts;
  }
  
  public static ArrayListModel<Rule> readRules(Element xml){
    Element rulesEl = xml.getChild("rules");
    ArrayListModel<Rule> rules = new ArrayListModel<Rule>();
    for (Object po : rulesEl.getChildren("rule")) {
      Element rule = (Element) po;
      String rulename = rule.getAttributeValue("rule");
      boolean ascending = rule.getAttributeValue("ascending")
          .equals("true");
      Rule r;
      try {
        r = (Rule) Rule.ruleMap.get(RuleType.valueOf(rulename)).clone();
        r.setAscending(ascending);
        rules.add(r);
      }
      catch (Exception e) {
        ErrorLogger.getLogger().throwing("RulesXMLHandler", "readRules", e);
        ErrorDialog ed = new ErrorDialog(
            Organizer.getInstance().getMainFrame(),
            ResourceManager.getText(Text.ERROR), e.toString(), e);
        ed.setVisible(true);
        e.printStackTrace();
      }
    }
    return rules;
  }
}
