package classes;

import java.util.*;

public class SetNode {
    public ArrayList<Integer> feature_set;
    public double accuracy;
    
    public SetNode() {
        feature_set = new ArrayList<Integer>();
        accuracy = 0;
    }

    public SetNode(ArrayList<Integer> set, double acc) {
        feature_set = new ArrayList<Integer>();

        for (int i = 0; i < set.size(); i ++) {
            feature_set.add(set.get(i));
        }

        accuracy = acc;
    }
}
