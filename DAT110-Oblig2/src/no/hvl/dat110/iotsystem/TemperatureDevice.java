package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.messages.Message;

public class TemperatureDevice {
	
	private static final int COUNT = 10;
	
	public static void main(String[] args) {
		
		TemperatureSensor sn = new TemperatureSensor();
		Message recieved;
		Client tempDevice=new Client("tempDevice",Common.BROKERHOST,Common.BROKERPORT);
		tempDevice.connect();
		for(int i=0;i<COUNT;i++) {
			String temp=""+sn.read();
			tempDevice.publish("temperature",temp);
		}
		tempDevice.disconnect();
		System.out.println("Temperature device stopping ... ");	
	}
	
	
}
