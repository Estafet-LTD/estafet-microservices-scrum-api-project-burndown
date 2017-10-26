package com.estafet.microservices.api.project.burndown.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "PROJECT_BURNDOWN")
public class ProjectBurndown {

	@Id
	@Column(name = "PROJECT_BURNDOWN_ID")
	private Integer id;

	@JsonIgnore
	@Column(name = "INITIAL_POINTS_TOTAL", nullable = false)
	private Integer initialPointsTotal = 0;

	@Transient
	private Integer noSprints;

	@Transient
	private Integer sprintLengthDays;

	@JsonIgnore
	@OneToMany(mappedBy = "sprintProject", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ProjectBurndownSprint> projectBurndownSprints = new HashSet<ProjectBurndownSprint>();

	@JsonIgnore
	public ProjectBurndown getBurndown() {
		projectBurndownSprints.add(new ProjectBurndownSprint().setNumber(0).setPointsTotal(this.initialPointsTotal));
		return this;
	}

	public ProjectBurndown update(Story story) {
		if (!projectBurndownSprints.isEmpty()) {
			ProjectBurndownSprint projectBurndownSprint = getLastestSprint();
			update(projectBurndownSprint.incrementPoints(story.getStorypoints()));
		} else {
			initialPointsTotal += story.getStorypoints();
		}
		return this;
	}

	public ProjectBurndown update(List<ProjectBurndownSprint> sprints) {
		for (ProjectBurndownSprint sprint : sprints) {
			update(sprint);
		}
		return this;
	}

	public ProjectBurndown update(ProjectBurndownSprint projectBurndownSprint) {
		if (projectBurndownSprint.getId() != null) {
			getSprint(projectBurndownSprint.getId()).setPointsTotal(projectBurndownSprint.getPointsTotal());
		} else {
			projectBurndownSprint.setSprintProject(this);
			projectBurndownSprints.add(projectBurndownSprint);
		}
		return this;
	}

	public Integer getId() {
		return id;
	}

	public Integer getNoSprints() {
		return noSprints;
	}

	public Integer getSprintLengthDays() {
		return sprintLengthDays;
	}

	private ProjectBurndownSprint getSprint(Integer sprintId) {
		for (ProjectBurndownSprint sprint : projectBurndownSprints) {
			if (sprint.getId() != null && sprint.getId().equals(sprintId)) {
				return sprint;
			}
		}
		throw new RuntimeException("Project burndown does not contain sprint with id " + sprintId);
	}

	private ProjectBurndownSprint getLastestSprint() {
		List<ProjectBurndownSprint> listOfSprints = new ArrayList<ProjectBurndownSprint>(projectBurndownSprints);
		Collections.sort(listOfSprints, new Comparator<ProjectBurndownSprint>() {
			public int compare(ProjectBurndownSprint o1, ProjectBurndownSprint o2) {
				return o2.getNumber() - o1.getNumber();
			}
		});
		return getSprints().get(0);
	}

	@JsonGetter
	public List<ProjectBurndownSprint> getSprints() {
		List<ProjectBurndownSprint> listOfSprints = new ArrayList<ProjectBurndownSprint>(projectBurndownSprints);
		Collections.sort(listOfSprints, new Comparator<ProjectBurndownSprint>() {
			public int compare(ProjectBurndownSprint o1, ProjectBurndownSprint o2) {
				return o1.getNumber() - o2.getNumber();
			}
		});
		return listOfSprints;
	}

	public static ProjectBurndown fromJSON(String message) {
		try {
			return new ObjectMapper().readValue(message, ProjectBurndown.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static ProjectBurndown getAPI() {
		ProjectBurndown projectBurndown = new ProjectBurndown();
		projectBurndown.id = 1;
		projectBurndown.initialPointsTotal = 20;
		projectBurndown.update(ProjectBurndownSprint.getAPI());
		return projectBurndown;
	}

}
