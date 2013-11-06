package com.easytournament.designer.export;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.Element;

import com.easytournament.basic.MetaInfos;
import com.easytournament.basic.Organizer;
import com.easytournament.basic.export.Exportable;
import com.easytournament.basic.navigationitem.NavigationItem;
import com.easytournament.basic.resources.ResourceManager;
import com.easytournament.basic.resources.Text;
import com.easytournament.basic.xml.XMLHandler;
import com.easytournament.designer.TournamentViewer;
import com.easytournament.designer.xml.DesignerXMLHandler;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;

public class PlanExportable implements Exportable {

  private NavigationItem module;

  public PlanExportable(NavigationItem module) {
    this.module = module;
  }

  @Override
  public void doExport(boolean activeModule) {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".jpg") || f.isDirectory();
      }

      public String getDescription() {
        return "JPG (*.jpg)";
      }
    });
    FileFilter xmlfilter = new FileFilter() {

      @Override
      public String getDescription() {
        return "XML (*.xml)";
      }

      @Override
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
      }
    };
    chooser.addChoosableFileFilter(xmlfilter);
    FileFilter svgfilter = new FileFilter() {

      @Override
      public String getDescription() {
        return "SVG (*.svg)";
      }

      @Override
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".svg") || f.isDirectory();
      }
    };
    chooser.addChoosableFileFilter(svgfilter);
    int answer = chooser.showSaveDialog(Organizer.getInstance().getMainFrame());
    if (answer == JFileChooser.APPROVE_OPTION) {
      File filename = chooser.getSelectedFile();

      if (filename != null) {

        if (chooser.getFileFilter().equals(xmlfilter)) {
          if (!filename.getPath().toLowerCase().endsWith(".xml")) {
            filename = new File(filename.getPath() + ".xml");
          }
          Element filetype = new Element("filetype");
          filetype.setAttribute("application", MetaInfos.FILE_APPLICATION);
          filetype.setAttribute("type", MetaInfos.FILE_PLANFILETYPE);
          filetype.setAttribute("version", MetaInfos.getXMLFileVersion());

          DesignerXMLHandler.save(filetype);
          XMLHandler.saveXMLDoc(new Document(filetype), filename);
        }
        else if (chooser.getFileFilter().equals(svgfilter)) {
          if (!filename.getPath().toLowerCase().endsWith(".svg")) {
            filename = new File(filename.getPath() + ".svg");
          }
          mxSvgCanvas canvas = (mxSvgCanvas)mxCellRenderer.drawCells(
              TournamentViewer.getGraphComponent().getGraph(), null, 1, null,
              new CanvasFactory() {
                public mxICanvas createCanvas(int width, int height) {
                  mxSvgCanvas canvas = new mxSvgCanvas(mxUtils
                      .createSvgDocument(width, height));
                  canvas.setEmbedded(true);

                  return canvas;
                }

              });

          FileWriter fw;
          try {
            fw = new FileWriter(filename);
            fw.write(mxUtils.getXml(canvas.getDocument()));
            fw.flush();
            fw.close();
          }
          catch (IOException e) {
            JOptionPane.showInternalMessageDialog(Organizer.getInstance()
                .getMainFrame(), ResourceManager
                .getText(Text.COULD_NOT_SAVE_DIAG), ResourceManager
                .getText(Text.ERROR), JOptionPane.ERROR_MESSAGE);
          }
        }
        else {
          if (!filename.getPath().toLowerCase().endsWith(".jpg")) {
            filename = new File(filename.getPath() + ".jpg");
          }
          mxGraphComponent graphComponent = TournamentViewer
              .getGraphComponent();
          BufferedImage image = mxCellRenderer.createBufferedImage(
              graphComponent.getGraph(), null, 1,
              graphComponent.getBackground(), graphComponent.isAntiAlias(),
              null, graphComponent.getCanvas());

          if (image != null) {
            try {
              ImageIO.write(image, "jpg", filename);
            }
            catch (IOException e) {
              JOptionPane.showInternalMessageDialog(Organizer.getInstance()
                  .getMainFrame(), ResourceManager
                  .getText(Text.COULD_NOT_SAVE_DIAG), ResourceManager
                  .getText(Text.ERROR), JOptionPane.ERROR_MESSAGE);
            }
          }
          else {
            JOptionPane.showMessageDialog(graphComponent,
                mxResources.get("noImageData"));
          }
        }
      }
    }
  }

  @Override
  public NavigationItem getModule() {
    return this.module;
  }
}
