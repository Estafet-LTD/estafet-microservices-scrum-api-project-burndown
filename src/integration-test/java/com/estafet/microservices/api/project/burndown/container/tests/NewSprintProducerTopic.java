package com.estafet.microservices.api.project.burndown.container.tests;

public class NewSprintProducerTopic extends TopicProducer {
	
	private NewSprintProducerTopic() {
		super("update.sprint.topic");
	}
	
	public static void send(String message) {
		new NewSprintProducerTopic().sendMessage(message);
	}
	
}