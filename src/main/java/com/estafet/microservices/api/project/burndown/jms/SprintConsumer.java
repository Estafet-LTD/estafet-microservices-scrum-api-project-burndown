package com.estafet.microservices.api.project.burndown.jms;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.entity.Project;
import com.estafet.microservices.api.project.burndown.entity.Sprint;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "sprintConsumer")
public class SprintConsumer {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Autowired
	private ProjectBurndownService projectBurndownService;

	public void onMessage(String message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Sprint sprint = mapper.readValue(message, Sprint.class);

			Project project = projectBurndownService.getSprintProject(sprint.getId());
			sprint.setPointsTotal(projectBurndownService.getStoryPointsTotal(project.getId()));
			project.addSprint(sprint);

			projectBurndownDAO.updateProjectBurndown(project);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
