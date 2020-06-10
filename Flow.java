import java.util.ArrayList;

public class Flow {
    LookupKey keyFlow;
    int size;
    int startTime;
    int classifiedBy;

    public Flow(LookupKey keyFlow, Distribution distribution, int num, int classifiedBy) {
        this.keyFlow = keyFlow;
        size = distribution.generateRandomSize();
        startTime = distribution.generateRandomStartPoint(num);
        this.classifiedBy = classifiedBy;
    }


    public Flow(Flow flow) {
        this.keyFlow = flow.keyFlow;
        this.size = flow.size;
        this.startTime = flow.startTime;
        this.classifiedBy = flow.classifiedBy;
    }

    static ArrayList<Flow> createFlows(ArrayList<LookupKey> lookupKeys, Distribution distribution, Classifier classifier) {
	if (lookupKeys.size() > 10000) {
                return null;
        }
        ArrayList<Flow> ans = new ArrayList<>();
        for (LookupKey key : lookupKeys) {
            ans.add(new Flow(key, distribution, ans.size(), classifier.getClassifyingRule(key, 31).id));
        }
        return ans;
    }

}
