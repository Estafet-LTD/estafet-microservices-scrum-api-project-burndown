package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.model.ProjectBurndownSprint;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@Component
public class UpdatedSprintConsumer {

	@Autowired
	private ProjectBurndownService projectBurndownService;

	@JmsListener(destination = "update.sprint.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		projectBurndownService.updateBurndown(ProjectBurndownSprint.fromJSON(message));
	}

}
