package com.estafet.microservices.api.project.burndown.container.tests;

import com.estafet.microservices.scrum.lib.commons.jms.TopicProducer;

public class NewStoryTopic extends TopicProducer {
	
	private NewStoryTopic() {
		super("new.story.topic");
	}
	
	public static void send(String message) {
		new NewStoryTopic().sendMessage(message);
	}
	
}