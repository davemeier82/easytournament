package easytournament.designer.xml;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jdom.Element;

import com.jgoodies.common.collect.ArrayListModel;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import easytournament.basic.Organizer;
import easytournament.basic.gui.dialog.ErrorDialog;
import easytournament.basic.logging.ErrorLogger;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.valueholder.Team;
import easytournament.basic.xml.RulesXMLHandler;
import easytournament.basic.xml.TeamsXMLHandler;
import easytournament.designer.TournamentViewer;
import easytournament.designer.gui.jgraph.DuellGroupCell;
import easytournament.designer.gui.jgraph.GroupCell;
import easytournament.designer.valueholder.AbstractGroup;
import easytournament.designer.valueholder.DuellGroup;
import easytournament.designer.valueholder.Group;
import easytournament.designer.valueholder.GroupType;
import easytournament.designer.valueholder.Position;
import easytournament.designer.valueholder.TournamentPlan;

public class DesignerXMLHandler {

  public static HashMap<String,Position> positionMap = new HashMap<String,Position>();

  public static void save(Element xml) {
    save(xml, false);
  }

  public static void exportPlan(Element xml) {
    save(xml, true);
  }

  private static void save(Element xml, boolean export) {

    Element plan = new Element("plan");

    easytournament.basic.valueholder.Tournament tourn = Organizer
        .getInstance().getCurrentTournament();

    for (AbstractGroup g : tourn.getPlan().getGroups()) {
      Element group = new Element("group");
      group.setAttribute("name", g.getName());
      group.setAttribute("id", g.getId() + "");
      group.setAttribute("defaultsettings", g.isDefaultSettings()? "1" : "0");
      group.setAttribute("defaultrules", g.isDefaultRules()? "1" : "0");
      
      ArrayList<Team> teams = g.getTeams();
      if (g instanceof Group) {
        group.setAttribute("type", GroupType.NORMAL.toString());
      }
      else {
        group.setAttribute("type", GroupType.DUELL.toString());
      }
      Element positions = new Element("positions");
      for (Position tm : g.getPositions()) {
        Element position = new Element("position");
        position.setAttribute("name", tm.getName());
        position.setAttribute("id", tm.getId() + "");
        for (Position p : tm.getNext()) {
          if(p != null) {
            Element nextElement = new Element("next");
            nextElement.setAttribute("id", p.getId() + "");
            position.addContent(nextElement);
          }
        }
        Position prev = tm.getPrev();
        if (prev != null) {
          position.setAttribute("prev", prev.getId() + "");
        }
        if (!export) {
          Team temp = tm.getTeam();
          if (temp != null)
            position.setAttribute("team", temp.getId() + "");
        }

        positions.addContent(position);
      }
      group.addContent(positions);
      if (g.isDefaultSettings() || g.getSettings() == null) {
        RulesXMLHandler.saveTSettings(group, tourn.getSettings());
      }
      else {
        RulesXMLHandler.saveTSettings(group, g.getSettings());
      }
      if (g.isDefaultRules()) {
        RulesXMLHandler.saveRules(group, tourn.getDefaultRules());
      }
      else {
        RulesXMLHandler.saveRules(group, g.getRules());
      }

      plan.addContent(group);
    }
    xml.addContent(plan);

    try {
      Object root = TournamentViewer.getGraphComponent().getGraph().getModel()
          .getRoot();
      if (root != null) {
        Element gui = new Element("gui");
        saveGuiElements(gui, (mxCell)root);
        xml.addContent(gui);
      }
    }
    catch (NullPointerException e) {
      ErrorLogger.getLogger().throwing("DesignerXMLHandler", "save", e);
      ErrorDialog ed = new ErrorDialog(Organizer.getInstance().getMainFrame(),
          ResourceManager.getText(Text.ERROR), e.toString(), e);
      ed.setVisible(true);
      e.printStackTrace();
    }

    savePageFormat(xml);

  }

  private static void savePageFormat(Element xml) {
    PageFormat pageFormat = TournamentViewer.getGraphComponent()
        .getPageFormat();
    Element pageEl = new Element("pageformat");
    pageEl.setAttribute("orientation", pageFormat.getOrientation() + "");
    Paper paper = pageFormat.getPaper();
    Element paperEl = new Element("paper");
    paperEl.setAttribute("width", paper.getWidth() + "");
    paperEl.setAttribute("height", paper.getHeight() + "");
    paperEl.setAttribute("imageablex", paper.getImageableX() + "");
    paperEl.setAttribute("imageabley", paper.getImageableY() + "");
    paperEl.setAttribute("imageablewidth", paper.getImageableWidth() + "");
    paperEl.setAttribute("imageableheight", paper.getImageableHeight() + "");

    pageEl.addContent(paperEl);
    xml.addContent(pageEl);
  }

  public static void open(Element xml) {
    open(xml, false);
  }

  public static void importPlan(Element xml) {
    open(xml, true);
  }

  public static void open(Element xml, boolean importing) {
    TournamentPlan plan = Organizer.getInstance().getCurrentTournament()
        .getPlan();
    AbstractGroup.CURRENT_MAX_ID = 0;
    Position.CURRENT_MAX_ID = 0;

    HashMap<String,ArrayList<Position>> nexts = new HashMap<String,ArrayList<Position>>();
    HashMap<String,ArrayList<Position>> prevs = new HashMap<String,ArrayList<Position>>();
    HashMap<Integer,AbstractGroup> groupmap = new HashMap<Integer,AbstractGroup>();
    ArrayList<Team> assignedTeams = new ArrayList<Team>();
    positionMap.clear();
    HashMap<String,Team> teammap = TeamsXMLHandler.teammap;

    Element planElement = xml.getChild("plan");

    for (Object o : planElement.getChildren("group")) {
      Element g = (Element)o;

      Element positionsEl = g.getChild("positions");
      List<Element> positionlist = positionsEl.getChildren("position");
      AbstractGroup tempgroup = null;

      if (g.getAttributeValue("type").equals(GroupType.NORMAL.toString())) {
        tempgroup = new Group(g.getAttributeValue("name"), positionlist.size());
      }
      else {
        tempgroup = new DuellGroup(g.getAttributeValue("name"), true);
      }

      Integer groupId = Integer.valueOf(g.getAttributeValue("id"));
      tempgroup.setDefaultSettings(g.getAttributeValue("defaultsettings")
          .equals("1"));
      tempgroup
          .setDefaultRules(g.getAttributeValue("defaultrules").equals("1"));

      int i = 0;
      for (Element posElement : positionlist) {
        Position position = tempgroup.getPosition(i++);
        position.setName(posElement.getAttributeValue("name"));
        position.setGroup(tempgroup);

        positionMap.put(posElement.getAttributeValue("id"), position);

        for (Object no : posElement.getChildren("next")) {
          Element nextElem = (Element)no;
          String nextId = nextElem.getAttributeValue("id");
          if (nextId != null) {
            if (nexts.containsKey(nextId)) {
              nexts.get(nextId).add(position);
            }
            else {
              ArrayList<Position> al = new ArrayList<Position>();
              al.add(position);
              nexts.put(nextId, al);
            }
          }
        }
        String prev = posElement.getAttributeValue("prev");
        if (prev != null) {
          if (prevs.containsKey(prev)) {
            prevs.get(prev).add(position);
          }
          else {
            ArrayList<Position> al = new ArrayList<Position>();
            al.add(position);
            prevs.put(prev, al);
          }
        }
        String tm = posElement.getAttributeValue("team");
        if (!importing && tm != null) {
          if (teammap.containsKey(tm)) {
            Team temp = teammap.get(tm);
            position.setTeam(temp);
            tempgroup.getTeams().add(temp);
            assignedTeams.add(temp);
          }
        }
      }
      groupmap.put(groupId, tempgroup);
      tempgroup.setSettings(RulesXMLHandler.readSettings(g));
      tempgroup.setRules(RulesXMLHandler.readRules(g));
      plan.addGroup(tempgroup);
    }

    easytournament.basic.valueholder.Tournament t = Organizer.getInstance()
        .getCurrentTournament();
    if (!importing) {
      ArrayListModel<Team> unassignedteams = new ArrayListModel<Team>(
          teammap.values());
      unassignedteams.removeAll(assignedTeams);
      t.setUnassignedteams(unassignedteams);
    }

    t.setPlan(plan);

    readGUI(xml, groupmap);

    for (String key : nexts.keySet()) {
      for (Position tm : nexts.get(key)) {
        Position n = positionMap.get(key);
        tm.addNext(n);
      }
    }
    for (String key : prevs.keySet()) {
      for (Position tm : prevs.get(key)) {
        tm.setPrev(positionMap.get(key));
      }
    }
    readPageFormat(xml);
  }

  private static void readPageFormat(Element xml) {
    mxGraphComponent gcomp = TournamentViewer.getGraphComponent();
    PageFormat pageFormat = gcomp.getPageFormat();

    Element pageEl = xml.getChild("pageformat");
    pageFormat.setOrientation(Integer.parseInt(pageEl
        .getAttributeValue("orientation")));
    Paper paper = pageFormat.getPaper();
    Element paperEl = pageEl.getChild("paper");
    paper.setSize(Double.parseDouble(paperEl.getAttributeValue("width")),
        Double.parseDouble(paperEl.getAttributeValue("height")));
    double x = Double.parseDouble(paperEl.getAttributeValue("imageablex"));
    double y = Double.parseDouble(paperEl.getAttributeValue("imageabley"));
    double w = Double.parseDouble(paperEl.getAttributeValue("imageablewidth"));
    double h = Double.parseDouble(paperEl.getAttributeValue("imageableheight"));
    paper.setImageableArea(x, y, w, h);
    pageFormat.setPaper(paper);
    gcomp.setPageFormat(pageFormat);
    gcomp.zoomAndCenter();
  }

  private static void readGUI(Element xml,
      HashMap<Integer,AbstractGroup> groupmap) {
    mxGraphComponent gcomp = TournamentViewer.getGraphComponent();
    mxGraph graph = gcomp.getGraph();
    Element guiElement = xml.getChild("gui");
    HashMap<Integer,mxICell> posCellmap = new HashMap<Integer,mxICell>();

    HashMap<Integer,ArrayList<mxICell>> orderingMap = new HashMap<Integer,ArrayList<mxICell>>();
    HashMap<Integer,mxICell> cells = new HashMap<Integer,mxICell>();
    ArrayList<Integer> edges = new ArrayList<Integer>();

    for (Object o : guiElement.getChildren()) {
      Element cellElement = (Element)o;
      if (cellElement.getName().equals("groupcell")) {
        readGroupCell(groupmap, posCellmap, orderingMap, cells, cellElement);
      }
      else {
        readCell(orderingMap, cells, edges, cellElement);
      }
    }

    Set<Integer> keys = orderingMap.keySet();
    Integer[] parents = keys.toArray(new Integer[keys.size()]);
    Arrays.sort(parents);
    mxICell root = (mxICell)graph.getCurrentRoot();

    for (Integer i : parents) {
      if (i.intValue() == 1) {
        for (mxICell c : orderingMap.get(i)) {
          graph.addCell(c, root);
        }
      }
      else {
        for (mxICell c : orderingMap.get(i)) {
          graph.addCell(c, cells.get(i));
        }
      }
    }

    for (int i = 0; i < edges.size(); i++) {
      mxCell edge = (mxCell)cells.get(edges.get(i++));
      mxICell source = posCellmap.get(edges.get(i++));
      mxICell target = posCellmap.get(edges.get(i));

      if (source != null) {
        edge.setSource(source);
        source.insertEdge(edge, true);
      }
      if (target != null) {
        edge.setTarget(target);
        target.insertEdge(edge, false);
      }
    }

    graph.refresh();
    gcomp.repaint();

  }

  private static void readCell(HashMap<Integer,ArrayList<mxICell>> orderingMap,
      HashMap<Integer,mxICell> cells, ArrayList<Integer> edges,
      Element cellElement) {
    Element geomElement = cellElement.getChild("geometry");

    mxGeometry geom = null;
    if (geomElement != null) {
      geom = new mxGeometry();

      List<mxPoint> pointList = new ArrayList<mxPoint>();

      Element pointsElement = geomElement.getChild("points");
      for (Object po : pointsElement.getChildren("point")) {
        Element pointElement = (Element)po;
        pointList.add(new mxPoint(Double.parseDouble(pointElement
            .getAttributeValue("x")), Double.parseDouble(pointElement
            .getAttributeValue("y"))));
      }
      geom.setPoints(pointList);

      Element sourceElement = geomElement.getChild("source");
      Element targetElement = geomElement.getChild("target");
      Element offsetElement = geomElement.getChild("offset");

      if (sourceElement != null)
        geom.setSourcePoint(new mxPoint(Double.parseDouble(sourceElement
            .getAttributeValue("x")), Double.parseDouble(sourceElement
            .getAttributeValue("y"))));
      if (targetElement != null)
        geom.setTargetPoint(new mxPoint(Double.parseDouble(targetElement
            .getAttributeValue("x")), Double.parseDouble(targetElement
            .getAttributeValue("y"))));
      if (offsetElement != null)
        geom.setOffset(new mxPoint(Double.parseDouble(offsetElement
            .getAttributeValue("x")), Double.parseDouble(offsetElement
            .getAttributeValue("y"))));
    }

    mxCell edge = new mxCell(cellElement.getAttributeValue("value"), geom,
        cellElement.getAttributeValue("style"));

    Integer id = Integer.valueOf(cellElement.getAttributeValue("id"));
    String source = cellElement.getAttributeValue("sourceid");
    String target = cellElement.getAttributeValue("targetid");
    if (source != null && target != null) {
      edges.add(id);
      edges.add(Integer.valueOf(source));
      edges.add(Integer.valueOf(target));
    }

    edge.setEdge(cellElement.getAttributeValue("edge").equals("1"));
    edge.setVertex(cellElement.getAttributeValue("vertex").equals("1"));

    String parentStr = cellElement.getAttributeValue("parent");
    if (!parentStr.equals("none")) {
      Integer parent = Integer.valueOf(parentStr);
      if (orderingMap.containsKey(parent)) {
        orderingMap.get(parent).add(edge);
      }
      else {
        ArrayList<mxICell> list = new ArrayList<mxICell>();
        list.add(edge);
        orderingMap.put(parent, list);
      }
    }
    cells.put(id, edge);
  }

  private static void readGroupCell(HashMap<Integer,AbstractGroup> groupmap,
      HashMap<Integer,mxICell> posCellmap,
      HashMap<Integer,ArrayList<mxICell>> orderingMap,
      HashMap<Integer,mxICell> cells, Element cellElement) {
    double x = Double.parseDouble(cellElement.getAttributeValue("x"));
    double y = Double.parseDouble(cellElement.getAttributeValue("y"));
    double width = Double.parseDouble(cellElement.getAttributeValue("width"));
    double height = Double.parseDouble(cellElement.getAttributeValue("height"));
    double titleHeight = Double.parseDouble(cellElement
        .getAttributeValue("titleHeight"));
    mxGeometry geom = new mxGeometry(x, y, width, height);
    
    List<Element> posCellElements = cellElement.getChildren("positioncell");
    
    AbstractGroup group = groupmap.get(Integer.valueOf(cellElement
          .getAttributeValue("groupid")));
    
    if(posCellElements.size() != group.getNumPositions())
    {
      try {
        ((Group)group).setNumPositions(posCellElements.size());
      }
      catch(ClassCastException ex)
      {
       // it is a duell group and must therefore have 2 teams
        if(posCellElements.size() > group.getNumPositions())
        {
          while(posCellElements.size() > group.getNumPositions())
          {
            posCellElements.remove(posCellElements.size()-1);
          }
        }
        else
        {
          Exception tfex = new Exception("Too few elements in DuellGroup");
          ErrorLogger.getLogger().throwing("DesignerXMLHandler", "readGroupCell", tfex);
          ErrorDialog ed = new ErrorDialog(
              Organizer.getInstance().getMainFrame(),
              ResourceManager.getText(Text.ERROR), tfex.toString(), tfex);
          ed.setVisible(true);
          tfex.printStackTrace();
        }
      }      
    }      
    
    DuellGroupCell groupcell = null;
    if (cellElement.getAttributeValue("type").equals(
        GroupType.NORMAL.toString())) {
      groupcell = new GroupCell((Group)group, geom, titleHeight);
    }
    else {
      groupcell = new DuellGroupCell(group, geom, titleHeight);
    }
    groupcell.setStyle(cellElement.getAttributeValue("style"));
    Integer parent = Integer.valueOf(cellElement.getAttributeValue("parent"));
    if (orderingMap.containsKey(parent)) {
      orderingMap.get(parent).add(groupcell);
    }
    else {
      ArrayList<mxICell> list = new ArrayList<mxICell>();
      list.add(groupcell);
      orderingMap.put(parent, list);
    }
    cells.put(Integer.valueOf(cellElement.getAttributeValue("id")), groupcell);

    int i = 0;
    for (Element posCellElement : posCellElements) {
      posCellmap.put(Integer.valueOf(posCellElement.getAttributeValue("id")),
          groupcell.getChildAt(i));
      groupcell.setPositionStyle(i, posCellElement.getAttributeValue("style"));
      i++;
    }
  }

  private static void saveGuiElements(Element gui, mxICell cell) {
    if (cell instanceof DuellGroupCell) {
      DuellGroupCell gc = (DuellGroupCell)cell;
      Element e = new Element("groupcell");
      e.setAttribute("id", gc.getId());
      e.setAttribute("parent", cell.getParent() == null? "none" : cell
          .getParent().getId());
      e.setAttribute("groupid", Integer.toString(gc.getGroup().getId()));
      e.setAttribute("x", Double.toString(cell.getGeometry().getX()));
      e.setAttribute("y", Double.toString(cell.getGeometry().getY()));
      e.setAttribute("width", Double.toString(cell.getGeometry().getWidth()));
      e.setAttribute("height", Double.toString(cell.getGeometry().getHeight()));
      e.setAttribute("titleHeight",
          Double.toString(((DuellGroupCell)cell).getTitleHeigth()));
      e.setAttribute("style", cell.getStyle());
      e.setAttribute("type",
          gc instanceof GroupCell? GroupType.NORMAL.toString()
              : GroupType.DUELL.toString());
      for (int i = 0; i < cell.getChildCount(); i++) {
        Element pos = new Element("positioncell");
        mxICell p = cell.getChildAt(i);
        pos.setAttribute("id", p.getId());
        pos.setAttribute("style", p.getStyle());

        e.addContent(pos);
      }
      gui.addContent(e);

    }
    else if (cell instanceof mxCell) {
      mxCell mxcell = (mxCell)cell;

      Element e = new Element("cell");
      e.setAttribute("id", cell.getId());
      e.setAttribute("parent", cell.getParent() == null? "none" : cell
          .getParent().getId());
      if (cell.getValue() != null)
        e.setAttribute("value", cell.getValue().toString());
      if (cell.getStyle() != null)
        e.setAttribute("style", cell.getStyle());
      if (mxcell.getSource() != null)
        e.setAttribute("sourceid", mxcell.getSource().getId());
      if (mxcell.getTarget() != null)
        e.setAttribute("targetid", mxcell.getTarget().getId());

      e.setAttribute("edge", mxcell.isEdge()? "1" : "0");
      e.setAttribute("vertex", mxcell.isVertex()? "1" : "0");

      mxGeometry geom = cell.getGeometry();
      if (cell.getGeometry() != null) {
        Element geometry = new Element("geometry");

        Element points = new Element("points");
        List<mxPoint> pointList = geom.getPoints();
        if (pointList != null)
          for (mxPoint p : pointList) {
            Element point = new Element("point");
            point.setAttribute("x", p.getX() + "");
            point.setAttribute("y", p.getY() + "");
            points.addContent(point);
          }
        geometry.addContent(points);

        if (geom.getSourcePoint() != null) {
          Element source = new Element("source");
          source.setAttribute("x", geom.getSourcePoint().getX() + "");
          source.setAttribute("y", geom.getSourcePoint().getY() + "");
          geometry.addContent(source);
        }
        if (geom.getTargetPoint() != null) {
          Element target = new Element("target");
          target.setAttribute("x", geom.getTargetPoint().getX() + "");
          target.setAttribute("y", geom.getTargetPoint().getY() + "");
          geometry.addContent(target);
        }
        if (geom.getOffset() != null) {
          Element offset = new Element("offset");
          offset.setAttribute("x", geom.getOffset().getX() + "");
          offset.setAttribute("y", geom.getOffset().getY() + "");
          geometry.addContent(offset);
        }
        e.addContent(geometry);
      }

      gui.addContent(e);
      for (int i = 0; i < cell.getChildCount(); i++)
        saveGuiElements(gui, cell.getChildAt(i));
    }
    else {
      for (int i = 0; i < cell.getChildCount(); i++)
        saveGuiElements(gui, cell.getChildAt(i));
    }
  }

}
