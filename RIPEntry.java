import java.nio.charset.StandardCharsets;


public class RIPEntry {
    byte[] addressFamilyIdentifier;
    byte[] ipAddress;
    int id;
    int nextHop;
    int cost;

    /**
     * Processes the Byte array of RIP Entry data from another rover
     * @param data - Byte array data from another rover
     */
    public RIPEntry(byte[] data) {
        int i=0;

        this.addressFamilyIdentifier = new byte[]{data[i++], data[i++]};

        this.id = data[i++];
        i+=1;

        this.ipAddress = new byte[4];
        for (int j=0; j<4;j++) {
            ipAddress[j] = data[i++];
        }

        i+=8;

        this.cost = data[i++];
        this.nextHop = data[i];
    }

    /**
     * Creates a RIP entry packet from the information
     * @param id - id of the rover
     * @param ipAddress - ip address of the rover
     * @param nextHop - NextHop to reach the rover
     * @param cost - cost to reach the rover
     */
    public RIPEntry(int id, String ipAddress, int nextHop, int cost) {
        this.addressFamilyIdentifier = new byte[]{0, 2};
        this.id = id;
        int counter = 0;
        this.ipAddress = new byte[4];
        for (String str : ipAddress.split("\\.")) {
            this.ipAddress[counter++] = (byte) Integer.parseInt(str);
        }
        this.nextHop = nextHop;
        this.cost = cost;
    }

    /**
     * Uses the current RIP Entry data to create a byte array for the
     * RIP Packet
     * @return - Byte array of RIP Entry data
     */
    public byte[] createRIPEntryPacket() {
        byte[] packet = new byte[20];
        int counter = 0;
        packet[counter++] = addressFamilyIdentifier[0];
        packet[counter++] = addressFamilyIdentifier[1];
        packet[counter++] = (byte) id;
        packet[counter++] = 0;
        for (byte b : ipAddress) {
            packet[counter++] = b;
        }
        for (int i=0; i < 8; i++) {
            packet[counter++] = 0;
        }
        packet[counter++] = (byte) this.cost;
        packet[counter++] = (byte) this.nextHop;
        packet[counter++] = 0;
        packet[counter] = 0;
        return packet;
    }

    /**
     * Gets the cost
     * @return - cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * gets the next hop
     * @return - next hop
     */
    public int getNextHop() {
        return nextHop;
    }

    /**
     * get the ip address
     * @return - ip address
     */
    public String getIpAddress() {
        String ip = "";
        for (byte b : ipAddress) {
            ip += Byte.toUnsignedInt(b) + ".";
        }
        ip = ip.substring(0, ip.length() - 1);
        return ip;
    }

    /**
     * get the id
     * @return - returns id
     */
    public int getId() {
        return id;
    }
}
