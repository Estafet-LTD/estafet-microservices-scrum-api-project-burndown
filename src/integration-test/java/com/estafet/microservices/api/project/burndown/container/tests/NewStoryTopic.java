package com.estafet.microservices.api.project.burndown.container.tests;

public class NewStoryTopic extends JMSTopic {
	
	private NewStoryTopic() {
		super("new.story.topic");
	}
	
	public static void sendMessage(String message) {
		new NewStoryTopic().send(message);
	}
	
}