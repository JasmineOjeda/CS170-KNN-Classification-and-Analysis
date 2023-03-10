package classes;

import java.util.*;

public class Node {
    public double score;
    public int feature;
    
    public Node() {
        score = 0;
        feature = -1;
    }

    public Node(double sc, int f) {
        score = sc;
        feature = f;
    }
}