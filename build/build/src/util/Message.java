package util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Message {
	private final StringProperty message;
	private final StringProperty people;
	
	public Message() {
		this(null,null);
	}
	
	public Message(String people,String message) {
		this.people = new SimpleStringProperty(people);
		this.message = new SimpleStringProperty(message);
	}
	
	public StringProperty getMessage() {
		return message;
	}
	
	public StringProperty getPeople() {
		return people;
	}
}
