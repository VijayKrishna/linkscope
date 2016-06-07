package org.spideruci.linkscope;

import java.util.HashMap;

/**
 * Created by vpalepu on 6/7/16.
 */
public class Node {
  public final String ident;
  private final HashMap<String, String> attributes;

  public static Node create(String ident) {
    Node node = new Node(ident);
    return node;
  }

  private Node(String ident) {
    this.ident = ident;
    this.attributes = new HashMap<>();
  }

  public void putAttribute(String name, String value) {
    attributes.put(name, value);
  }

  public String getAttribute(String name) {
    final String value = attributes.get(name);
    return value;
  }


}
