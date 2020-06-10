import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Distribution {
    private static Random rnd = new Random(217);

    double[] prob;
    int[] size;
    int n;

    double meanFlowSize = 0;

    public Distribution(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename));
        n = in.nextInt();
        prob = new double[n+1];
        size = new int[n+1];
        for (int i = 0; i <= n; i++) {
            size[i] = in.nextInt();
            prob[i] = in.nextDouble();
        }

        for (int i = 1; i <= n; i++) {
            meanFlowSize += 0.5*(prob[i]-prob[i-1])*(size[i]+size[i-1]);
        }


        in.close();
    }

    public int generateRandomSize() {
        double p = rnd.nextDouble();
        for (int i = 1; ; i++) {
            if (p < prob[i]) {
                double dp = (p - prob[i-1])/(prob[i] - prob[i-1]);
                return (int)(Math.round(size[i-1]+dp*(size[i] - size[i-1])));
            }
        }
    }

    public int generateRandomStartPoint(int num) {
        double mid = 0.00 * meanFlowSize;
        return (int)(mid*(num+1) + 0.3*mid*rnd.nextGaussian());
    }

}
