import java.lang.reflect.Array;
import java.util.*;

public class Classifier {
    ArrayList<Rule> rules = new ArrayList<>();
    TreeMap<Long, ArrayList<Rule>> index = new TreeMap<>();

    public Classifier(ArrayList<Rule> rules) {
        this.rules = new ArrayList<>(rules);
        for (Rule r : rules) {
            if (!index.containsKey(r.ranges.get(0).l)) {
                index.put(r.ranges.get(0).l, new ArrayList<>());
            }
            if (!index.containsKey(r.ranges.get(0).r)) {
                index.put(r.ranges.get(0).r, new ArrayList<>());
            }
        }

        for (Rule r : rules) {
            Map.Entry<Long, ArrayList<Rule>> iter = index.floorEntry(r.ranges.get(0).l);
            if (!iter.getKey().equals(r.ranges.get(0).l)) {
                throw new AssertionError();
            }
            while (true) {
                iter.getValue().add(r);
                if (iter.getKey().equals(r.ranges.get(0).r)) {
                    break;
                }
                iter = index.higherEntry(iter.getKey());
            }
        }
    }

    public Rule getClassifyingRule(LookupKey key, int mode) {
        if (!index.containsKey(key.fields.get(0)))  {
            throw new AssertionError();
        }
        for (Rule rule : index.get(key.fields.get(0))) {
            if (key.isMatchedBy(rule, mode)) {
                return rule;
            }
        }
        throw new AssertionError();
    }


}
