package com.estafet.microservices.api.project.burndown.message;

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

}
