package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.PublishMsg;
import no.hvl.dat110.iotsystem.*;

public class DisplayDevice {
	
	private static final int COUNT = 10;
	
	private  boolean connected;
		
	public static void main (String[] args) {
		
		System.out.println("Display starting ...");
		Message recieved;
		Client display=new Client("display",Common.BROKERHOST,Common.BROKERPORT);
		display.connect();
		display.createTopic("temperature");
		display.subscribe("temperature");
		
		// TODO - START
				for(int i=0;i<COUNT;i++) {
					recieved=display.receive();
					System.out.println(recieved);
				}
		// TODO - END
		display.disconnect();
		System.out.println("Display stopping ... ");

		
	
	}
	
	
		
	
	
}
