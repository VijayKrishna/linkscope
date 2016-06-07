package org.spideruci.linkscope;

/**
 * Created by vpalepu on 6/7/16.
 */
public enum DotKeywords {

  ARROW ("->"),

  DIGRAPH ("digraph"),
  GRAPH ("graph"),
  NODE ("node"),

  BB ("bb"),
  LABEL ("label"),

  WIDTH ("width"),
  HEIGHT ("height"),
  POS ("pos"),

  UNKNOWN("");

  public final String value;

  DotKeywords(String value) {
    this.value = value;
  }

}
