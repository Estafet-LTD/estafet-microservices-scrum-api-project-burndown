package com.estafet.microservices.api.project.burndown.container.tests;

public class UpdatedStoryTopic extends JMSTopic {
	
	private UpdatedStoryTopic() {
		super("update.story.topic");
	}
	
	public static void sendMessage(String message) {
		new UpdatedStoryTopic().send(message);
	}
	
}