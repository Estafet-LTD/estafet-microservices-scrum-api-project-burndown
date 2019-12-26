package com.estafet.microservices.api.project.burndown.container.tests;

import com.estafet.microservices.scrum.lib.commons.jms.TopicProducer;

public class UpdatedSprintTopic extends TopicProducer {
	
	private UpdatedSprintTopic() {
		super("new.sprint.topic");
	}
	
	public static void send(String message) {
		new UpdatedSprintTopic().sendMessage(message);
	}
	
}