package classes;

import java.util.*;

public class Validator {
    Classifier classifier;
    ArrayList<Integer> feature_subset;

    public Validator() {
        classifier = new Classifier();
        feature_subset = new ArrayList<Integer>();
    }

    public Validator(Classifier c, ArrayList<Integer> set) {
        classifier = c;
        feature_subset = set;
    }
}
