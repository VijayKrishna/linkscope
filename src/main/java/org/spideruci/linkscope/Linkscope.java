package org.spideruci.linkscope;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by vpalepu on 6/3/16.
 */
public class Linkscope {

  public static final String DOT = "dot";

  public static void main(String[] args) {
    System.out.println("Starting Linkscope...");
    runDot(Arrays.asList("-V"));

    DotGraph<String> graph = DotGraph.create();

    graph.addEdge("apple", "banana")
        .addEdge("banana", "grapes")
        .addEdge("apple", "potato")
        .addEdge("grapes", "potato")
        .addEdge("grapes", "berry")
        .addEdge("berry", "potato")
        .addEdge("b", "c")
        .addEdge("c", "berry")
        .addEdge("b", "potato");


    String dotString = graph.toDotString();
    System.out.println(dotString);

    Path jotFile = graph.pushToTempFile();

    final DotGraph dotGraph = DotGraph.create();

    runDot(Arrays.asList("-Tdot", jotFile.toString()), new DotParser());

  }

  public static void runDot(List<String> args) {
    runDot(args, null);
  }

  public static void runDot(List<String> args, StreamGobbler outGobbler) {
    ArrayList<String> command = new ArrayList<>();
    command.addAll(args);
    command.add(0, DOT);

    ProcessBuilder processBuilder = new ProcessBuilder().command(command);

    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

    if(outGobbler == null) {
      processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
    }

    try {
      Process process = processBuilder.start();

      if(outGobbler != null) {
        InputStream processStream = process.getInputStream();
        outGobbler.withStream(processStream).start();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}