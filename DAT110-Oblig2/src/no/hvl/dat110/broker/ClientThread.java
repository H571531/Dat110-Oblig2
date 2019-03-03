package no.hvl.dat110.broker;

import java.util.Set;

import no.hvl.dat110.common.Logger;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.CreateTopicMsg;
import no.hvl.dat110.messages.DeleteTopicMsg;
import no.hvl.dat110.messages.DisconnectMsg;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.MessageType;
import no.hvl.dat110.messages.PublishMsg;
import no.hvl.dat110.messages.SubscribeMsg;
import no.hvl.dat110.messages.UnsubscribeMsg;
import no.hvl.dat110.messagetransport.Connection;

public class ClientThread extends Stopable {
	
	
	ClientSession session;
	Storage storage;

	public ClientThread(String name, ClientSession session, Storage storage) {
		// TODO Auto-generated constructor stub
		super(name);
		
		this.session = session;
		this.storage = storage;
		
		
	}
	
	public void dispatch(Message msg) {

		MessageType type = msg.getType();

		switch (type) {

		case DISCONNECT:
			onDisconnect((DisconnectMsg) msg);
			break;

		case CREATETOPIC:
			onCreateTopic((CreateTopicMsg) msg);
			break;

		case DELETETOPIC:
			onDeleteTopic((DeleteTopicMsg) msg);
			break;

		case SUBSCRIBE:
			onSubscribe((SubscribeMsg) msg);
			break;

		case UNSUBSCRIBE:
			onUnsubscribe((UnsubscribeMsg) msg);
			break;

		case PUBLISH:
			onPublish((PublishMsg) msg);
			break;

		default:
			Logger.log("broker dispatch - unhandled message type");
			break;

		}
	}

	@Override
	public void doProcess() {
		
		Message msg = null;

		if (session.hasData()) {
			msg = session.receive();
		}

		if (msg != null) {
			dispatch(msg);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	// called by dispatch upon receiving a disconnect message 
		public void onDisconnect(DisconnectMsg msg) {
			

			String user = msg.getUser();

			Logger.log("onDisconnect:" + msg.toString());
			
			
			storage.startBufferingMessages(user);
			
			storage.removeClientSession(user);
			
			this.doStop();

		}

		public void onCreateTopic(CreateTopicMsg msg) {
			
			Logger.log("onCreateTopic:" + msg.toString());

			// TODO: create the topic in the broker storage 
			
			storage.createTopic(msg.getTopic());

		}

		public void onDeleteTopic(DeleteTopicMsg msg) {
			
			Logger.log("onDeleteTopic:" + msg.toString());

			// TODO: delete the topic from the broker storage
			
			storage.deleteTopic(msg.getTopic());
			
		}

		public void onSubscribe(SubscribeMsg msg) {
			
			Logger.log("onSubscribe:" + msg.toString());

			// TODO: subscribe user to the topic
			
			
			storage.addSubscriber(msg.getUser(), msg.getTopic());
			
		}

		public void onUnsubscribe(UnsubscribeMsg msg) {
			
			Logger.log("onUnsubscribe:" + msg.toString());

			// TODO: unsubscribe user to the topic
			storage.removeSubscriber(msg.getUser(), msg.getTopic());
			

		}

		public void onPublish(PublishMsg msg) {

			Logger.log("onPublish:" + msg.toString());

			// TODO: publish the message to clients subscribed to the topic
			
			Set<String> subscriptions = storage.getSubscribers(msg.getTopic());
			
			for(String user: subscriptions) {
				ClientSession session = storage.getSession(user);
				if(session == null) {
					storage.addMessageToBuffer(user, msg);
				} else {
					session.send(msg);
				}
			}
		}

}
