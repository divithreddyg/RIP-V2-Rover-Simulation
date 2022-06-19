import java.net.*; 
import java.io.*; 
import java.util.*; 
import java.net.InetAddress; 

// Main.java
// (C) 2019 Sam Fryer
//
// Starts the UdpMulticastClient and the UdpMulticastSender threads
// and then just sits and waits forever.


class Main
{
 public static void main(String args[])
 {
        if (args.length>0) {
			// Get the node number (first arg)
		int nodeNum = Integer.parseInt(args[0]);
		System.out.println("I'm node " + nodeNum);


		// Start the rover thread
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			String address = (localhost.getHostAddress()).trim();
			System.out.println("Broadcasting from: "+address);

			Thread rover = new Thread(new Rover(nodeNum, address));
			rover.start();
			while(true) {Thread.sleep(10000);}
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
	else
		System.out.println("No input args! Must specify Node Number!");
 }
}
