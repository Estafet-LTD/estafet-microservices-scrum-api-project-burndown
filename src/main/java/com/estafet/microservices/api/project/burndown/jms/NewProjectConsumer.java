package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.project.burndown.model.Project;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@Component
public class NewProjectConsumer {

	@Autowired
	private ProjectBurndownService projectBurndownService;
	
	@Autowired
	private RestTemplate restTemplate;

	@JmsListener(destination = "new.project.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		restTemplate.getForObject(System.getenv("PROJECT_API_SERVICE_URI") + "/project/{id}", Project.class, Project.fromJSON(message).getId());
		projectBurndownService.newProject(Project.fromJSON(message));
	}

}
