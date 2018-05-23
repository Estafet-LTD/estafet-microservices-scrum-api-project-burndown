package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.event.MessageEventHandler;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

import io.opentracing.Tracer;

@Component
public class NewProjectConsumer {
	
	public final static String TOPIC = "new.project.topic";

	@Autowired
	private Tracer tracer;
	
	@Autowired
	private ProjectBurndownService projectBurndownService;
	
	@Autowired
	private MessageEventHandler messageEventHandler;

	@JmsListener(destination = TOPIC, containerFactory = "myFactory")
	public void onMessage(String message, @Header("message.event.interaction.reference") String reference) {
		try {
			if (messageEventHandler.isValid(TOPIC, reference)) {
				projectBurndownService.newProject(ProjectBurndown.fromJSON(message));	
			}
		} finally {
			if (tracer.activeSpan() != null) {
				tracer.activeSpan().close();	
			}
		}
	}
}
