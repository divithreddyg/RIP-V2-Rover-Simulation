import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

// Code originally from:
//https://www.developer.com/java/data/how-to-multicast-using-java-sockets.html
//
// edited by Sam Fryer.


public class UdpMulticastSender implements Runnable  {

   public int port = 63001; // port to send on
   public String broadcastAddress; // multicast address to send on
   public int roverId = 0; // the arbitrary node number of this executable
   public RIPPacket packetCreator = null;
   public Rover rover = null;

   // standard constructor
   public UdpMulticastSender(int thePort, String broadcastIp, Rover rover)
   {
      port = thePort;
      broadcastAddress = broadcastIp;
      roverId = rover.roverId;
      this.rover = rover;
   }
  
   // Send the UDP Multicast message
   public void sendUdpMessage() throws IOException {
      // Socket setup
      System.out.println(this.rover.routingTable);
      DatagramSocket socket = new DatagramSocket();
      InetAddress group = InetAddress.getByName(broadcastAddress);
      
      // Packet setup
      RIPPacket newPacket = new RIPPacket(roverId, rover.routingTable);
      byte[] msg = newPacket.createRIPPacketData();
      DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);

      // let 'er rip
      socket.send(packet);
      socket.close();
   }

   // sleep for 5 seconds
   public void goToSleep() {
      try {
         Thread.sleep(5000);
      } catch (InterruptedException i) {
      }
   }

   // the thread runnable.  Starts sending packets every 500ms.
   @Override
   public void run(){
      while (true)
      {
        try {
          // set our message as "Node 1" (or applicable number)
           sendUdpMessage();
           goToSleep();
        }catch(Exception ex){
          ex.printStackTrace();
        }
      }
   }
}
