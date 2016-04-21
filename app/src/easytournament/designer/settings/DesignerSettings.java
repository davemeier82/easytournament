package easytournament.designer.settings;

import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

import org.jdom.Element;

import com.jgoodies.binding.beans.Model;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import easytournament.basic.model.dialog.SettingsDialogPModel;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.basic.settings.Settings;
import easytournament.basic.settings.SettingsTreeNode;
import easytournament.designer.gui.jgraph.DuellGroupCell;
import easytournament.designer.gui.settings.DesignerSettingsPanel;
import easytournament.designer.model.settings.DesignerSetPModel;
import easytournament.designer.navigationitem.DesignerItem;


public class DesignerSettings extends Model implements Settings {

  public static final String PROPERTY_GROUP_FONTSIZE = "groupFontSize";
  public static final String PROPERTY_GROUP_FONTTYPE = "groupFontType";
  private DesignerSetPModel pm;
  private DesignerSettingsPanel panel;
  private static DesignerSettings instance;

  protected String lineFontSize;
  protected String lineFontType;
  protected int lineendSize = 5;
  protected int lineWidth = 2;
  protected String lineendType = mxConstants.ARROW_CLASSIC;

  protected String groupFontSize;
  protected String groupFontType;
  protected int groupBorderWidth = 2;
  protected String groupHAlign = mxConstants.ALIGN_CENTER;
  protected String groupVAlign = mxConstants.ALIGN_MIDDLE;

  protected String posFontSize;
  protected String posFontType;
  protected int posBorderWidth = 2;
  protected String posHAlign = mxConstants.ALIGN_CENTER;
  protected String posVAlign = mxConstants.ALIGN_MIDDLE;

  protected String groupFontColor = "#000000";
  protected String groupBorderColor = "#5d65df";
  protected String groupFillColor = "#adc5ff";
  protected String groupGradientColor = "#7d85df";
  protected String posFontColor = "#000000";
  protected String posBorderColor = "#5d65df";
  protected String posFillColor = "#adc5ff";
  protected String posGradientColor = "#7d85df";
  protected String lineFontColor = "#000000";
  protected String lineColor = "#000000";

  private DesignerSettings() {}

  public static DesignerSettings getInstance() {
    if (instance == null)
      instance = new DesignerSettings();
    return instance;
  }

  private void init() {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    List<String> fonts = Arrays.asList(env.getAvailableFontFamilyNames());
    if (fonts.contains("Verdana"))
      this.setLineFontType("Verdana");
    else if (fonts.contains("Arial"))
      this.setLineFontType("Arial");
    else
      this.setLineFontType(fonts.get(0));

    this.setGroupFontType(lineFontType);
    this.setPosFontType(lineFontType);
    
    this.setLineFontSize("12");
    this.setGroupFontSize("13");
    this.setPosFontSize("12");

    save();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (pm != null && evt.getPropertyName() == SettingsDialogPModel.PROPERTY_OK) {
      pm.save();
      save();
      pm = null;
      panel = null;
    } if (pm != null && evt.getPropertyName() == SettingsDialogPModel.PROPERTY_CANCEL) { 
      pm = null;
      panel = null;
    }
  }

  @Override
  public SettingsTreeNode getNode() {
    SettingsTreeNode node = new SettingsTreeNode(ResourceManager.getText(Text.DESIGNER), this);
    node.add(GridSettings.getInstance().getNode());
    node.add(PageSettings.getInstance().getNode());
    return node;
  }

  @Override
  public JComponent getPanel() {

    if (panel == null) {
      pm = new DesignerSetPModel();
      panel = new DesignerSettingsPanel(pm);
    }
    return panel;
  }

  @Override
  public void read(Element xml) {
    if (xml == null) {
      init();
      GridSettings.getInstance().read(null);
      PageSettings.getInstance().read(null);
    }
    else {
      Element desEl = xml.getChild("designer");
      Element groupEl = desEl.getChild("group");
      groupFillColor = groupEl.getAttributeValue("fillcolor");
      groupBorderColor = groupEl.getAttributeValue("bordercolor");
      groupBorderWidth = Integer.parseInt(groupEl
          .getAttributeValue("borderwidth"));
      groupGradientColor = groupEl.getAttributeValue("gradientcolor");
      Element groupLabelEl = groupEl.getChild("label");
      groupHAlign = groupLabelEl.getAttributeValue("halign");
      groupVAlign = groupLabelEl.getAttributeValue("valign");
      Element groupFontEl = groupLabelEl.getChild("font");
      this.setGroupFontType(groupFontEl.getAttributeValue("type"));
      this.setGroupFontSize(groupFontEl.getAttributeValue("size"));
      groupFontColor = groupFontEl.getAttributeValue("color");

      Element posEl = desEl.getChild("position");
      posFillColor = posEl.getAttributeValue("fillcolor");
      posBorderColor = posEl.getAttributeValue("bordercolor");
      posBorderWidth = Integer.parseInt(posEl.getAttributeValue("borderwidth"));
      posGradientColor = posEl.getAttributeValue("gradientcolor");
      Element posLabelEl = posEl.getChild("label");
      posHAlign = posLabelEl.getAttributeValue("halign");
      posVAlign = posLabelEl.getAttributeValue("valign");
      Element posFontEl = posLabelEl.getChild("font");
      this.setPosFontType(posFontEl.getAttributeValue("type"));
      this.setPosFontSize(posFontEl.getAttributeValue("size"));
      posFontColor = posFontEl.getAttributeValue("color");

      Element lineEl = desEl.getChild("line");
      lineColor = lineEl.getAttributeValue("color");
      lineWidth = Integer.parseInt(lineEl.getAttributeValue("width"));
      lineendType = lineEl.getAttributeValue("endtype");
      lineendSize = Integer.parseInt(lineEl.getAttributeValue("endsize"));
      Element lineLabelEl = lineEl.getChild("label");
      Element lineFontEl = lineLabelEl.getChild("font");
      this.setLineFontType(lineFontEl.getAttributeValue("type"));
      this.setLineFontSize(lineFontEl.getAttributeValue("size"));
      lineFontColor = lineFontEl.getAttributeValue("color");
      
      GridSettings.getInstance().read(desEl);
      PageSettings.getInstance().read(desEl);
      }
  }

  @Override
  public void save(Element xml) {
    Element e = new Element("designer");
    Element groupEl = new Element("group");
    groupEl.setAttribute("fillcolor", groupFillColor);
    groupEl.setAttribute("bordercolor", groupBorderColor);
    groupEl.setAttribute("borderwidth", groupBorderWidth + "");
    groupEl.setAttribute("gradientcolor", groupGradientColor);
    Element groupLabelEl = new Element("label");
    groupLabelEl.setAttribute("halign", groupHAlign);
    groupLabelEl.setAttribute("valign", groupVAlign);
    Element groupFontEl = new Element("font");
    groupFontEl.setAttribute("type", groupFontType);
    groupFontEl.setAttribute("size", groupFontSize);
    groupFontEl.setAttribute("color", groupFontColor);
    groupLabelEl.addContent(groupFontEl);
    groupEl.addContent(groupLabelEl);
    e.addContent(groupEl);
    Element posEl = new Element("position");
    posEl.setAttribute("fillcolor", posFillColor);
    posEl.setAttribute("bordercolor", posBorderColor);
    posEl.setAttribute("borderwidth", posBorderWidth + "");
    posEl.setAttribute("gradientcolor", posGradientColor);
    Element posLabelEl = new Element("label");
    posLabelEl.setAttribute("halign", posHAlign);
    posLabelEl.setAttribute("valign", posVAlign);
    Element posFontEl = new Element("font");
    posFontEl.setAttribute("type", posFontType);
    posFontEl.setAttribute("size", posFontSize);
    posFontEl.setAttribute("color", posFontColor);
    posLabelEl.addContent(posFontEl);
    posEl.addContent(posLabelEl);
    e.addContent(posEl);
    Element lineEl = new Element("line");
    lineEl.setAttribute("color", lineColor);
    lineEl.setAttribute("width", lineWidth + "");
    lineEl.setAttribute("endtype", lineendType);
    lineEl.setAttribute("endsize", lineendSize + "");
    Element lineLabelEl = new Element("label");
    lineEl.addContent(lineLabelEl);
    Element lineFontEl = new Element("font");
    lineFontEl.setAttribute("type", lineFontType);
    lineFontEl.setAttribute("size", lineFontSize);
    lineFontEl.setAttribute("color", lineFontColor);
    lineLabelEl.addContent(lineFontEl);
    e.addContent(lineEl);
    GridSettings.getInstance().save(e);
    PageSettings.getInstance().save(e);
    xml.addContent(e);
  }

  public String getLineFontSize() {
    return lineFontSize;
  }

  public void setLineFontSize(String lineFontSize) {
    this.lineFontSize = lineFontSize;
  }

  public String getLineFontType() {
    return lineFontType;
  }

  public void setLineFontType(String lineFontType) {
    this.lineFontType = lineFontType;
  }

  public int getLineendSize() {
    return lineendSize;
  }

  public void setLineendSize(int lineendSize) {
    this.lineendSize = lineendSize;
  }

  public int getLineWidth() {
    return lineWidth;
  }

  public void setLineWidth(int lineWidth) {
    this.lineWidth = lineWidth;
  }

  public String getLineendType() {
    return lineendType;
  }

  public void setLineendType(String lineendType) {
    this.lineendType = lineendType;
  }

  public String getGroupFontSize() {
    return groupFontSize;
  }

  public void setGroupFontSize(String groupFontSize) {
    String old = this.groupFontSize;
    this.groupFontSize = groupFontSize;
    this.firePropertyChange(PROPERTY_GROUP_FONTSIZE, old, this.groupFontSize);
  }

  public String getGroupFontType() {
    return groupFontType;
  }

  public void setGroupFontType(String groupFontType) {
    String old = this.groupFontType;
    this.groupFontType = groupFontType;
    this.firePropertyChange(PROPERTY_GROUP_FONTTYPE, old, this.groupFontType);
  }

  public int getGroupBorderWidth() {
    return groupBorderWidth;
  }

  public void setGroupBorderWidth(int groupBorderWidth) {
    this.groupBorderWidth = groupBorderWidth;
  }

  public String getGroupHAlign() {
    return groupHAlign;
  }

  public void setGroupHAlign(String groupHAlign) {
    this.groupHAlign = groupHAlign;
  }

  public String getGroupVAlign() {
    return groupVAlign;
  }

  public void setGroupVAlign(String groupVAlign) {
    this.groupVAlign = groupVAlign;
  }

  public String getPosFontSize() {
    return posFontSize;
  }

  public void setPosFontSize(String posFontSize) {
    this.posFontSize = posFontSize;
  }

  public String getPosFontType() {
    return posFontType;
  }

  public void setPosFontType(String posFontType) {
    this.posFontType = posFontType;
  }

  public int getPosBorderWidth() {
    return posBorderWidth;
  }

  public void setPosBorderWidth(int posBorderWidth) {
    this.posBorderWidth = posBorderWidth;
  }

  public String getPosHAlign() {
    return posHAlign;
  }

  public void setPosHAlign(String posHAlign) {
    this.posHAlign = posHAlign;
  }

  public String getPosVAlign() {
    return posVAlign;
  }

  public void setPosVAlign(String posVAlign) {
    this.posVAlign = posVAlign;
  }

  public String getGroupFontColor() {
    return groupFontColor;
  }

  public void setGroupFontColor(String groupFontColor) {
    this.groupFontColor = groupFontColor;
  }

  public String getGroupBorderColor() {
    return groupBorderColor;
  }

  public void setGroupBorderColor(String groupBorderColor) {
    this.groupBorderColor = groupBorderColor;
  }

  public String getGroupFillColor() {
    return groupFillColor;
  }

  public void setGroupFillColor(String groupFillColor) {
    this.groupFillColor = groupFillColor;
  }

  public String getGroupGradientColor() {
    return groupGradientColor;
  }

  public void setGroupGradientColor(String groupGradientColor) {
    this.groupGradientColor = groupGradientColor;
  }

  public String getPosFontColor() {
    return posFontColor;
  }

  public void setPosFontColor(String posFontColor) {
    this.posFontColor = posFontColor;
  }

  public String getPosBorderColor() {
    return posBorderColor;
  }

  public void setPosBorderColor(String posBorderColor) {
    this.posBorderColor = posBorderColor;
  }

  public String getPosFillColor() {
    return posFillColor;
  }

  public void setPosFillColor(String posFillColor) {
    this.posFillColor = posFillColor;
  }

  public String getPosGradientColor() {
    return posGradientColor;
  }

  public void setPosGradientColor(String posGradientColor) {
    this.posGradientColor = posGradientColor;
  }

  public String getLineFontColor() {
    return lineFontColor;
  }

  public void setLineFonColor(String lineFontColor) {
    this.lineFontColor = lineFontColor;
  }

  public String getLineColor() {
    return lineColor;
  }

  public void setLineColor(String lineColor) {
    this.lineColor = lineColor;
  }

  private void save() {
    ArrayList<DuellGroupCell> dgCells = DesignerItem.getTournamentViewer()
        .getShapesPalette().getGroupTemplates();
    for (DuellGroupCell dgc : dgCells) {

      String s = mxUtils.setStyle(dgc.getStyle(), mxConstants.STYLE_FILLCOLOR,
          groupFillColor);
      s = mxUtils.setStyle(s, mxConstants.STYLE_GRADIENTCOLOR,
          groupGradientColor);
      s = mxUtils.setStyle(s, mxConstants.STYLE_FONTCOLOR, groupFontColor);
      s = mxUtils.setStyle(s, mxConstants.STYLE_STROKECOLOR, groupBorderColor);
      s = mxUtils.setStyle(s, mxConstants.STYLE_FONTFAMILY, groupFontType);
      s = mxUtils.setStyle(s, mxConstants.STYLE_FONTSIZE, groupFontSize);
      s = mxUtils.setStyle(s, mxConstants.STYLE_ALIGN, groupHAlign);
      s = mxUtils.setStyle(s, mxConstants.STYLE_VERTICAL_ALIGN, groupVAlign);
      s = mxUtils.setStyle(s, mxConstants.STYLE_STROKEWIDTH, groupBorderWidth
          + "");
      dgc.setStyle(s);

      s = mxUtils.setStyle(dgc.getChildAt(0).getStyle(),
          mxConstants.STYLE_FILLCOLOR, posFillColor);
      s = mxUtils
          .setStyle(s, mxConstants.STYLE_GRADIENTCOLOR, posGradientColor);
      s = mxUtils.setStyle(s, mxConstants.STYLE_FONTCOLOR, posFontColor);
      s = mxUtils.setStyle(s, mxConstants.STYLE_STROKECOLOR, posBorderColor);
      s = mxUtils.setStyle(s, mxConstants.STYLE_FONTFAMILY, posFontType);
      s = mxUtils.setStyle(s, mxConstants.STYLE_FONTSIZE, posFontSize);
      s = mxUtils.setStyle(s, mxConstants.STYLE_ALIGN, posHAlign);
      s = mxUtils.setStyle(s, mxConstants.STYLE_VERTICAL_ALIGN, posVAlign);
      s = mxUtils.setStyle(s, mxConstants.STYLE_STROKEWIDTH, posBorderWidth
          + "");

      for (int i = 0; i < dgc.getChildCount(); i++) {
        dgc.setPositionStyle(i, s);
      }
    }

    DesignerItem.getTournamentViewer().getUndoManager().clear();
  }

}
