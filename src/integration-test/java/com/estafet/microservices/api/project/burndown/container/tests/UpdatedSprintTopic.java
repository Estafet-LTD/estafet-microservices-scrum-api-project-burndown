package com.estafet.microservices.api.project.burndown.container.tests;

public class UpdatedSprintTopic extends TopicProducer {
	
	private UpdatedSprintTopic() {
		super("new.sprint.topic");
	}
	
	public static void send(String message) {
		new UpdatedSprintTopic().sendMessage(message);
	}
	
}