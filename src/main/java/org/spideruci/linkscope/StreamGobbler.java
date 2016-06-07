package org.spideruci.linkscope;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

public abstract class StreamGobbler extends Thread {
  private InputStream is;

  public StreamGobbler withStream(InputStream stream) {
    if(this.is == null) {
      this.is = stream;
    } else {
      throw new RuntimeException("Input stream is already setup.");
    }

    return this;
  }


  public void run() {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      br.lines().forEach(this::processLine);

      this.finish();

    } catch (UncheckedIOException ioe) {
      ioe.printStackTrace();
    }
  }

  public abstract void processLine(String line);

  public abstract void finish();
}
