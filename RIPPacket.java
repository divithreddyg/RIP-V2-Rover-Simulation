import java.util.ArrayList;

public class RIPPacket {
    int command;
    int version;
    int roverId;
    ArrayList<RIPEntry> ripEntries;

    /**
     * Function takes in a byte array of data received from another rover
     * and creates a RIPPacket object
     * @param data - byte data received from another rover
     */
    public RIPPacket(byte[] data) {
        this.command = data[0];
        this.version = data[1];
        this.roverId = data[2];
        int totalCount = data[3];

        this.ripEntries = new ArrayList<>();

        // create a RIPEntry
        for (int index = 0; index < totalCount; index++) {
            int pos = index * 20 + 4;
            byte[] RIPEntry = new byte[20];
            System.arraycopy(data, pos, RIPEntry, 0, 20);
            this.ripEntries.add(new RIPEntry(RIPEntry));
        }
    }

    /**
     * Creates a RIPPacket object containing information of the current rover
     * This Packet can be used to send data to other rovers
     * @param roverId - Rover ID
     * @param routingTable - Routing table of the rover
     */
    public RIPPacket(int roverId, RoutingTable routingTable) {
        this.command = 2;
        this.version = 2;
        this.roverId = roverId;
        this.ripEntries = new ArrayList<>();
        for (RoutingTableEntry entry : routingTable.getRoutingTable()) {
            ripEntries.add(new RIPEntry(entry.getId(), entry.getIp(), entry.nextHop, entry.cost));
        }
    }

    /**
     * Creates the RIP packet that can be sent across
     * @return - returns a byte array containing the RIP Packet
     */
    public byte[] createRIPPacketData() {
        byte[] buff = new byte[512];

        buff[0] = (byte) this.command;
        buff[1] = (byte) this.version;
        buff[2] = (byte) this.roverId;
        buff[3] = (byte) ripEntries.size();

        int i = 0;
        for (RIPEntry entry : this.ripEntries) {
            byte[] ripEntryData = entry.createRIPEntryPacket();
            System.arraycopy(ripEntryData, 0, buff, i * ripEntryData.length + 4, ripEntryData.length);
            i++;
        }

        return buff;
    }

    /**
     * returns the RoverID
     * @return - rover id
     */
    public int getRoverId() {
        return roverId;
    }

    /**
     * returns all the RIP entries
     * @return - list of RIP Entries
     */
    public ArrayList<RIPEntry> getRipEntries() {
        return ripEntries;
    }
}
