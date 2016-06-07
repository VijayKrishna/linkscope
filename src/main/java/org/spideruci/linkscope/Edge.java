package org.spideruci.linkscope;

/**
 * Created by vpalepu on 6/3/16.
 */
public class Edge<T> {

  public final T from;
  public final T to;

  public static <T> Edge<T> create(final T from, final T to) {

    return new Edge(from, to);
  }

  private Edge(final T from, final T to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public String toString() {
    return String.format("%s -> %s", from, to);
  }


}
