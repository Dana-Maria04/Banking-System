package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an edge in a graph, used to store the destination node and the
 * rate associated with it.
 */
@Getter
@Setter
public class Edge {
    private int to;
    private double rate;

    /**
     * Constructs a new Edge with the specified destination node and rate.
     *
     * @param to   The destination node index.
     * @param rate The rate associated with the edge.
     */
    public Edge(final int to, final double rate) {
        this.to = to;
        this.rate = rate;
    }
}
