package easytournament.designer.model.settings;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

import com.jgoodies.binding.beans.Model;
import com.mxgraph.util.mxConstants;

import easytournament.basic.Organizer;
import easytournament.basic.resources.ResourceManager;
import easytournament.basic.resources.Text;
import easytournament.designer.settings.DesignerSettings;
import easytournament.designer.util.comperator.Utils;


public class DesignerSetPModel extends Model {

  public static final String PROPERTY_LINE_FONTSIZE = "lineFontSize";
  public static final String PROPERTY_LINE_FONTTYPE = "lineFontType";
  public static final String PROPERTY_LINEEND_SIZE = "lineendSize";
  public static final String PROPERTY_LINEWIDTH = "lineWidth";
  public static final String PROPERTY_LINEEND_TYPE = "lineendType";
  
  public static final String PROPERTY_GROUP_FONTSIZE = "groupFontSize";
  public static final String PROPERTY_GROUP_FONTTYPE = "groupFontType";
  public static final String PROPERTY_GROUP_BORDERWIDTH = "groupBorderWidth";
  public static final String PROPERTY_GROUP_HALIGN = "groupHAlign";
  public static final String PROPERTY_GROUP_VALIGN = "groupVAlign";
  
  public static final String PROPERTY_POS_FONTSIZE = "posFontSize";
  public static final String PROPERTY_POS_FONTTYPE = "posFontType";
  public static final String PROPERTY_POS_BORDERWIDTH = "posBorderWidth";
  public static final String PROPERTY_POS_HALIGN = "posHAlign";
  public static final String PROPERTY_POS_VALIGN = "posVAlign";

  public static final int LINE_FONT_COLOR_ACTION = 0;
  public static final int LINE_COLOR_ACTION = 1;
  
  public static final int GROUP_FONT_COLOR_ACTION = 2;
  public static final int GROUP_BORDER_COLOR_ACTION = 3;
  public static final int GROUP_FILL_COLOR_ACTION = 4;
  public static final int GROUP_GRADIENT_COLOR_ACTION = 5;
  
  public static final int POS_FONT_COLOR_ACTION = 6;
  public static final int POS_BORDER_COLOR_ACTION = 7;
  public static final int POS_FILL_COLOR_ACTION = 8;
  public static final int POS_GRADIENT_COLOR_ACTION = 9;
  

  private List<String> fonts = new ArrayList<String>();
  private List<String> lineEndTypes = new ArrayList<String>();
  private List<String> hAlignments = new ArrayList<String>();
  private List<String> vAlignments = new ArrayList<String>();

  private String[] fontSizes = new String[] {"6", "8", "9", "10",
      "12", "14", "18", "24", "30", "36", "48", "60"};

  protected String lineFontSize;
  protected String lineFontType;
  protected int lineendSize;
  protected int lineWidth;
  protected String lineendType;
  
  protected String groupFontSize;
  protected String groupFontType;
  protected int groupBorderWidth;
  protected String groupHAlign;
  protected String groupVAlign;
  
  protected String posFontSize;
  protected String posFontType;
  protected int posBorderWidth;
  protected String posHAlign;
  protected String posVAlign;
  
  protected Color groupFontColor;
  protected Color groupBorderColor;
  protected Color groupFillColor;
  protected Color groupGradientColor;
  protected Color posFontColor;
  protected Color posBorderColor;
  protected Color posFillColor;
  protected Color posGradientColor;
  protected Color lineFontColor;
  protected Color lineColor;

  protected JFrame mainframe = Organizer.getInstance().getMainFrame();
  
  protected DesignerSettings settings = DesignerSettings.getInstance();

  public DesignerSetPModel() {
    init();

  }

  private void init() {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    fonts.addAll(Arrays.asList(env.getAvailableFontFamilyNames()));

    lineEndTypes.add(ResourceManager.getText(Text.END_OPEN_MENU));
    lineEndTypes.add(ResourceManager.getText(Text.END_CLASSIC_MENU));
    lineEndTypes.add(ResourceManager.getText(Text.END_BLOCK_MENU));
    if(settings.getLineendType().equals(mxConstants.ARROW_BLOCK))
      lineendType = lineEndTypes.get(2);
    else if(settings.getLineendType().equals(mxConstants.ARROW_OPEN))
      lineendType = lineEndTypes.get(0);
    else
      lineendType = lineEndTypes.get(1);
    
   
    hAlignments.add(ResourceManager.getText(Text.TEXT_ALIGN_LEFT_MENU));
    hAlignments.add(ResourceManager.getText(Text.TEXT_ALIGN_CENTER_MENU));
    hAlignments.add(ResourceManager.getText(Text.TEXT_ALIGN_RIGHT_MENU));
    if(settings.getGroupHAlign().equals(mxConstants.ALIGN_LEFT))
      groupHAlign = hAlignments.get(0);
    else if(settings.getGroupHAlign().equals(mxConstants.ALIGN_RIGHT))
      groupHAlign = hAlignments.get(2);
    else
      groupHAlign = hAlignments.get(1);
    
    if(settings.getPosHAlign().equals(mxConstants.ALIGN_LEFT))
      posHAlign = hAlignments.get(0);
    else if(settings.getPosHAlign().equals(mxConstants.ALIGN_RIGHT))
      posHAlign = hAlignments.get(2);
    else
      posHAlign = hAlignments.get(1);
    
    vAlignments.add(ResourceManager.getText(Text.TEXT_ALIGN_TOP_MENU));
    vAlignments.add(ResourceManager.getText(Text.TEXT_ALIGN_MIDDLE_MENU));
    vAlignments.add(ResourceManager.getText(Text.TEXT_ALIGN_BOTTOM_MENU));
    
    if(settings.getGroupVAlign().equals(mxConstants.ALIGN_TOP))
      groupVAlign = vAlignments.get(0);
    else if(settings.getGroupVAlign().equals(mxConstants.ALIGN_BOTTOM))
      groupVAlign = vAlignments.get(2);
    else
      groupVAlign = vAlignments.get(1);
    
    if(settings.getPosVAlign().equals(mxConstants.ALIGN_TOP))
      posVAlign = vAlignments.get(0);
    else if(settings.getPosVAlign().equals(mxConstants.ALIGN_BOTTOM))
      posVAlign = vAlignments.get(2);
    else
      posVAlign = vAlignments.get(1);
    
    lineWidth = settings.getLineWidth();
    lineendSize = settings.getLineendSize();
    groupBorderWidth = settings.getGroupBorderWidth();
    posBorderWidth = settings.getPosBorderWidth();
    lineFontSize = settings.getLineFontSize();
    groupFontSize = settings.getGroupFontSize();
    posFontSize = settings.getPosFontSize();
    lineFontType = settings.getLineFontType();
    groupFontType = settings.getGroupFontType();
    posFontType = settings.getPosFontType();
  }

  public void save() {
    settings.setLineWidth(lineWidth);
    settings.setLineendSize(lineendSize);
    settings.setGroupBorderWidth(groupBorderWidth);
    settings.setPosBorderWidth(posBorderWidth);
    settings.setLineFontSize(lineFontSize);
    settings.setGroupFontSize(groupFontSize);
    settings.setPosFontSize(posFontSize);
    settings.setLineFontType(lineFontType);
    settings.setGroupFontType(groupFontType);
    settings.setPosFontType(posFontType);
    
    if(lineendType.equals(lineEndTypes.get(2)))
      settings.setLineendType(mxConstants.ARROW_BLOCK);
    else if(lineendType.equals(lineEndTypes.get(0)))
      settings.setLineendType(mxConstants.ARROW_OPEN);
    else
      settings.setLineendType(mxConstants.ARROW_CLASSIC);
    
    if(groupHAlign.equals(hAlignments.get(0)))
      settings.setGroupHAlign(mxConstants.ALIGN_LEFT);
    else if(groupHAlign.equals(hAlignments.get(2)))
      settings.setGroupHAlign(mxConstants.ALIGN_RIGHT);
    else
      settings.setGroupHAlign(mxConstants.ALIGN_CENTER);
    
    if(posHAlign.equals(hAlignments.get(0)))
      settings.setPosHAlign(mxConstants.ALIGN_LEFT);
    else if(posHAlign.equals(hAlignments.get(2)))
      settings.setPosHAlign(mxConstants.ALIGN_RIGHT);
    else
      settings.setPosHAlign(mxConstants.ALIGN_CENTER);
    
    if(groupVAlign.equals(vAlignments.get(0)))
      settings.setGroupVAlign(mxConstants.ALIGN_TOP);
    else if(groupVAlign.equals(vAlignments.get(2)))
      settings.setGroupVAlign(mxConstants.ALIGN_BOTTOM);
    else
      settings.setGroupVAlign(mxConstants.ALIGN_MIDDLE);
    
    if(posVAlign.equals(vAlignments.get(0)))
      settings.setPosVAlign(mxConstants.ALIGN_TOP);
    else if(posVAlign.equals(vAlignments.get(2)))
      settings.setPosVAlign(mxConstants.ALIGN_BOTTOM);
    else
      settings.setPosVAlign(mxConstants.ALIGN_MIDDLE);
    
    settings.setGroupFontColor(Utils.toHex(groupFontColor));
    settings.setGroupBorderColor(Utils.toHex(groupBorderColor));
    settings.setGroupFillColor(Utils.toHex(groupFillColor));
    settings.setGroupGradientColor(Utils.toHex(groupGradientColor));
    settings.setPosFontColor(Utils.toHex(posFontColor));
    settings.setPosBorderColor(Utils.toHex(posBorderColor));
    settings.setPosFillColor(Utils.toHex(posFillColor));
    settings.setPosGradientColor(Utils.toHex(posGradientColor));
    settings.setLineColor(Utils.toHex(lineColor));
    settings.setLineFonColor(Utils.toHex(lineFontColor));
    
  }

  public Action getColorAction(int action) {
    
    AbstractAction a = null;

    switch (action) {
      case LINE_FONT_COLOR_ACTION: {    
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.FONTCOLOR_MENU),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              lineFontColor = newColor;
            }

          }
        };
        lineFontColor = Color.decode(settings.getLineFontColor());
        a.putValue("FG", lineFontColor);
        break;
      }
      case LINE_COLOR_ACTION: {  
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.LINE_COLOR_MENU),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              lineColor = newColor;
            }

          }
        };
        lineColor = Color.decode(settings.getLineColor());
        a.putValue("FG", lineColor);
        break;
      }
      case GROUP_FONT_COLOR_ACTION: {  
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.FONTCOLOR_MENU),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              groupFontColor = newColor;
            }

          }
        };
        groupFontColor = Color.decode(settings.getGroupFontColor());
        a.putValue("FG", groupFontColor);
        break;
      }
      case GROUP_BORDER_COLOR_ACTION: {   
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.BORDER_COLOR),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              groupBorderColor = newColor;
            }

          }
        };
        groupBorderColor = Color.decode(settings.getGroupBorderColor());
        a.putValue("FG", groupBorderColor);
        break;
      }
      case GROUP_FILL_COLOR_ACTION: {     
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.BACKGROUND_COLOR),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              groupFillColor = newColor;
            }

          }
        };
        groupFillColor = Color.decode(settings.getGroupFillColor());
        a.putValue("FG", groupFillColor);
        break;
      }
      case GROUP_GRADIENT_COLOR_ACTION: {      
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.COLORGRADIENT_MENU),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              groupGradientColor = newColor;
            }

          }
        };
        groupGradientColor = Color.decode(settings.getGroupGradientColor());
        a.putValue("FG", groupGradientColor);
        break;
      }
      case POS_FONT_COLOR_ACTION: {  
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.FONTCOLOR_MENU),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              posFontColor = newColor;
            }

          }
        };
        posFontColor = Color.decode(settings.getPosFontColor());
        a.putValue("FG", posFontColor);
        break;
      }
      case POS_BORDER_COLOR_ACTION: {  
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.BORDER_COLOR),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              posBorderColor = newColor;
            }

          }
        };
        posBorderColor = Color.decode(settings.getPosBorderColor());
        a.putValue("FG", posBorderColor);
        break;
      }
      case POS_FILL_COLOR_ACTION: { 
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.BACKGROUND_COLOR),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              posFillColor = newColor;
            }

          }
        };
        posFillColor = Color.decode(settings.getPosFillColor());
        a.putValue("FG", posFillColor);
        break;
      }
      case POS_GRADIENT_COLOR_ACTION: {   
        a = new AbstractAction() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(mainframe, ResourceManager.getText(Text.COLORGRADIENT_MENU),
                (Color)this.getValue("FG"));

            if (newColor != null) {
              this.putValue("FG", newColor);
              posGradientColor = newColor;
            }

          }
        };
        posGradientColor = Color.decode(settings.getPosGradientColor());
        a.putValue("FG", posGradientColor);
        break;
      }
    }
    return a;
  }

  public String getLineFontSize() {
    return lineFontSize;
  }

  public void setLineFontSize(String fontSize) {
    String old = this.lineFontSize;
    this.lineFontSize = fontSize;
    this.firePropertyChange(PROPERTY_LINE_FONTSIZE, old, this.lineFontSize);
  }

  public String[] getFontSizes() {
    return fontSizes;
  }

  public String getLineFontType() {
    return lineFontType;
  }

  public void setLineFontType(String fontType) {
    String old = this.lineFontType;
    this.lineFontType = fontType;
    this.firePropertyChange(PROPERTY_LINE_FONTTYPE, old, this.lineFontType);
  }

  public List<String> getFonts() {
    return fonts;
  }

  public int getLineendSize() {
    return lineendSize;
  }

  public void setLineendSize(int lineendSize) {
    int old = this.lineendSize;
    this.lineendSize = lineendSize;
    this.firePropertyChange(PROPERTY_LINEEND_SIZE, old, this.lineendSize);
  }

  public int getLineWidth() {
    return lineWidth;
  }

  public void setLineWidth(int lineWidth) {
    int old = this.lineWidth;
    this.lineWidth = lineWidth;
    this.firePropertyChange(PROPERTY_LINEWIDTH, old, this.lineWidth);
  }

  public List<String> getHAlignements() {
    return hAlignments;
  }

  public List<String> getVAlignements() {
    return vAlignments;
  }

  public List<String> getLineEndTypes() {
    return lineEndTypes;
  }

  public String getLineendType() {
    return lineendType;
  }

  public void setLineendType(String lineendType) {
    String old = this.lineendType;
    this.lineendType = lineendType;
    this.firePropertyChange(PROPERTY_LINEEND_TYPE, old, this.lineendType);
  }

  public String getGroupHAlign() {
    return groupHAlign;
  }

  public void setGroupHAlign(String hAlign) {
    String old = this.groupHAlign;
    this.groupHAlign = hAlign;
    this.firePropertyChange(PROPERTY_GROUP_HALIGN, old, this.groupHAlign);
  }

  public String getGroupVAlign() {
    return groupVAlign;
  }

  public void setGroupVAlign(String vAlign) {
    String old = this.groupVAlign;
    this.groupVAlign = vAlign;
    this.firePropertyChange(PROPERTY_GROUP_VALIGN, old, this.groupVAlign);
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
    int old = this.groupBorderWidth;
    this.groupBorderWidth = groupBorderWidth;
    this.firePropertyChange(PROPERTY_GROUP_BORDERWIDTH, old, this.groupBorderWidth);
  }

  public String getPosFontSize() {
    return posFontSize;
  }

  public void setPosFontSize(String posFontSize) {
    String old = this.posFontSize;
    this.posFontSize = posFontSize;
    this.firePropertyChange(PROPERTY_POS_FONTSIZE, old, this.posFontSize);
  }

  public String getPosFontType() {
    return posFontType;
  }

  public void setPosFontType(String posFontType) {
    String old = this.posFontType;
    this.posFontType = posFontType;
    this.firePropertyChange(PROPERTY_POS_FONTTYPE, old, this.posFontType);
  }

  public int getPosBorderWidth() {
    return posBorderWidth;
  }

  public void setPosBorderWidth(int posBorderWidth) {
    int old = this.posBorderWidth;
    this.posBorderWidth = posBorderWidth;
    this.firePropertyChange(PROPERTY_POS_BORDERWIDTH, old, this.posBorderWidth);
  }

  public String getPosHAlign() {
    return posHAlign;
  }

  public void setPosHAlign(String posHAlign) {
    String old = this.posHAlign;
    this.posHAlign = posHAlign;
    this.firePropertyChange(PROPERTY_POS_HALIGN, old, this.posHAlign);
  }

  public String getPosVAlign() {
    return posVAlign;
  }

  public void setPosVAlign(String posVAlign) {
    String old = this.posVAlign;
    this.posVAlign = posVAlign;
    this.firePropertyChange(PROPERTY_POS_VALIGN, old, this.posVAlign);
  }

}
