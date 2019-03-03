package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.PublishMsg;
import no.hvl.dat110.iotsystem.*;

public class DisplayDevice {

	private static final int COUNT = 10;

	public static void main(String[] args) {

		System.out.println("Display starting ...");
		PublishMsg received;
		Client display = new Client("display", Common.BROKERHOST, Common.BROKERPORT);

		String topic = "TemperatureMeasurements";

		display.connect();
		display.createTopic(topic);
		display.subscribe(topic);

		// TODO - START
		for (int i = 0; i < COUNT; i++) {
			
			received = (PublishMsg) display.receive();
			display(received.getMessage());
			
		}
		
		// TODO - END
		display.disconnect();
		System.out.println("Display stopping ... ");

	}

	private static void display(String message) {
		System.out.println("//////////////////////////////////////");
		System.out.println("DISPLAY: " + message);
		System.out.println("//////////////////////////////////////");
		
	}

}
