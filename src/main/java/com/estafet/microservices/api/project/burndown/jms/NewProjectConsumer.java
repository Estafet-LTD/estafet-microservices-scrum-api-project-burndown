package com.estafet.microservices.api.project.burndown.jms;

public interface NewProjectConsumer {

	void onMessage(String message);

}