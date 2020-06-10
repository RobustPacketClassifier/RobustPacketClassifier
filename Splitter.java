import java.util.ArrayList;

public class Splitter {
    public static void dfs(int v, ArrayList<Integer>[][] splitByExec, int[][] mapToExec, int cl, int color[]) {
        if (color[v] != 0) {
            if (color[v] != cl) {
                throw new AssertionError();
            }
            return;
        }
        color[v] = cl;
        for (int i = 0; i < mapToExec[v].length; i++) {
            for (int key : splitByExec[mapToExec[v][i]][i]) {
                dfs(key, splitByExec, mapToExec, cl, color);
            }
        }
    }

    public static ArrayList<ArrayList<LookupKey>> split(ArrayList<LookupKey> lookupKeys, Classifier classifierRunner, Integer... execs) {
        ArrayList<Integer>[][] splitByExec = new ArrayList[lookupKeys.size()][execs.length];
        for (int i = 0; i < lookupKeys.size(); i++) {
            for (int j = 0; j < execs.length; j++) {
                splitByExec[i][j] = new ArrayList<>();
            }
        }
        int[][] mapToExec = new int[lookupKeys.size()][execs.length];
        for (int i = 0; i < lookupKeys.size(); i++) {
            for (int j = 0; j < execs.length; j++) {
                mapToExec[i][j] = classifierRunner.getClassifyingRule(lookupKeys.get(i), execs[j]).id;
                splitByExec[mapToExec[i][j]][j].add(i);
            }
        }

        int[] color = new int[lookupKeys.size()];
        int cl = 0;
        for (int i = 0; i < lookupKeys.size(); i++) {
            if (color[i] != 0) {
                continue;
            }
            cl++;
            dfs(i, splitByExec, mapToExec, cl, color);
        }
        ArrayList<ArrayList<LookupKey>> keys = new ArrayList<>();
        for (int i = 0; i < cl; i++) {
            keys.add(new ArrayList<>());
        }
        for (int i = 0; i < color.length; i++) {
            keys.get(color[i]-1).add(lookupKeys.get(i));
        }
        return keys;

    }

}
