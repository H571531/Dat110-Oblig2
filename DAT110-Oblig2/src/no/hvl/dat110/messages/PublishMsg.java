package no.hvl.dat110.messages;

public class PublishMsg extends Message {
	
	// TODO: 
	// Implement objectvariables, constructor, get/set-methods, and toString method
	private String message;
	private String topic;
	
	public PublishMsg(String user, String topic, String message) {
		super(MessageType.PUBLISH, user);
		this.message = message;
		this.topic = topic;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getTopic() {
		return this.topic;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	@Override
	public String toString() {
		return "Message [type=" + this.getType() + ", user=" + this.getUser() + ", topic=" + topic + ", message=" + message + "]";
	}
}
