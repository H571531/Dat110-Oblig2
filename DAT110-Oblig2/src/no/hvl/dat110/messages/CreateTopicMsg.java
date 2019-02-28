package no.hvl.dat110.messages;

public class CreateTopicMsg extends Message {
	
	// TODO: 
	// Implement objectvariables, constructor, get/set-methods, and toString method
	private String identifier; 
	
	public CreateTopicMsg(String user, String identifier) {
		super(MessageType.CREATETOPIC, user);
		this.identifier=identifier;
	}
	
	

	public String getIdentifier() {
		return identifier;
	}



	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}



	@Override
	public String toString() {
		return "Topic: "+identifier;
	} 
	
	
	
	
	
}
