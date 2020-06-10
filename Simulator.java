import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


public class Simulator {

    public long[] run_mini_mul_exp(ArrayList<ArrayList<LookupKey>> splitByExec, Classifier classifierRunner, Distribution distribution,  int delta, double prob, double probMid) {
        long[] ans = new long[4];
        for (int i = 0; i < splitByExec.size(); i++) {
            System.err.println(i + " " +splitByExec.get(i).size()+" "+ splitByExec.size());
            ArrayList<Flow> flows = Flow.createFlows(splitByExec.get(i), distribution, classifierRunner);

            long[] anst = new long[]{
                SingleExpRunner.run_experiment(flows, Generator.createSingleGenerator(0.0, new Random(i), 3), delta, new int[]{3}, delta, true),
                SingleExpRunner.run_experiment(flows, Generator.createDoubleGenerator(prob, probMid, new Random(i), 5,6), delta, new int[]{5}, delta, true),
                SingleExpRunner.run_experiment(flows, Generator.createDoubleGenerator(prob, probMid, new Random(i), 5,6), delta, new int[]{6}, delta, true),
		SingleExpRunner.run_experiment(flows, Generator.createDoubleGenerator(prob, probMid, new Random(i), 5,6), delta, new int[]{5,6}, delta, true)
            };

            for (int type = 0; type < anst.length; type++) {
                ans[type] += anst[type];
            }
        }
        return ans;
    }

    public long[] run_mini_exp(ArrayList<ArrayList<LookupKey>> splitByExec, Classifier classifierRunner, Distribution distribution, int delta, double prob) {
        long[] ans = new long[6];
        for (int i = 0; i < splitByExec.size(); i++) {
            System.err.println(i + " " +splitByExec.get(i).size()+" "+ splitByExec.size());

            ArrayList<Flow> flows = Flow.createFlows(splitByExec.get(i), distribution, classifierRunner);

            long[] anst = new long[]{
                SingleExpRunner.run_experiment(flows, Generator.createSingleGenerator(0.0, new Random(i), 3), delta, new int[]{3}, delta, true),
                SingleExpRunner.run_experiment(flows, Generator.createSingleGenerator(prob, new Random(i), 0), delta, new int[]{3}, delta, true),
                SingleExpRunner.run_experiment(flows, Generator.createSingleGenerator(prob, new Random(i), 3), delta, new int[]{3}, 2000000, false),
                SingleExpRunner.run_experiment(flows, Generator.createSingleGenerator(prob, new Random(i), 3), delta, new int[]{3}, delta, false),
		SingleExpRunner.run_experiment(flows, Generator.createSingleGenerator(prob, new Random(i), 3), delta, new int[]{3}, 2000000, true),
		SingleExpRunner.run_experiment(flows, Generator.createSingleGenerator(prob, new Random(i), 3), delta, new int[]{3}, delta, true),
            };

            for (int type = 0; type < anst.length; type++) {
                ans[type] += anst[type];
            }
        }
        return ans;
    }

    public void run_experiment(String filename, String anssuf) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filename + "."+anssuf);
        ArrayList<Rule> classifier = Parser.parseClassifier(filename + ".txt");
        ArrayList<LookupKey> lookupKeys = Parser.parseTraces(filename + ".txt_trace");
        Distribution distribution = new Distribution("CDF_dctcp.txt");

        Classifier classifierRunner = new Classifier(classifier);

        {
	    out.println("|delta|pe(f)|w(aprE)|w(apr1)|w(apr2)|w(apr3)|w(apr4)|");
            for (int i = 1; i<=7; i++) {
                ArrayList<ArrayList<LookupKey>> splitByExec = Splitter.split(lookupKeys, classifierRunner, 3);
                long[] ans = run_mini_exp(splitByExec, classifierRunner, distribution, 5, 0.1*i);
                printRes(out, ans, String.format("%.2f %.2f", 5.0, 0.1*i));
            }
            out.println();
        }


        {
	    out.println("|delta|pe(f)|w(aprE)|w(apr1)|w(apr2)|w(apr3)|w(apr4)|");	
            for (int i = 1; i<=10; i++) {
                ArrayList<ArrayList<LookupKey>> splitByExec = Splitter.split(lookupKeys, classifierRunner, 3);
                long[] ans = run_mini_exp(splitByExec, classifierRunner, distribution, i, 0.5);
                printRes(out, ans, String.format("%.2f %.2f", 1.0*i, 0.5));
            }
            out.println();
        }

        {
	    out.println("|p_x|w(aprf1)|w(aprf2)|w(aprf1, aprf2)");
            for (int i=0; i<=5; i++) {
                ArrayList<ArrayList<LookupKey>> splitByExec = Splitter.split(lookupKeys, classifierRunner, 5, 6);
                long[] ans = run_mini_mul_exp(splitByExec, classifierRunner, distribution, 5, 0.5, 0.05*i);
                printRes(out, ans, String.format("%.2f", 0.05*i));
            }
            out.println();
        }

    }

    private void printRes(PrintWriter out, long[] t, String format) {
        out.printf(format);
        for (int z = 1; z < t.length; z++) {
	    out.printf(" %.5f", 1.0 * t[z] / t[0]);
        }
        out.println();
        out.flush();
    }


    public static void main(String[] args) throws FileNotFoundException {
    	new Simulator().run_experiment(args[0], "ans");
    }

}
