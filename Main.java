import java.io.File;
import java.util.Scanner;

import classes.Node;

import java.util.*;

class Main {
    public static ArrayList<double[]> database = new ArrayList<double[]>();

    public static void main(String args[]) throws Exception {
        File file = new File("datasets/smallData.txt");
        Scanner sc = new Scanner(file);
        
        populateDataset(sc);

        ArrayList<Node> set = forwardSelection();
        
        System.out.print("Finished search! The best feature subset is {");
        for (int i = 0; i < set.size(); i++) {
            System.out.print(set.get(i).feature);
            if (i != (set.size() - 1)) {
                System.out.print(", ");
            }
        }
        System.out.print("}, with accuracy of " + set.get(set.size() - 1).score);
        
        sc.close();  
    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * * * FEATURE SEARCH ALGORITHMS * * * * *
    public static ArrayList<Node> forwardSelection() {
        ArrayList<Node> feature_set_nodes = new ArrayList<Node>();
        ArrayList<Node> result = new ArrayList<Node>();
        double highest_accuracy = 0;

        for (int i = 1; i < database.get(0).length; i++) {
            //System.out.println("On the " + i + "th level of the search tree");
            int feature_to_add = -1;
            double best_accuracy = 0;

            for (int j = 1; j < database.get(0).length; j++) {
                if (!isInSet(j, feature_set_nodes)) {
                    //System.out.println("--Considering adding the " + j +" feature");
                    ArrayList<Node> temp = copyList(feature_set_nodes);
                    temp.add(new Node(-1, j));

                    double accuracy = evaluation(feature_set_nodes, j + 1);  
                    
                    System.out.print("Using feature(s) {");
                    for (int a = 0; a < temp.size(); a++) {
                        System.out.print(temp.get(a).feature);
                        if (a != (temp.size() - 1)) {
                            System.out.print(", ");
                        }
                    }
                    System.out.print("} accuracy is " + accuracy + "\n");
                    
                    if (accuracy > best_accuracy) {
                        best_accuracy = accuracy;
                        feature_to_add = j;
                    }
                }         
            }
            //System.out.println("On level " + i + ", i added feature " + feature_to_add + " to current set");
            
            System.out.print("Feature set {");
            for (int a = 0; a < feature_set_nodes.size(); a++) {
                System.out.print(feature_set_nodes.get(a).feature + ", ");
            }
            System.out.print(feature_to_add + "} was best, accuracy is " + best_accuracy + "\n\n");
            
            if (best_accuracy > highest_accuracy) {
                highest_accuracy = best_accuracy;
                result.add(new Node(best_accuracy, feature_to_add));
            }
            else {
                System.out.println("(Warning, accuracy has decreased)\n");
            }
            
            feature_set_nodes.add(new Node(best_accuracy, feature_to_add));
        }

        return result;
    }

    public static ArrayList<Node> backwardsElimination() {
        ArrayList<Node> feature_set_nodes = new ArrayList<Node>();
        ArrayList<Node> result = new ArrayList<Node>();

        for (int i = 1; i < database.get(0).length; i++) {
            feature_set_nodes.add(new Node(-1, i));
        }

        for (int i = 1; i < database.get(0).length; i++) {
            //System.out.println("On the " + i + "th level of the search tree");
            int feature_to_remove = -1;
            double worst_accuracy = 0;

            for (int j = 1; j < database.get(0).length; j++) {
                if (isInSet(j, feature_set_nodes)) {
                    //System.out.println("--Considering adding the " + j +" feature");
                    ArrayList<Node> temp = copyList(feature_set_nodes);
                    removeInList(temp, j);

                    double accuracy = evaluation(temp, j + 1);  
                    /*
                    System.out.print("Using feature(s) {");
                    for (int a = 0; a < temp.size(); a++) {
                        System.out.print(temp.get(a).feature);
                        if (a != (temp.size() - 1)) {
                            System.out.print(", ");
                        }
                    }
                    System.out.print("} accuracy is " + accuracy + "\n");
                    */
                    if (accuracy < worst_accuracy) {
                        worst_accuracy = accuracy;
                        feature_to_remove = j;
                    }
                }         
            }
            //System.out.println("On level " + i + ", i added feature " + feature_to_remove + " to current set");
            
            System.out.print("Feature set {");
            for (int a = 0; a < feature_set_nodes.size(); a++) {
                System.out.print(feature_set_nodes.get(a).feature + ", ");
            }
            System.out.print(feature_to_remove + "} was worst, accuracy is " + worst_accuracy + "\n\n");
            /*
            if (best_accuracy > highest_accuracy) {
                highest_accuracy = best_accuracy;
                result.add(new Node(best_accuracy, feature_to_remove));
            }
            else {
                System.out.println("(Warning, accuracy has decreased)\n");
            }
            */
            removeInList(feature_set_nodes, feature_to_remove);
        }

        return result;
    }

    public static double evaluation(ArrayList<Node> nodes, int k) {
        Random rand = new Random();
        return (rand.nextDouble() * 100);
    }
    
    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * * * OTHER HELPER FUNCTIONS * * * * *
    public static boolean isInSet(int a, ArrayList<Node> featureSet) {
        for (int i = 0; i < featureSet.size(); i++) {
            Node node = featureSet.get(i);

            if (node.feature == a) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Node> copyList(ArrayList<Node> original) {
        ArrayList<Node> new_list = new ArrayList<Node>();

        for (int i = 0; i < original.size(); i++) {
            new_list.add(new Node(original.get(i).score, original.get(i).feature));
        }

        return new_list;
    }

    public static void removeInList(ArrayList<Node> original, int feature) {
        for (int i = 0; i < original.size(); i++) {
            if (original.get(i).feature == feature) {
                original.remove(i);
            }
        }
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