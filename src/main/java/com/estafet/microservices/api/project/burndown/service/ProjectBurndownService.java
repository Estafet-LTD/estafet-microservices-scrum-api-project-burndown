package com.estafet.microservices.api.project.burndown.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.entity.Project;
import com.estafet.microservices.api.project.burndown.entity.Sprint;
import com.estafet.microservices.api.project.burndown.message.Story;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProjectBurndownService {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;
	
	@Transactional(readOnly = true)
	public Project getProjectBurndown(int id) {
		return projectBurndownDAO.getProjectBurndown(id).getBurndown();
	}
		
	public Project getSprintProject(int sprintId) {
		Sprint sprint = getSprint(sprintId);
		return projectBurndownDAO.getProjectBurndown(sprint.getProjectId());
	}
	
	public Project getProject(int projectId) {
		return new RestTemplate().getForObject(System.getenv("PROJECT_API_SERVICE_URI") + "/project/{id}",
				Project.class, projectId);
	}
	
	public Sprint getSprint(int sprintId) {
		return new RestTemplate().getForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{id}",
				Sprint.class, sprintId);
	}

	@SuppressWarnings("rawtypes")
	public int getStoryPointsTotal(int projectId) {
		List objects = new RestTemplate().getForObject(System.getenv("STORY_API_SERVICE_URI") + "/project/{id}/stories",
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
