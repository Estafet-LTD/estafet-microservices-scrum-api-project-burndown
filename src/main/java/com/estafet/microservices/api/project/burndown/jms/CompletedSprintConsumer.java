package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.entity.Project;
import com.estafet.microservices.api.project.burndown.entity.Sprint;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@Component
public class CompletedSprintConsumer {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Autowired
	private ProjectBurndownService projectBurndownService;

	@Transactional
	@JmsListener(destination = "update.sprint.topic", containerFactory = "myFactory")
	public void onMessage(Sprint sprint) {
		if (sprint.getStatus().equals("Completed")) {
			Project project = projectBurndownService.getSprintProject(sprint.getId());
			sprint.setPointsTotal(projectBurndownService.getStoryPointsTotal(project.getId()));
			project.addSprint(sprint);
			projectBurndownDAO.update(project);
		}
	}

}
