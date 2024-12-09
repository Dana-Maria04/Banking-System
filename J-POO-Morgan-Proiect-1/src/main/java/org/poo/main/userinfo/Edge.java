package org.poo.main.userinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Edge {
    public int to;
    public double rate;

    public Edge(int to, double rate) {
        this.to = to;
        this.rate = rate;
    }
}
