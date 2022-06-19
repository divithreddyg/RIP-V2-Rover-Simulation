import java.util.ArrayList;
import java.util.List;

public class RoutingTable {
    int roverId = 0;
    String ipAddress = "";
    ArrayList<RoutingTableEntry> routingTable = null;

    /**
     * Creates an routing table for the current rover
     * @param id - id of the rover
     * @param ipAddress - ip address of the rover
     */
    public RoutingTable(int id, String ipAddress) {
        roverId = id;
        this.ipAddress = "10.0." + id + ".0";
        routingTable = new ArrayList<>();
        routingTable.add(new RoutingTableEntry(id, this.ipAddress, id, 0));
    }

    /**
     * Returns the timestamp for a certain id in the routing table
     * @param id - id of the rover
     * @return - timestamp for the rover
     */
    public long getTimeStamp(int id) {
        for (RoutingTableEntry tableEntry : this.routingTable) {
            if (tableEntry.id == id) {
                return tableEntry.timeStamp;
            }
        }
        return 0;
    }

    /**
     * Sets cost to 16 for the rover entries whose timestamps have
     * expired
     * @return - true if it has removed any entries
     */
    public boolean deleteExpiredEntries() {
        long current_time = System.currentTimeMillis();
        boolean isDeleted = false;
        List<RoutingTableEntry> removeEntries = new ArrayList<>();
        for (RoutingTableEntry tableEntry : this.routingTable) {
            if ((current_time - tableEntry.getTimeStamp()) / 1000 > 10 && tableEntry.cost != 16) {
                isDeleted = true;
                tableEntry.setCost(16);
                removeEntries.add(tableEntry);
            }
        }
//        this.routingTable.removeAll(removeEntries);
        for (RoutingTableEntry entry : removeEntries) {
            // Set the cost of all the entries who's next hop is
            // the deleted entry
            removeNextHops(entry.id);

        }
        return isDeleted;
    }

    /**
     * update the timestamp of rover roverID
     * @param roverId - rover id
     */
    public void updateTimestamp(int roverId) {
        long current_time = System.currentTimeMillis();
        for (int i=0; i<this.routingTable.size();i++){
            if (this.routingTable.get(i).id == roverId) {
                this.routingTable.get(i).setTimeStamp(current_time);
                break;
            }
        }
    }

    /**
     * Return the nextHops of the rover "id"
     * @param id - rover id
     */
    private void removeNextHops(int id) {
        for (int i=0; i<routingTable.size(); i++) {
            if (id == routingTable.get(i).id) {
                routingTable.get(i).setCost(16);
            }
        }
    }

    /**
     * Check if the routing table contains a certain ip address
     * @param ip - ip of the rover
     * @return - true if ip is present in the table
     */
    public boolean contains(String ip) {
        for (RoutingTableEntry tableEntry : this.routingTable) {
            if (tableEntry.getIp().equals(ip)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the Routing table entry from the ip address
     * @param ip - ip address of the rover
     * @return - returns RoutingTableEntry of the ip, null
     * if it doesn't exist
     */
    public RoutingTableEntry getFromIP(String ip) {

        for (RoutingTableEntry tableEntry : this.routingTable) {
            if (tableEntry.getIp().equals(ip)) {
                return tableEntry;
            }
        }
        return null;
    }

    /**
     * replaces a routing table entry in the routing table
     * @param id - id to be replaced
     * @param entry - entry to be replaced with
     */
    public void replaceEntry(int id, RoutingTableEntry entry) {
        for (int i=0; i<this.routingTable.size(); i++) {
            if (this.routingTable.get(i).getId() == id) {
                this.routingTable.set(i, entry);
                return;
            }
        }
        this.routingTable.add(entry);
    }

    /**
     * returns the routing table
     * @return - routing table
     */
    public ArrayList<RoutingTableEntry> getRoutingTable() {
        return routingTable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Address\t\t | Next Hop\t | Cost\n");
        sb.append("===============================================\n");
        for(RoutingTableEntry entry : routingTable) {
            sb.append(entry.toString());
        }
        return sb.toString();
    }
}
