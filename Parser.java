import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Parser {
    static ArrayList<Rule> parseClassifier(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename));
        ArrayList<Rule> classfier = new ArrayList<>();
        while (in.hasNext()) {
            classfier.add(Rule.createFromString(in.nextLine()));
        }
        System.err.println("Classifier_size: " + classfier.size());
        return classfier;
    }

    static ArrayList<LookupKey> parseTraces(String filename) throws FileNotFoundException {
        Scanner in_trace = new Scanner(new File(filename));
        ArrayList<LookupKey> lookupKeys = new ArrayList<>();
        while (in_trace.hasNext()) {
            lookupKeys.add(LookupKey.createFromString(in_trace.nextLine()));
        }
        HashSet<String> visited = new HashSet<>();
        ArrayList<LookupKey> ans = new ArrayList<>();
        for (LookupKey key : lookupKeys) {
            if (!visited.contains(key.toString())) {
                ans.add(key);
                visited.add(key.toString());
            }
        }
        System.err.println("Traces_size: " + ans.size());
        return ans;
    }


}
