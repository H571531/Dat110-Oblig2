package no.hvl.dat110.broker;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import no.hvl.dat110.common.Logger;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.ConnectMsg;
import no.hvl.dat110.messages.CreateTopicMsg;
import no.hvl.dat110.messages.DeleteTopicMsg;
import no.hvl.dat110.messages.DisconnectMsg;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.MessageType;
import no.hvl.dat110.messages.PublishMsg;
import no.hvl.dat110.messages.SubscribeMsg;
import no.hvl.dat110.messages.UnsubscribeMsg;
import no.hvl.dat110.messagetransport.Connection;

public class Dispatcher extends Stopable {

	private Storage storage;
	
	ConcurrentHashMap<ClientSession, ClientThread> clientThreads;


	public Dispatcher(Storage storage) {
		super("Dispatcher");
		this.storage = storage;
		
		clientThreads = new ConcurrentHashMap<ClientSession, ClientThread>();

	}

	@Override
	public void doProcess() {

		Logger.lg(".");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void doStop() {
		
		//Krav i oppgave 5: mulighet for å stoppe alle threads
		for(ClientThread clientThread: clientThreads.values()) {
			clientThread.doStop();
		}
		
		super.doStop();
	}
	
	
	// called from Broker after having established the underlying connection
	public void onConnect(ConnectMsg msg, Connection connection) {

		String user = msg.getUser();
		storage.addClientSession(user, connection);
		
		Logger.log("onConnect:" + msg.toString());

		
		
		//Oppgave 5: Hvis det har kommet noen meldinger siden disconnect, send disse
		Collection<Message> storedMessages = storage.retrieveBufferedMessages(user);
		ClientSession session = storage.getSession(user);
		
		if(storedMessages != null) {
			for(Message message: storedMessages) {
				session.send(message);
			}
		}
		
		ClientThread newClient = new ClientThread(("user_" + user), storage.getSession(user), storage);
		
		clientThreads.put(session, newClient);
		
		newClient.start();

	}
	
	
	
	/*
	 * Alle metoder under flyttet til ClientThread
	 */
	
	
	
//	public void dispatch(ClientSession client, Message msg) {
//
//		MessageType type = msg.getType();
//
//		switch (type) {
//
//		case DISCONNECT:
//			onDisconnect((DisconnectMsg) msg);
//			break;
//
//		case CREATETOPIC:
//			onCreateTopic((CreateTopicMsg) msg);
//			break;
//
//		case DELETETOPIC:
//			onDeleteTopic((DeleteTopicMsg) msg);
//			break;
//
//		case SUBSCRIBE:
//			onSubscribe((SubscribeMsg) msg);
//			break;
//
//		case UNSUBSCRIBE:
//			onUnsubscribe((UnsubscribeMsg) msg);
//			break;
//
//		case PUBLISH:
//			onPublish((PublishMsg) msg);
//			break;
//
//		default:
//			Logger.log("broker dispatch - unhandled message type");
//			break;
//
//		}
//	}

	// called by dispatch upon receiving a disconnect message 
//	public void onDisconnect(DisconnectMsg msg) {
//
//		String user = msg.getUser();
//
//		Logger.log("onDisconnect:" + msg.toString());
//
//		storage.removeClientSession(user);
//
//	}
//
//	public void onCreateTopic(CreateTopicMsg msg) {
//
//		Logger.log("onCreateTopic:" + msg.toString());
//		// TODO: create the topic in the broker storage 
//		if(msg.getTopic()!=null) {
//			storage.createTopic(msg.getTopic());
//		}else {
//			Logger.log("Topic is null");
//		}
//		
//	}
//
//	public void onDeleteTopic(DeleteTopicMsg msg) {
//
//		Logger.log("onDeleteTopic:" + msg.toString());
//
//		// TODO: delete the topic from the broker storage
//		
//		storage.deleteTopic(msg.getTopic());
//	}
//
//	public void onSubscribe(SubscribeMsg msg) {
//
//		Logger.log("onSubscribe:" + msg.toString());
//		// TODO: subscribe user to the topic
//		storage.addSubscriber(msg.getUser(), msg.getTopic());
//	}
//
//	public void onUnsubscribe(UnsubscribeMsg msg) {
//		Logger.log("onUnsubscribe:" + msg.toString());
//		// TODO: unsubscribe user to the topic	
//		storage.removeSubscriber(msg.getUser(), msg.getTopic());
//	}
//
//	public void onPublish(PublishMsg msg) {
//		Logger.log("onPublish:" + msg.toString());
//		// TODO: publish the message to clients subscribed to the topic
//		Set<String> subscriptions = storage.getSubscribers(msg.getTopic());
//		for(String user: subscriptions) {
//			ClientSession session = storage.getSession(user);
//			if(session == null) {
//				storage.addMessageToBuffer(user, msg);
//			} else {
//				session.send(msg);
//			}
//		}
//	}
}
