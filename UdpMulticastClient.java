import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

// Code originally from:
//https://www.developer.com/java/data/how-to-multicast-using-java-sockets.html
//
// edited by Sam Fryer.

public class UdpMulticastClient implements Runnable {

   public int port = 63001; // port to listen on
   public String broadcastAddress; // multicast address to listen on
   public Rover rover = null;

   // standard constructor
   public UdpMulticastClient(int thePort, String broadcastIp, Rover rover)
   {
      port = thePort;
      broadcastAddress = broadcastIp;
      this.rover = rover;
   }

   // listens to the ipaddress and reports when a message arrived
   public void receiveUDPMessage() throws
         IOException {
      byte[] buffer=new byte[512];

      // create and initialize the socket
      MulticastSocket socket=new MulticastSocket(port);
      InetAddress group=InetAddress.getByName(broadcastAddress);

      socket.joinGroup(group);


      while(true){
        try {
          DatagramPacket packet=new DatagramPacket(buffer,buffer.length);

          socket.receive(packet);
          if (rover.routingTable.deleteExpiredEntries()) {
              rover.messageSender.interrupt();
          }
          processPacket(packet);

        }catch(IOException ex){
          ex.printStackTrace();
        }
      }
   }

    /**
     * Process the packet
     * @param packet - Packet
     */
    public void processPacket(DatagramPacket packet) {
        byte[] data = packet.getData();
        String ipAddress = packet.getAddress().toString().substring(1);
        RIPPacket ripPacket = new RIPPacket(data);
        int neighborId = ripPacket.getRoverId();
        ArrayList<RIPEntry> ripEntries = ripPacket.getRipEntries();
        rover.routingTable.updateTimestamp(ripPacket.getRoverId());
        if (neighborId == this.rover.roverId) {
            return;
        }

        boolean change = false;
        for (RIPEntry ripEntry : ripEntries) {

            int sentCost = ripEntry.getCost();
            if (ripEntry.getNextHop() == this.rover.roverId) {
                sentCost = 16;
            }
            // If routing table has an entry for destination
            if (this.rover.routingTable.contains(ripEntry.getIpAddress())) {
                RoutingTableEntry routingTableEntry = this.rover.routingTable.getFromIP(ripEntry.getIpAddress());

                if (routingTableEntry.cost == 0) {
                    continue;
                }
                else {
                    if (routingTableEntry.cost > sentCost + 1) {
                        routingTableEntry.setNextHop(ripEntry.getNextHop());
                        routingTableEntry.cost = (sentCost + 1);
                        this.rover.routingTable.replaceEntry(routingTableEntry.getId(), routingTableEntry);
                        change = true;
                    }
                    else {
                        if (sentCost == 16) {
                            routingTableEntry.cost = 16;
                            this.rover.routingTable.replaceEntry(routingTableEntry.getId(), routingTableEntry);
                            change = true;
                        }
                    }
                }
            }
            else {
                if (sentCost < 15) {
                    this.rover.routingTable.routingTable.add(new RoutingTableEntry(ripEntry.getId(), ripEntry.getIpAddress(), ripEntry.getNextHop(), (sentCost + 1)));
                    change = true;
                }
            }
        }
        if (change) {
            rover.messageSender.interrupt();
        }

    }


   // the thread runnable.  just starts listening.
   @Override
   public void run(){
     try {
       receiveUDPMessage();
     }catch(IOException ex){
       ex.printStackTrace();
     }
   }
}
