package com.estafet.microservices.api.project.burndown.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {

	private int id;

	private Integer storypoints;

	private Integer sprintId;

	private Integer projectId;

	private String status;

	public String getStatus() {
		return status;
	}

	public Integer getStorypoints() {
		return storypoints;
	}

	public int getId() {
		return id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public Story setProjectId(Integer projectId) {
		this.projectId = projectId;
		return this;
	}

	public Story setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
		return this;
	}
	
	public static Story fromJSON(String message) {
		try {
			return new ObjectMapper().readValue(message, Story.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
