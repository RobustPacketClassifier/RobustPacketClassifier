import java.util.ArrayList;
import java.util.StringTokenizer;

public class Rule {
     int id;
     static int cID = 0;
     ArrayList<Range> ranges = new ArrayList<>();

     public Rule(ArrayList<Range> ranges) {
         this.ranges = new ArrayList<>(ranges);
         this.id = cID;
         cID++;
     }

     public boolean doesIntersect(Rule another, int mask) {
            for (int i = 0; i < ranges.size(); i++) {
                if (mask %2 == 1 && !ranges.get(i).isIntersect(another.ranges.get(i))) {
                    return false;
                }
                mask /= 2;
            }
            return true;
     }

    public boolean doesIntersect(Rule another) {
        return doesIntersect(another, (1 << ranges.size()) - 1);
    }

    @Override
    public String toString() {
        String s = "";
        for (Range r : ranges) {
            s += r;
        }
        return s;
    }

    public static Rule createFromString(String s) {
        StringTokenizer str = new StringTokenizer(s, "@ :\t");
        ArrayList<Range> ans = new ArrayList<>();
        ans.add(Range.createFromIp(str.nextToken()));
        ans.add(Range.createFromIp(str.nextToken()));
        ans.add(new Range(Long.parseLong(str.nextToken()), Long.parseLong(str.nextToken())));
        ans.add(new Range(Long.parseLong(str.nextToken()), Long.parseLong(str.nextToken())));
        ans.add(Range.createFromHex(str.nextToken()));
        return new Rule(ans);
    }

}
