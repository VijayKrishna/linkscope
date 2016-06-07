package org.spideruci.linkscope;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by vpalepu on 6/7/16.
 */
public class DotParser extends StreamGobbler {

  DotGraph<Node> graph = DotGraph.create();

  private HashMap<String, Node> nodes = new HashMap<>();

  String currentNodeIdent = null;
  StringBuffer currentToken = new StringBuffer();
  String previousToken = null;
  boolean inAttributeValue = false;
  boolean isLineWrapped = false;
  String wrappedLine = null;

  @Override
  public void processLine(String line) {
    if(line == null) {
      return;
    }

    line = line.trim();

    if(line.isEmpty()
        || line.startsWith(DotKeywords.DIGRAPH.value)
        || line.startsWith(DotKeywords.NODE.value)
        || line.startsWith(String.valueOf(DotOperators.START))
        || line.startsWith(String.valueOf(DotOperators.END)) ) {
      return;
    }

    if(line.startsWith(DotKeywords.GRAPH.value)) {
      if(line.contains(DotKeywords.BB.value)) {
        parseBoundingBox(line);
      }
      return;
    }

    if(line.contains(DotKeywords.ARROW.value)) {
      if(line.endsWith(String.valueOf(DotOperators.SLASH))) {
        wrappedLine = line.replace(DotOperators.SLASH + "", "").trim();
      } else {
        parseEdge(line);
      }

      return;
    }

    if(wrappedLine != null) {
      line = wrappedLine + line;
      wrappedLine = null;
      parseEdge(line);
      return;
    }



    parseNode(line);
  }

  private void parseBoundingBox(String line) {

    int openQuoteIndex = line.indexOf(DotOperators.QUOTE);
    int closeQuoteIndex = line.lastIndexOf(DotOperators.QUOTE);

    System.out.println(openQuoteIndex);
    System.out.println(closeQuoteIndex);

    line = line.substring(openQuoteIndex, closeQuoteIndex);
    String[] bbValues = line.split(",");

    graph.addBounds((int) Math.ceil(Double.parseDouble(bbValues[2])),
        (int) Math.ceil(Double.parseDouble(bbValues[3])));

  }

  private void parseEdge(final String line) {

    int arrowIndex = line.indexOf(DotKeywords.ARROW.value);

    String from = line.substring(0, arrowIndex).trim();
    String to = line.substring(arrowIndex + 2, line.indexOf('[')).trim();

    this.graph.addEdge(nodes.get(from), nodes.get(to));
  }

  private void parseNode(final String line) {
    char[] lineChars = line.toCharArray();

    for(char ch : lineChars) {
      if(Character.isWhitespace(ch)) {
        continue;
      }

      switch (ch) {
        case DotOperators.OPEN:
          previousToken = currentToken.toString();
          currentToken = new StringBuffer();
          startNewNode(previousToken);
          break;
        case DotOperators.CLOSE:
          addNodeAttribute(previousToken, currentToken.toString());
          previousToken = currentToken.toString();
          currentToken = new StringBuffer();
          break;
        case DotOperators.SEMI_COLON:
          // do nothing?
          break;
        case DotOperators.EQ:
          previousToken = currentToken.toString();
          currentToken = new StringBuffer();
          break;
        case DotOperators.COMA:
          if(!inAttributeValue) {
            addNodeAttribute(previousToken, currentToken.toString());
            previousToken = currentToken.toString();
            currentToken = new StringBuffer();
          } else {
            currentToken.append(ch);
          }
          break;
        case DotOperators.QUOTE:
          currentToken.append(ch);
          inAttributeValue = !inAttributeValue;
          break;
        default:
          currentToken.append(ch);
      }

    }
  }

  private void addNodeAttribute(String attribute, String value) {
    Node node = nodes.get(this.currentNodeIdent);
    node.putAttribute(attribute, value);
  }

  private void startNewNode(String token) {
    this.currentNodeIdent = token;
    Node node = Node.create(this.currentNodeIdent);

    this.nodes.put(this.currentNodeIdent, node);
  }

  @Override
  public void finish() {
    displayGraph();
    System.out.println("done.");
  }

  private void displayGraph() {
    Display frame = new Display();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(graph.getWidth() * 2,
        graph.getHeight() * 2);

    HashMap<String, Object> displayedNodes = new HashMap<>();

    for(Node node : this.nodes.values()) {
      Object n = frame.add(node);
      displayedNodes.put(node.ident, n);
    }

    for(Edge<Node> edge : this.graph.edges()) {
      String from = edge.from.ident;
      String to = edge.to.ident;

      Object n1 = displayedNodes.get(from);
      Object n2 = displayedNodes.get(to);

      frame.add(n1, n2);
    }

  }

}
