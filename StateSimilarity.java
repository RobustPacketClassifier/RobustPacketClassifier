import java.util.ArrayList;
import java.util.StringTokenizer;

public class StateSimilarity {
    FlowState flowState;
    int last_packet_no_l;
    int last_packet_no_r;
    int delta;

    public StateSimilarity(FlowState flowState, int delta) {
        this.flowState = flowState;
        last_packet_no_l = -1;
        last_packet_no_r = -1;
        this.delta = delta;
    }

    public boolean doesMatch(Packet packet, int mode) {
        if (!packet.flow.keyFlow.isMatchedBy(flowState.keyFlow, mode)) {
            return false;
        }
        return (packet.num >= last_packet_no_l  - delta) && (packet.num <= delta + last_packet_no_r);
    }

    public void notifySuccessClassification(Packet packet) {
        if (flowState != packet.flow) {
            throw new AssertionError();
        }

        last_packet_no_l = Math.max(last_packet_no_l, packet.num);
        last_packet_no_r = Math.max(last_packet_no_r, packet.num);


        if (last_packet_no_l > flowState.size) {
            throw new AssertionError();
        }
    }

    public void notifySuccessClassificationMultiple(Packet p) {
        if (p.flow.classifiedBy != flowState.classifiedBy) {
            throw new AssertionError();
        }

        last_packet_no_r = Math.max(last_packet_no_r, p.num);
    }
}
