package com.estafet.microservices.api.project.burndown.container.tests;

public class UpdatedSprintTopic extends JMSTopic {
	
	private UpdatedSprintTopic() {
		super("new.sprint.topic");
	}
	
	public static void sendMessage(String message) {
		new UpdatedSprintTopic().send(message);
	}
	
}