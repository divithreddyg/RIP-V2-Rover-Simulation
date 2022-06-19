public class Rover implements  Runnable {
    public int roverId = 0;
    public Thread messageReceiver = null;
    public Thread messageSender = null;
    public RoutingTable routingTable;
    public String ipAddress = "";

    /**
     * Creates a Rover object containing the routing table, the messageReceiver and MessageSender
     * @param roverId - RoverId
     * @param ipAddress - ipaddress of the rover
     */
    public Rover(int roverId, String ipAddress) {
        this.roverId = roverId;
        this.ipAddress = ipAddress;
        this.routingTable = new RoutingTable(roverId, ipAddress);
        messageReceiver = new Thread(new UdpMulticastClient(63001,"230.230.230.230", this));
        messageSender = new Thread(new UdpMulticastSender(63001,"230.230.230.230", this));

    }

    @Override
    public void run(){
        System.out.println("Running rover: " + roverId);
        messageReceiver.start();
        messageSender.start();
    }
}
