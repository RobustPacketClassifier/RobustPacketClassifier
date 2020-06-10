import java.util.*;

public class SingleExpRunner {
    public static long run_experiment(ArrayList<Flow> flows, Generator generator, int delta, int[] execs, int delta_comp, boolean isOptimized) {
	if (flows == null) {
	    return 0;
	}
        Queue<Packet> toInsert = new ArrayDeque<>();
        ArrayList<FlowState> flowStates = new ArrayList<>();
        for (Flow f : flows) {
            FlowState flowState = new FlowState(f, delta);
            flowStates.add(flowState);
            toInsert.addAll(flowState.createInitialPackets());
        }

        HashMap<String, ArrayList<StateSimilarity>> stateListMap = new HashMap<>();

        for (int i = 0; toInsert.size() > 0; i++) {
            Packet p = toInsert.poll();
            int mask = generator.generate();

            if (mask != 31 && (p.num < delta)) {
                toInsert.add(p);
                continue;
            }

            int maska = -1;
            for (int cand : execs) {
                if (mask == (mask | cand)) {
                    maska = cand;
                    break;
                }
            }

            if (maska == -1) {
                toInsert.add(p);
                continue;
            }

            ArrayList<StateSimilarity> stateList = stateListMap.get(p.flow.keyFlow.getMaskRepresentation(maska));
            ArrayList<StateSimilarity> matched = new ArrayList<>();
            if (stateList != null) {
                for (StateSimilarity st : stateList) {
                    if (st.doesMatch(p, mask)) {
                        matched.add(st);
                    }
                }
            }

            if (matched.size() > 0) {
                HashSet<Integer> indices = new HashSet<>();
                for (StateSimilarity sm : matched) {
                    indices.add(sm.flowState.classifiedBy);
                }

                if (indices.size() > 1 || (!isOptimized && (matched.size() > 1))) {
                    toInsert.add(p);
                    continue;
                }

                toInsert.addAll(p.flow.notifySuccessClassification(p, i));
                if (matched.size() > 1) {
                    for (StateSimilarity st : matched) {
                        st.notifySuccessClassificationMultiple(p);
                    }
                }
                else {
                    matched.get(0).notifySuccessClassification(p);
                }
            }
            else {
                if (p.num > delta) {
                    throw new AssertionError();
                }
                StateSimilarity state = new StateSimilarity(p.flow, delta_comp);
                toInsert.addAll(p.flow.notifySuccessClassification(p, i));
                state.notifySuccessClassification(p);
                for (int m : execs) {
                    String cand = p.flow.keyFlow.getMaskRepresentation(m);
                    if (!stateListMap.containsKey(cand)) {
                        stateListMap.put(cand, new ArrayList<>());
                    }
                    stateListMap.get(cand).add(state);
                }
            }

            if (p.flow.firstNotClassified == p.flow.size) {
                for (int exec: execs) {
                    ArrayList<StateSimilarity> stateListC = stateListMap.get(p.flow.keyFlow.getMaskRepresentation(exec));
                    for (StateSimilarity st : stateListC) {
                        if (st.flowState == p.flow) {
                            stateListC.remove(st);
                            break;
                        }
                    }
                }
            }
        }

        long tm = 0;
        for (FlowState flowState : flowStates) {
            tm = tm + (long) flowState.flowCompletionTime;
            if (flowState.firstNotClassified != flowState.size) {
                throw new AssertionError();
            }
        }
        return tm;
    }
}
