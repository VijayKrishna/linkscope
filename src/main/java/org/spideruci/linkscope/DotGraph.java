package org.spideruci.linkscope;

import com.google.common.base.Preconditions;

import static java.util.Objects.requireNonNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by vpalepu on 6/3/16.
 */
public class DotGraph<T> {

  public static final String TMP = "/tmp";
  public static final String TEMP_FILE_NAME = "temp-graph.dot";

  public static <T> DotGraph<T> create() {
    DotGraph dotGraph = new DotGraph();
    return dotGraph;
  }

  private final ArrayList<Edge<T>> edges;
  private final LinkedHashSet<T> nodes;
  private int height;
  private int width;

  private DotGraph() {
    this.nodes = new LinkedHashSet<>();
    edges = new ArrayList<>();
  }

  public DotGraph addEdge(Edge<T> edge) {
    requireNonNull(edge);

    nodes.add(edge.from);
    nodes.add(edge.to);

    edges.add(edge);
    return this;
  }

  public DotGraph addEdge(T from, T to) {
    requireNonNull(from);
    requireNonNull(to);

    Edge edge = Edge.create(from, to);
    return addEdge(edge);
  }

  public DotGraph addBounds(int w, int h) {
    this.width = w;
    this.height = h;
    return this;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  @Override
  public String toString() {
    StringBuffer edgeBuffer = new StringBuffer();

    for(Edge edge : edges) {
      edgeBuffer.append(edge.toString()).append(";\n");
    }

    String dotString = String.format("digraph {\n%s}", edgeBuffer.toString());

    return dotString;
  }

  public Iterable<Edge<T>> edges() {
    return this.edges;
  }

  public String toDotString() {
    return toString();
  }

  /**
   * Creates a temporary file in the /tmp folder, where {@code this} graph is written.
   * @return the path of the temp dot file to which {@code this} graph
   * has been pushed to.
   */
  public Path pushToTempFile() {
    Path temp = Paths.get(TMP, TEMP_FILE_NAME);

    try {

      Files.deleteIfExists(temp);
      Files.createFile(temp);
      Preconditions.checkState(Files.exists(temp));

      FileWriter fileWriter = new FileWriter(temp.toFile());
      BufferedWriter writer = new BufferedWriter(fileWriter);

      writer.write("digraph {\n");

      for(Edge edge : edges) {
        writer.write(edge.toString());
        writer.write(";\n");
      }

      writer.write("\n}");

      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return temp;
  }



}
