import java.util.Arrays;
import java.util.StringTokenizer;

public class Range {
    public long l, r;

    public Range(long l, long r) {
        this.l = l;
        this.r = r;
    }

    boolean isIntersect(Range o) {
        return (Math.max(l, o.l) <= Math.min(r, o.r));
    }

    @Override
    public String toString() {
        return "["+l+", "+r+"]";
    }

    public static Range createFromIp(String adr) {
        StringTokenizer str = new StringTokenizer(adr, "./");
        long ans = 0;
        while (true) {
            int k = Integer.parseInt(str.nextToken());
            if (str.hasMoreTokens()) {
                ans = ans * 256L + 1L*k;
            }
            else {
                int bit = 32-k;
                return new Range(ans - ans %(1L << bit), ans | ((1L << bit) - 1));
            }
        }
    }

    public static Range createFromHex(String adr) {
        StringTokenizer str = new StringTokenizer(adr, "/");
        long ans = Long.decode(str.nextToken());
        long mask = Long.decode(str.nextToken());
        if (mask == 255) {
            return new Range(ans, ans);
        }
        else if (mask == 0){
            return new Range(0, 255);
        }
        else  {
            throw new AssertionError();
        }
    }

}
