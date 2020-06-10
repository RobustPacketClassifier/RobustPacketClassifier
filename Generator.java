import java.util.Random;

public interface Generator {
    int generate();

    static Generator createDoubleGenerator(double prob, double prob1,  Random random, int mask1, int mask2) {
        return (()-> {
            double x = random.nextDouble();
            if (x > prob) {
                return 31;
            } else if (x < prob1) {
                return mask1;
            } else if (x < 2 * prob1) {
                return mask2;
            } else {
                return mask1 | mask2;
            }
        });
    }

    static Generator createSingleGenerator(double prob, Random random, int mask) {
        return (()->random.nextDouble() < prob ? mask : 31);
    }

}
