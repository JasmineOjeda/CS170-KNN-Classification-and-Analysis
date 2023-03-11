import java.io.File;
import java.util.*;

import classes.SetNode;
import classes.Classifier;
import classes.Validator;

class Main {
    public static ArrayList<double[]> database = new ArrayList<double[]>();
    public static Classifier nn_classifier;
    public static Validator validator;
    public static String v_choice = "2";

    public static void main(String args[]) throws Exception {
        /*int[] f = {2, 8, 9, 11, 18, 26, 34, 36, 39}; // Test validation on random feature sets
        ArrayList<Integer> random_features = new ArrayList<Integer>();
        for (int i = 0; i < f.length; i++) { random_features.add(f[i]); }*/

        Scanner input = new Scanner(System.in);
        System.out.print("Enter the filepath to the dataset being tested: ");
        String file_name = input.nextLine(); // "datasets/smallData.txt";

        File file = new File(file_name);
        Scanner sc = new Scanner(file);
        populateDataset(sc);
        nn_classifier = new Classifier(database);
        SetNode best_feature_set = new SetNode();

        /*
        validator = new Validator(nn_classifier, random_features, database);
        System.out.println("Accuracy: " + validator.validation());
        */ 
        System.out.println("\n1. Forward Selection");
        System.out.println("2. Backwards Elimination");
        System.out.print("Type the number of the algorithm you want to run: ");
        String alg = input.nextLine();

        System.out.println("\n1. Random");
        System.out.println("2. Leave-one-out validator");
        System.out.print("Type the number of the validation you want to use: ");
        v_choice = input.nextLine();

        if (alg.equals("1")) {
            System.out.println("\n* * * FORWARDS SELECTION * * *\n");
            best_feature_set = forwardSelection();
        }
        else {
            System.out.println("\n* * * BACKWARDS ELIMINATION * * *\n");
            best_feature_set = backwardsElimination();
        }

        ArrayList<Integer> set = best_feature_set.feature_set;
        System.out.print("Finished search! The best feature subset is ");
        displayIntList(set);
        System.out.print(", with accuracy of " + best_feature_set.accuracy + "\n");
        if (v_choice.equals("1")) {
            System.out.println("NOTE: Random evaluation used");
        }

        //getFeatureData(set);
        input.close();
        sc.close();  
    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * * * FEATURE SEARCH ALGORITHMS * * * * *
    public static SetNode forwardSelection() {
        ArrayList<SetNode> subsets = new ArrayList<SetNode>();
        ArrayList<Integer> current_feature_set = new ArrayList<Integer>();
        double highest_accuracy = 0;

        //System.out.println("Using no features, I get an accuracy of " + evaluation(current_feature_set) + "\n");
        System.out.println("Beginning search...\n");
        for (int i = 1; i < database.get(0).length; i++) {
            //System.out.println("On the " + i + "th level of the search tree");
            int feature_to_add = -1;
            double best_accuracy = 0;

            for (int j = 1; j < database.get(0).length; j++) {
                if (!isInSet(j, current_feature_set)) {
                    //System.out.println("--Considering adding the " + j +" feature");
                    ArrayList<Integer> temp = copyList(current_feature_set);
                    temp.add(j);
                    double accuracy = evaluation(temp);  
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

        highest_accuracy = evaluation(current_feature_set);
        subsets.add(new SetNode(current_feature_set, highest_accuracy));
        
        //System.out.println("Using all features, I get an accuracy of " + highest_accuracy + "\n");
        System.out.println("Beginning search...\n");
        for (int i = 1; i < database.get(0).length; i++) {
            //System.out.println("On the " + i + "th level of the search tree");
            int feature_to_remove = -1;
            double best_accuracy = 0;

            for (int j = 1; j < database.get(0).length; j++) {
                if (isInSet(j, current_feature_set)) {
                    //System.out.println("--Considering removing the " + j +" feature");
                    ArrayList<Integer> temp = copyList(current_feature_set);
                    removeInList(temp, j);
                    double accuracy = evaluation(temp);  
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

    public static double evaluation(ArrayList<Integer> set) {
        if (v_choice.equals("2")) {
            Validator validator = new Validator(nn_classifier, set, database);
            return validator.validation();
        }

        Random rand = new Random();
        return rand.nextDouble();
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
        //normalizeDatabase();
        //displayDatabase();
    }

    public static void normalizeDatabase() {
        double[] max_min = findMaxMinFeatureValue();
        double max = max_min[0];
        double min = max_min[1];

        for (int i = 0; i < database.size(); i++) {
            for (int j = 1; j < database.get(i).length; j++) {
                database.get(i)[j] = (database.get(i)[j] - min) / (max - min);
            }
        }
    }

    public static double[] findMaxMinFeatureValue() {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        for (int i = 0; i < database.size(); i++) {
            for (int j = 1; j < database.get(i).length; j++) {
                if (database.get(i)[j] > max) {
                    max = database.get(i)[j];
                }
                if (database.get(i)[j] < min) {
                    min = database.get(i)[j];
                }
            }
        }
        
        double[] result = {max, min};
        return result;
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

    public static void getFeatureData(ArrayList<Integer> set) {
        for (int i = 0; i < database.size(); i++) {
            System.out.print(database.get(i)[0] + ", ");
            for (int j = 0; j < set.size(); j++) {
                System.out.print(database.get(i)[set.get(j)]);
                if (j != (set.size() - 1)) {
                    System.out.print(", ");
                }
            }
            System.out.print("\n");
        }
    }
}

