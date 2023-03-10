import java.io.File;
import java.util.*;

import classes.SetNode;
import classes.Classifier;
import classes.Validator;

class Main {
    public static ArrayList<double[]> database = new ArrayList<double[]>();

    public static void main(String args[]) throws Exception {
        File file = new File("datasets/smallDataTEST.txt");
        Scanner sc = new Scanner(file);
        
        populateDataset(sc);
        SetNode best_feature_set = backwardsElimination();
        ArrayList<Integer> set = best_feature_set.feature_set;

        Classifier nn_classifer = new Classifier(database);
        
        /*
        System.out.print("Finished search! The best feature subset is ");
        displayIntList(set);
        System.out.print(", with accuracy of " + best_feature_set.accuracy);
        */
        sc.close();  
    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * * * FEATURE SEARCH ALGORITHMS * * * * *
    public static SetNode forwardSelection() {
        ArrayList<SetNode> subsets = new ArrayList<SetNode>();
        ArrayList<Integer> current_feature_set = new ArrayList<Integer>();
        double highest_accuracy = 0;

        for (int i = 1; i < database.get(0).length; i++) {
            //System.out.println("On the " + i + "th level of the search tree");
            int feature_to_add = -1;
            double best_accuracy = 0;

            for (int j = 1; j < database.get(0).length; j++) {
                if (!isInSet(j, current_feature_set)) {
                    //System.out.println("--Considering adding the " + j +" feature");
                    ArrayList<Integer> temp = copyList(current_feature_set);
                    temp.add(j);

                    double accuracy = evaluation(temp, j + 1);  
                    /* 
                    System.out.print("Using feature(s) ");
                    displayIntList(temp);
                    System.out.print(" accuracy is " + accuracy + "\n");
                    */
                    if (accuracy > best_accuracy) {
                        best_accuracy = accuracy;
                        feature_to_add = j;
                    }
                }         
            }
            //System.out.println("On level " + i + ", i added feature " + feature_to_add + " to current set");
            current_feature_set.add(feature_to_add);
            subsets.add(new SetNode(current_feature_set, best_accuracy));
            /*
            System.out.print("Feature set {");
            for (int a = 0; a < current_feature_set.size(); a++) {
                System.out.print(current_feature_set.get(a) + ", ");
            }
            System.out.print(feature_to_add + "} was best, accuracy is " + best_accuracy + "\n\n");
            */
            if (best_accuracy > highest_accuracy) {
                highest_accuracy = best_accuracy;
            }
            else {
                //System.out.println("(Warning, accuracy has decreased)\n");
            }
        }

        SetNode best_subset = new SetNode();
        for (int i = 0; i < subsets.size(); i++) {
            if (best_subset.accuracy < subsets.get(i).accuracy) {
                best_subset = subsets.get(i);
            }
        }
        return best_subset;
    }

    public static SetNode backwardsElimination() {
        ArrayList<SetNode> subsets = new ArrayList<SetNode>();
        ArrayList<Integer> current_feature_set = new ArrayList<Integer>();
        double highest_accuracy = 0;

        for (int i = 1; i < database.get(0).length; i++) {
            current_feature_set.add(i);
        }

        highest_accuracy = evaluation(current_feature_set, 0);
        subsets.add(new SetNode(current_feature_set, highest_accuracy));

        System.out.print("Feature set ");
        displayIntList(current_feature_set);
        System.out.print(" was best, accuracy is " + highest_accuracy + "\n\n");

        for (int i = 1; i < database.get(0).length; i++) {
            //System.out.println("On the " + i + "th level of the search tree");
            int feature_to_remove = -1;
            double best_accuracy = 0;

            for (int j = 1; j < database.get(0).length; j++) {
                if (isInSet(j, current_feature_set)) {
                    //System.out.println("--Considering removing the " + j +" feature");
                    ArrayList<Integer> temp = copyList(current_feature_set);
                    removeInList(temp, j);

                    double accuracy = evaluation(temp, j + 1);  
                    
                    /*
                    System.out.print("Using feature(s) ");
                    displayIntList(temp);
                    System.out.print(" accuracy is " + accuracy + "\n");
                    */

                    if (accuracy > best_accuracy) {
                        best_accuracy = accuracy;
                        feature_to_remove = j;
                    }
                }         
            }
            //System.out.println("On level " + i + ", i removed feature " + feature_to_remove + " from current set");
            removeInList(current_feature_set, feature_to_remove);
            subsets.add(new SetNode(current_feature_set, best_accuracy));
            /*
            System.out.print("Feature set ");
            displayIntList(current_feature_set);
            System.out.print(" was best, accuracy is " + best_accuracy + "\n\n");
            */
            if (best_accuracy > highest_accuracy) {
                highest_accuracy = best_accuracy;
            }
            else {
                //System.out.println("(Warning, accuracy has decreased)\n");
            }
        }

        SetNode best_subset = new SetNode();
        for (int i = 0; i < subsets.size(); i++) {
            if (best_subset.accuracy < subsets.get(i).accuracy) {
                best_subset = subsets.get(i);
            }
        }
        return best_subset;
    }

    public static double evaluation(ArrayList<Integer> nodes, int k) {
        Random rand = new Random();
        return (rand.nextDouble() * 100);
    }
    


    
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * * * OTHER HELPER FUNCTIONS * * * * *
    public static boolean isInSet(int a, ArrayList<Integer> featureSet) {
        for (int i = 0; i < featureSet.size(); i++) {
            int feature = featureSet.get(i);

            if (feature == a) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Integer> copyList(ArrayList<Integer> original) {
        ArrayList<Integer> new_list = new ArrayList<Integer>();

        for (int i = 0; i < original.size(); i++) {
            new_list.add(original.get(i));
        }

        return new_list;
    }

    public static void removeInList(ArrayList<Integer> original, int feature) {
        for (int i = 0; i < original.size(); i++) {
            if (original.get(i) == feature) {
                original.remove(i);
            }
        }
    }

    public static void displayIntList(ArrayList<Integer> list) {
        System.out.print("{");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i != (list.size() - 1)) {
                System.out.print(", ");
            }
        }
        System.out.print("}");
    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * * * HELPER FUNCTIONS FOR DATABASE * * * * *
    public static void populateDataset(Scanner sc) {
        int i = 0;
        int j = 0;

        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().trim().split("\\s+");
            database.add(new double[line.length]);

            for (String entry : line) {
                if (entry.length() > 0) {
                    database.get(i)[j] = Double.parseDouble(entry);
                    j++;
                }
            }
            i++;
            j = 0;
        }

        //displayDatabase();
    }

    public static void displayDatabase() {
        for (int i = 0; i < database.size(); i++) {
            System.out.print("{");
            for (int j = 0; j < database.get(i).length; j++) {
                System.out.print(database.get(i)[j]);

                if (j != (database.get(i).length - 1)) {
                    System.out.print(", ");
                }
            }
            System.out.print("}");
            System.out.print("\n");
        }
    }
}