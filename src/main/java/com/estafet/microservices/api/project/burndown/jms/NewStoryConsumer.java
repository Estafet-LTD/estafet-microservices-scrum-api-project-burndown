package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.model.Story;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@Component
public class NewStoryConsumer {

	@Autowired
	private ProjectBurndownService projectBurndownService;

	@JmsListener(destination = "new.story.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		projectBurndownService.updateBurndown(Story.fromJSON(message));
	}

}
