package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.model.Project;
import com.estafet.microservices.api.project.burndown.model.Sprint;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@Component
public class CompletedSprintConsumer {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Autowired
	private ProjectBurndownService projectBurndownService;

	@Transactional
	@JmsListener(destination = "update.sprint.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		Sprint sprint = Sprint.fromJSON(message);
		if (sprint.getStatus().equals("Completed")) {
			Project project = projectBurndownService.getSprintProject(sprint.getId());
			sprint.setPointsTotal(projectBurndownService.getStoryPointsTotal(project.getId()));
			project.addSprint(sprint);
			projectBurndownDAO.update(project);
		}
	}

}
