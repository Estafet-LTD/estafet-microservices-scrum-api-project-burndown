package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.model.Project;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

@Component
public class NewProjectConsumer {

	@Autowired
	private Tracer tracer;
	
	@Autowired
	private ProjectBurndownService projectBurndownService;

	@JmsListener(destination = "new.project.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		ActiveSpan activeSpan = tracer.buildSpan("newProject").startActive();
		try {
			projectBurndownService.newProject(Project.fromJSON(message));
		} finally {
			activeSpan.deactivate();
		}
	}

}
