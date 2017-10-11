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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "SPRINT")
public class Sprint {

	@Id
	@Column(name = "SPRINT_ID")
	private Integer id;

	@Column(name = "SPRINT_NUMBER", nullable = false)
	private Integer number;

	@Column(name = "POINTS_TOTAL", nullable = false)
	private Integer pointsTotal = 0;

	@JsonInclude(Include.NON_NULL)
	private String status;

	@Transient
	private Integer projectId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "PROJECT_ID", referencedColumnName = "PROJECT_ID")
	private Project sprintProject;

	public Integer getId() {
		return id;
	}

	public Integer getNumber() {
		return number;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Project getSprintProject() {
		return sprintProject;
	}

	public Integer getPointsTotal() {
		return pointsTotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Sprint setNumber(Integer number) {
		this.number = number;
		return this;
	}

	public Sprint setPointsTotal(Integer pointsTotal) {
		this.pointsTotal = pointsTotal;
		return this;
	}

	public Sprint incrementPoints(int points) {
		pointsTotal += points;
		return this;
	}

	void setSprintProject(Project sprintProject) {
		this.sprintProject = sprintProject;
	}
	
	public static Sprint fromJSON(String message) {
		try {
			return new ObjectMapper().readValue(message, Sprint.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
