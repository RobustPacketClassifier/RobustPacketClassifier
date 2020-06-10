import java.util.ArrayList;

public class FlowState extends Flow {

    int firstNotClassified;

    int flowCompletionTime = 0;

    int delta;

    boolean[] wasClassified;

    public FlowState(Flow flow, int delta) {
        super(flow);
        flowCompletionTime = flow.startTime;
        this.delta = delta;
        wasClassified = new boolean[delta*3+1];
    }

    public ArrayList<Packet> createInitialPackets() {
        ArrayList<Packet> ans = new ArrayList<>();
        for (int i = 0; i < Math.min(delta, size) ; i++) {
            ans.add(new Packet(this, i));
        }
        firstNotClassified = 0;
        return ans;
    }

    public ArrayList<Packet> notifySuccessClassification(Packet packet, int time) {
        if (firstNotClassified >= size) {
            throw new AssertionError();
        }
        wasClassified[packet.num%wasClassified.length] = true;
        int cnt = 0;
        for (int i = firstNotClassified; i < firstNotClassified + delta && i < size; i++) {
            if (wasClassified[i %wasClassified.length]) {
                cnt++;
            } else {
                break;
            }
        }
        if (firstNotClassified + cnt >= size) {
            flowCompletionTime = time - flowCompletionTime;
            //System.err.println(size+" "+time);
        }
        ArrayList<Packet> ans = new ArrayList<>();
        while (cnt > 0) {
            if (firstNotClassified + delta < size) {
                wasClassified[(firstNotClassified +delta)%wasClassified.length] = false;
                ans.add(new Packet(this, firstNotClassified +delta));
            }
            firstNotClassified++;
            cnt--;
        }
        return ans;
    }




}
