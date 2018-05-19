package com.estafet.microservices.api.project.burndown.container.tests;

public class UpdatedStoryTopic extends TopicProducer {
	
	private UpdatedStoryTopic() {
		super("update.story.topic");
	}
	
	public static void send(String message) {
		new UpdatedStoryTopic().sendMessage(message);
	}
	
}