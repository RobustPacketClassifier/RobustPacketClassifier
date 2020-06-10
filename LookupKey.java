import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class LookupKey {
    ArrayList<Long> fields = new ArrayList<>();

    public LookupKey(ArrayList<Long> fields) {
        this.fields = new ArrayList<>(fields);
    }

    public boolean isMatchedBy(Rule r) {

        return isMatchedBy(r, (1 << r.ranges.size())-1);
    }

    public boolean isMatchedBy(Rule r, int mask) {
        for (int i = 0; i < r.ranges.size(); i++) {
            if ((mask & (1 << i)) == 0) {
                continue;
            }
            if (r.ranges.get(i).l > fields.get(i)  ||  r.ranges.get(i).r < fields.get(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isMatchedBy(LookupKey keyH, int mask) {
        for (int i = 0; i < keyH.fields.size(); i++) {
            if ((mask & (1 << i)) == 0) {
                continue;
            }
            if (!keyH.fields.get(i).equals(fields.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static LookupKey createFromString(String s) {
        StringTokenizer str = new StringTokenizer(s);
        ArrayList<Long> ans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ans.add(Long.parseLong(str.nextToken()));
        }
        return new LookupKey(ans);
    }

    @Override
    public String toString() {
        return fields.toString();
    }

    public String getMaskRepresentation(int mask) {
        ArrayList<Long> fieldRes = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            if ((mask & (1 << i)) == 0) {
                fieldRes.add(-1L);
            } else {
                fieldRes.add(fields.get(i));
            }
        }
        return fieldRes.toString();
    }
}
