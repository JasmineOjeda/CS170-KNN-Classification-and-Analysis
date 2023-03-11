package classes;

import java.util.*;

public class Classifier {
    ArrayList<Integer> train_indexes;
    ArrayList<double[]> database;

    public Classifier() {
        train_indexes = new ArrayList<Integer>();
        database = new ArrayList<double[]>();
    }

    public Classifier(ArrayList<double[]> db) {
        train_indexes = new ArrayList<Integer>();
        database = db;
        int train_size = (int)(database.size() * 0.8);
        
        for (int i = 0; i < train_size; i++) {
            train_indexes.add(i);
        }
    }

    public Classifier(ArrayList<double[]> db, ArrayList<Integer> train_instances) {
        train_indexes = new ArrayList<Integer>();
        database = db;
        train_indexes = train_instances;
    }

    public void train(ArrayList<Integer> train_instances) {
        train_indexes = train_instances;
    }

    public double test(int test_instance, ArrayList<Integer> feature_subset) {
        double predicted_class = 0;
        double min_distance = Double.POSITIVE_INFINITY;
        double[] test_vector = database.get(test_instance);
        int nn_index = 0;

        for (int i = 0; i < train_indexes.size(); i++) {
            //System.out.println("Ask if " + test_instance + " is nearest neighbor with " + train_indexes.get(i));
            double[] curr_train_vector = database.get(train_indexes.get(i));
            double distance = euclideanDistance(test_vector, curr_train_vector, feature_subset);

            if (distance <= min_distance) {
                min_distance = distance;
                predicted_class = curr_train_vector[0];
                nn_index = train_indexes.get(i);
            }
        }
        //System.out.println("Its nearest neighbor is " + nn_index + " which is in class " + (int)(predicted_class));
        return predicted_class;
    }

    public double euclideanDistance(double[] point_a, double[] point_b, ArrayList<Integer> feature_subset) {
        double distance = 0;

        for (int i = 0; i < feature_subset.size(); i++) {
            distance += Math.pow(point_b[feature_subset.get(i)] - point_a[feature_subset.get(i)], 2);
        }

        return Math.sqrt(distance);
    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * * * HELPER FUNCTIONS * * * * *
    public void displayIntList(ArrayList<Integer> list) {
        System.out.print("{");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i != (list.size() - 1)) {
                System.out.print(", ");
            }
        }
        System.out.print("}");
    }
}
