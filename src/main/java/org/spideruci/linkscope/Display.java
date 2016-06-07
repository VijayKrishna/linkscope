package org.spideruci.linkscope;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.util.Hashtable;

/**
 * Created by vpalepu on 6/7/16.
 */
public class Display extends JFrame {

  public static final int MAGNIFIER = 50;

  /**
   *
   */
  private static final long serialVersionUID = -2707712944901661771L;

  private final mxGraph graph = new mxGraph();
  private final Object parent = graph.getDefaultParent();

  public Display() {
    super("Linkscope");

    mxStylesheet styleSheet = graph.getStylesheet();
    Hashtable<String, Object> style = new Hashtable<String, Object>();
    style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
    style.put(mxConstants.STYLE_ROUNDED, true);
    style.put(mxConstants.STYLE_ARCSIZE, 100);
    style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
    styleSheet.putCellStyle("ROUNDED", style);

    mxGraphComponent graphComponent = new mxGraphComponent(graph);
    getContentPane().add(graphComponent);
  }

  public Object add(Node node) {
    graph.getModel().beginUpdate();
    Object n1 = null;
    try {
      double width = Double.valueOf(node.getAttribute(DotKeywords.WIDTH.value));

      double height = Double.valueOf(node.getAttribute(DotKeywords.HEIGHT.value));
      String[] position =
          node.getAttribute(DotKeywords.POS.value)
              .replaceAll("\"", "")
              .split(",");

      double x = Double.parseDouble(position[0]);
      double y = Double.parseDouble(position[1]);
      n1 = graph.insertVertex(parent, null,
          node.ident, x, y,
          width * MAGNIFIER,
          height * MAGNIFIER,
          "ROUNDED;strokeColor=black;fillColor=white");

    } finally {
      graph.getModel().endUpdate();
    }

    return n1;
  }

  public void add(Object node1, Object node2) {

    graph.getModel().beginUpdate();

    try {
      graph.insertEdge(parent, null, null, node1, node2);
    } finally {
      graph.getModel().endUpdate();
    }

  }

}