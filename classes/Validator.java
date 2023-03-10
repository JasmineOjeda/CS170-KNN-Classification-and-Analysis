package classes;

import java.util.*;

public class Validator {
    Classifier classifier;
    ArrayList<Integer> feature_subset;
    ArrayList<double[]> database;

    public Validator() {
        classifier = new Classifier();
        feature_subset = new ArrayList<Integer>();
    }

    public Validator(Classifier c, ArrayList<Integer> set, ArrayList<double[]> db) {
        classifier = c;
        feature_subset = set;
        database = db;
    }

    public double validation() {
        double correct = 0;
        double total_tests = 0;

        for (int i = 0; i < classifier.train_indexes.size(); i++) {
            //System.out.println("Looping over i, at the " + i + "location");
            ArrayList<Integer> temp_train = new ArrayList<Integer>();

            for (int j = 0; j < classifier.train_indexes.size(); j++) {
                if (j != i) {
                    temp_train.add(classifier.train_indexes.get(j));
                }
            }

            Classifier temp_classifier = new Classifier(database, temp_train);
            double test_class = temp_classifier.test(classifier.train_indexes.get(i), feature_subset);
            //System.out.println("The " + i + "the object is in class " + (int)(test_class));
            if (test_class == database.get(i)[0]) {
                correct++;
            }
            total_tests++;
        }

        return (correct / total_tests);
    }
}
