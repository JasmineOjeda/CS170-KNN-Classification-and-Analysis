package classes;

import java.util.*;

public class Classifier {
    ArrayList<Integer> train;
    ArrayList<Integer> test;
    ArrayList<double[]> database;

    public Classifier() {
        train = new ArrayList<Integer>();
        test = new ArrayList<Integer>();
        database = new ArrayList<double[]>();
    }

    public Classifier(ArrayList<double[]> db) {
        train = new ArrayList<Integer>();
        test = new ArrayList<Integer>();
        database = db;

        int train_size = (int)(database.size() * 0.8);
        
        for (int i = 0; i < database.size(); i++) {
            if (i < train_size) {
                train.add(i);
            }
            else {
                test.add(i);
            }
        }

        //System.out.print("Training instances: "); displayIntList(train); System.out.print(", size " + train.size() + "\n");
        //System.out.print("Testing instances: "); displayIntList(test); System.out.print(", size " + test.size() + "\n");
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
