package com.estafet.microservices.api.project.burndown.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.project.burndown.entity.Project;
import com.estafet.microservices.api.project.burndown.message.Story;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProjectBurndownService {

	public Project getSprintProject(int sprintId) {
		return new RestTemplate().getForObject(System.getenv("PROJECT_REPOSITORY_JDBC_URL") + "/sprint/{id}/project",
				Project.class, sprintId);
	}

	@SuppressWarnings("rawtypes")
	public int getStoryPointsTotal(int projectId) {
		List objects = new RestTemplate().getForObject(System.getenv("PROJECT_REPOSITORY_JDBC_URL") + "/{id}/stories",
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
