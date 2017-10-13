package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.model.Sprint;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@Component
public class CompletedSprintConsumer {

	@Autowired
	private ProjectBurndownService projectBurndownService;

	@JmsListener(destination = "update.sprint.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		projectBurndownService.completedSprint(Sprint.fromJSON(message));
	}

}
