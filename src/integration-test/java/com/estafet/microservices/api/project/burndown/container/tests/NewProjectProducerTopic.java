package com.estafet.microservices.api.project.burndown.container.tests;

import com.estafet.microservices.scrum.lib.commons.jms.TopicProducer;

public class NewProjectProducerTopic extends TopicProducer {
	
	private NewProjectProducerTopic() {
		super("new.project.topic");
	}
	
	public static void send(String message) {
		new NewProjectProducerTopic().sendMessage(message);
	}
}