package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;

public class TemperatureDevice {

	private static final int COUNT = 10;

	public static void main(String[] args) {

		TemperatureSensor sn = new TemperatureSensor();
		Client tempDevice = new Client("tempDevice", Common.BROKERHOST, Common.BROKERPORT);

		String topic = "TemperatureMeasurements";
		
		tempDevice.connect();

		for (int i = 0; i < COUNT; i++) {
			String temp = Integer.toString(sn.read());
			tempDevice.publish(topic, temp);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		tempDevice.disconnect();
		System.out.println("Temperature device stopping ... ");
	}

}
