public class Packet {
    int num;
    FlowState flow;

    public Packet(FlowState flow, int num) {
        this.flow = flow;
        this.num = num;
        if (num >= flow.size) {
            throw new AssertionError();
        }
    }
}
