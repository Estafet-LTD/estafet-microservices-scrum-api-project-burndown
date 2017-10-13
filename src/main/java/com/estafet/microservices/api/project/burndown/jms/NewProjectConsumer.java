package com.estafet.microservices.api.project.burndown.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.model.Project;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

@Component
public class NewProjectConsumer {

	private static final Logger logger = LoggerFactory.getLogger(NewProjectConsumer.class);
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private ProjectBurndownService projectBurndownService;

	@JmsListener(destination = "new.project.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		ActiveSpan span = tracer.buildSpan("newProjectBurnDown").startActive();
		try {
			Project project = Project.fromJSON(message);
			span.setTag("project.id", project.getId());
			span.log("Creating new project burndown");
			projectBurndownService.newProject(project);
		} finally {
			span.deactivate();
		}
	}

}
