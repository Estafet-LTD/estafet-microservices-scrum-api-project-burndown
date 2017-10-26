package com.estafet.microservices.api.project.burndown.model;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "PROJECT_BURNDOWN_STORY")
public class Story {

	@Id
	@Column(name = "PROJECT_BURNDOWN_STORY_ID")
	private int id;

	@Column(name = "STORY_POINTS", nullable = false)
	private Integer storypoints;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "PROJECT_BURNDOWN_ID", referencedColumnName = "PROJECT_BURNDOWN_ID")
	private ProjectBurndown storyProject;

	@Transient
	private Integer sprintId;

	@Transient
	private Integer projectId;

	@Column(name = "STATUS", nullable = false)
	private String status;

	void setStoryProject(ProjectBurndown storyProject) {
		this.storyProject = storyProject;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Story other = (Story) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
