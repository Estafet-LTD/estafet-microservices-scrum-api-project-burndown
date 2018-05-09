package com.estafet.microservices.api.project.burndown.container.tests;

public class NewSprintTopic extends TopicProducer {
	
	private NewSprintTopic() {
		super("new.sprint.topic");
	}
	
	public static void sendMessage(String message) {
		new NewSprintTopic().send(message);
	}
	
}