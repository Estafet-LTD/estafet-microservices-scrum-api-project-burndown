package com.estafet.microservices.api.project.burndown.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "PROJECT_BURN_SPRINT")
public class Sprint {

	@Id
	@Column(name = "SPRINT_ID")
	private Integer id;

	@Column(name = "SPRINT_NUMBER")
	private Integer number;

	@Column(name = "POINTS_TOTAL")
	private Integer pointsTotal = 0;

	@JsonInclude(Include.NON_NULL)
	private String status;

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

	public Sprint setId(Integer id) {
		this.id = id;
		return this;
	}

	public Sprint setNumber(Integer number) {
		this.number = number;
		return this;
	}

	public void setPointsTotal(Integer pointsTotal) {
		this.pointsTotal = pointsTotal;
	}

	public Sprint incrementPoints(int points) {
		pointsTotal += points;
		return this;
	}

	void setSprintProject(Project sprintProject) {
		this.sprintProject = sprintProject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Sprint other = (Sprint) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
