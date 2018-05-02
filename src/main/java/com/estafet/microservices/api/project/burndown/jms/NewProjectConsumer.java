package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.jms.annotation.JmsListener;

public interface NewProjectConsumer {

	void onMessage(String message);

}