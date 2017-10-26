package com.estafet.microservices.api.project.burndown.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.messages.CalculateSprints;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndownSprint;
import com.estafet.microservices.api.project.burndown.model.Story;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProjectBurndownService {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Autowired
	private RestTemplate restTemplate;

	@Transactional(readOnly = true)
	public ProjectBurndown getProjectBurndown(int id) {
		return projectBurndownDAO.getProjectBurndown(id).getBurndown();
	}

	@Transactional
	public void newProject(ProjectBurndown projectBurndown) {
		if (projectBurndownDAO.getProjectBurndown(projectBurndown.getId()) == null) {
			projectBurndown.update(calculateSprints(projectBurndown));
			projectBurndownDAO.create(projectBurndown);
		}
	}

	@Transactional
	public void updateBurndown(Story story) {
		ProjectBurndown projectBurndown = projectBurndownDAO.getProjectBurndown(story.getProjectId());
		projectBurndownDAO.update(projectBurndown.update(story));
	}

	@Transactional
	public void updateBurndown(ProjectBurndownSprint projectBurndownSprint) {
		ProjectBurndown projectBurndown = getSprintProject(projectBurndownSprint.getId());
		projectBurndown.update(projectBurndownSprint);
		projectBurndownDAO.update(projectBurndown);
	}

	@Transactional(readOnly = true)
	public ProjectBurndown getSprintProject(int sprintId) {
		ProjectBurndownSprint projectBurndownSprint = getSprint(sprintId);
		return projectBurndownDAO.getProjectBurndown(projectBurndownSprint.getProjectId());
	}

	public ProjectBurndown getProject(int projectId) {
		return restTemplate.getForObject(System.getenv("PROJECT_API_SERVICE_URI") + "/project/{id}",
				ProjectBurndown.class, projectId);
	}

	public ProjectBurndownSprint getSprint(int sprintId) {
		return restTemplate.getForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{id}",
				ProjectBurndownSprint.class, sprintId);
	}

	@SuppressWarnings("rawtypes")
	public List<ProjectBurndownSprint> calculateSprints(ProjectBurndown burndown) {
		CalculateSprints message = new CalculateSprints(burndown.getId(), burndown.getSprintLengthDays(),
				burndown.getNoSprints());
		List objects = restTemplate.postForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/calculate-sprints",
				message, List.class);
		List<ProjectBurndownSprint> sprints = new ArrayList<ProjectBurndownSprint>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			ProjectBurndownSprint sprint = mapper.convertValue(object, new TypeReference<ProjectBurndownSprint>() {
			});
			sprints.add(sprint);
		}
		return sprints;
	}

}
