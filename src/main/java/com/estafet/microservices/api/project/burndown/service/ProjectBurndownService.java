package com.estafet.microservices.api.project.burndown.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.model.Project;
import com.estafet.microservices.api.project.burndown.model.Sprint;
import com.estafet.microservices.api.project.burndown.model.Story;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

@Component
public class ProjectBurndownService {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Transactional(readOnly = true)
	public Project getProjectBurndown(int id) {
		return projectBurndownDAO.getProjectBurndown(id).getBurndown();
	}
	
	@Transactional
	public void newProject(Project project) {
		try (ActiveSpan activeSpan = tracer.buildSpan("newProject").startActive()) {
			if (projectBurndownDAO.getProjectBurndown(project.getId()) == null) {
				projectBurndownDAO.create(project);
			}	
		}
	}
	
	@Transactional
	public void newStory(Story story) {
		Project project = projectBurndownDAO.getProjectBurndown(story.getProjectId());
		projectBurndownDAO.update(project.addStory(story));
	}
	
	@Transactional
	public void completedSprint(Sprint sprint) {
		if (sprint.getStatus().equals("Completed")) {
			Project project = getSprintProject(sprint.getId());
			sprint.setPointsTotal(getStoryPointsTotal(project.getId()));
			project.addSprint(sprint);
			projectBurndownDAO.update(project);
		}
	}
	
	@Transactional(readOnly = true)
	public Project getSprintProject(int sprintId) {
		Sprint sprint = getSprint(sprintId);
		return projectBurndownDAO.getProjectBurndown(sprint.getProjectId());
	}
	
	public Project getProject(int projectId) {
		return restTemplate.getForObject(System.getenv("PROJECT_API_SERVICE_URI") + "/project/{id}",
				Project.class, projectId);
	}
	
	public Sprint getSprint(int sprintId) {
		return restTemplate.getForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{id}",
				Sprint.class, sprintId);
	}

	@SuppressWarnings("rawtypes")
	public int getStoryPointsTotal(int projectId) {
		List objects = restTemplate.getForObject(System.getenv("STORY_API_SERVICE_URI") + "/project/{id}/stories",
				List.class, projectId);
		int total = 0;
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Story story = mapper.convertValue(object, new TypeReference<Story>() {
			});
			if (!story.getStatus().equals("Completed")) {
				total += story.getStorypoints();
			}
		}
		return total;
	}

}
