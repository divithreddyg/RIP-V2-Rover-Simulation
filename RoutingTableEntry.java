public class RoutingTableEntry {
    int id;
    String ip;
    int nextHop;
    int cost;
    long timeStamp;

    /**
     * Creates a routingTableEntry for the given information
     * @param id - id of the rover
     * @param ip - ip address of the rover
     * @param nextHop - nexthop of the rover
     * @param cost - cost to reach the rover
     */
    public RoutingTableEntry(int id, String ip, int nextHop, int cost) {
        this.id = id;
        this.ip = ip;
        this.nextHop = nextHop;
        this.cost = cost;
        this.timeStamp = System.currentTimeMillis();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getCost() {
        return cost;
    }

    public int getId() {
        return id;
    }

    public int getNextHop() {
        return nextHop;
    }

    public String getIp() {
        return ip;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setNextHop(int nextHop) {
        this.nextHop = nextHop;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return this.ip + "/24\t | " +
                this.nextHop + "\t\t | " +
                this.cost + "\n";
    }
}
