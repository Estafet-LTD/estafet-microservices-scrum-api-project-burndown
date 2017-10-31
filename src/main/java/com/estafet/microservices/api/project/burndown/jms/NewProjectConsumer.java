package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@Component
public class NewProjectConsumer {

	@Autowired
	private ProjectBurndownService projectBurndownService;

	@JmsListener(destination = "new.project.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		projectBurndownService.newProject(ProjectBurndown.fromJSON(message));
	}

}
