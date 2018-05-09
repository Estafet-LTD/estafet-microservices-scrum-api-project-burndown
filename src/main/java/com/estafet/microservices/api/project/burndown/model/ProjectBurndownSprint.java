package com.estafet.microservices.api.project.burndown.model;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "PROJECT_BURNDOWN_SPRINT")
public class ProjectBurndownSprint {

	@JsonIgnore
	@Id
	@SequenceGenerator(name = "PROJECT_BURNDOWN_SPRINT_ID_SEQ", sequenceName = "PROJECT_BURNDOWN_SPRINT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_BURNDOWN_SPRINT_ID_SEQ")
	@Column(name = "PROJECT_BURNDOWN_SPRINT_ID")
	private Integer id;

	@Column(name = "SPRINT_NUMBER", nullable = false)
	private Integer number;

	@Column(name = "POINTS_TOTAL")
	private Integer pointsTotal;

	@Column(name = "STATUS", nullable = false)
	private String status = "Not Started";
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "PROJECT_BURNDOWN_ID", referencedColumnName = "PROJECT_BURNDOWN_ID")
	private ProjectBurndown sprintProject;

	@Transient
	private float idealPointsTotal;

	@Transient
	private Integer projectId;

	@Column(name = "START_DATE")
	private String startDate;

	@Column(name = "END_DATE")
	private String endDate;

	public Integer getId() {
		return id;
	}

	public Integer getNumber() {
		return number;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public ProjectBurndown getSprintProject() {
		return sprintProject;
	}

	public Integer getPointsTotal() {
		return pointsTotal;
	}

	public float getIdealPointsTotal() {
		return idealPointsTotal;
	}

	public String getStatus() {
		return status;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ProjectBurndownSprint setNumber(Integer number) {
		this.number = number;
		return this;
	}

	public ProjectBurndownSprint setPointsTotal(Integer pointsTotal) {
		this.pointsTotal = pointsTotal;
		return this;
	}

	void setIdealPointsTotal(float idealPointsTotal) {
		this.idealPointsTotal = idealPointsTotal;
	}

	void setSprintProject(ProjectBurndown sprintProject) {
		this.sprintProject = sprintProject;
	}

	public static ProjectBurndownSprint fromJSON(String message) {
		try {
			return new ObjectMapper().readValue(message, ProjectBurndownSprint.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static ProjectBurndownSprint getAPI() {
		ProjectBurndownSprint projectBurndownSprint = new ProjectBurndownSprint();
		projectBurndownSprint.id = 1;
		projectBurndownSprint.number = 1;
		projectBurndownSprint.pointsTotal = 20;
		projectBurndownSprint.projectId = 1;
		projectBurndownSprint.status = "Not Started";
		return projectBurndownSprint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
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
		ProjectBurndownSprint other = (ProjectBurndownSprint) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (projectId == null) {
			if (other.projectId != null)
				return false;
		} else if (!projectId.equals(other.projectId))
			return false;
		return true;
	}

}
