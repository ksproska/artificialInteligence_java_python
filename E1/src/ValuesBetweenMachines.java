public class ValuesBetweenMachines {
    public final int id1, id2;
    private int flow, cost;

    public ValuesBetweenMachines(int id1, int id2, int flow, int cost) {
        this.id1 = id1;
        this.id2 = id2;
        this.flow = flow;
        this.cost = cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getCost() {
        return cost;
    }

    public int getFlow() {
        return flow;
    }

    @Override
    public String toString() {
        return "{(" + id1 + ", " + id2 + ") => f=" + flow + ", c=" + cost + '}';
    }
}
