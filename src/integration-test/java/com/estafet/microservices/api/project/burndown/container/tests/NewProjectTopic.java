package com.estafet.microservices.api.project.burndown.container.tests;

public class NewProjectTopic extends TopicProducer {
	
	private NewProjectTopic() {
		super("new.project.topic");
	}
	
	public static void sendMessage(String message) {
		new NewProjectTopic().send(message);
	}
}