package com.estafet.microservices.api.project.burndown.container.tests;

import com.estafet.microservices.scrum.lib.commons.jms.TopicProducer;

public class UpdatedStoryTopic extends TopicProducer {
	
	private UpdatedStoryTopic() {
		super("update.story.topic");
	}
	
	public static void send(String message) {
		new UpdatedStoryTopic().sendMessage(message);
	}
	
}