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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "PROJECT_BURNDOWN")
public class ProjectBurndown {

	@Id
	@Column(name = "PROJECT_BURNDOWN_ID")
	private Integer id;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@JsonIgnore
	@Column(name = "INITIAL_POINTS_TOTAL", nullable = false)
	private Integer initialPointsTotal = 0;

	@Transient
	private Integer noSprints;

	@Transient
	private Integer sprintLengthDays;

	@JsonIgnore
	@OneToMany(mappedBy = "sprintProject", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ProjectBurndownSprint> sprints = new HashSet<ProjectBurndownSprint>();

	@JsonIgnore
	@OneToMany(mappedBy = "storyProject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Story> stories = new HashSet<Story>();

	@JsonIgnore
	public ProjectBurndown getBurndown() {
		sprints.add(new ProjectBurndownSprint().setNumber(0).setPointsTotal(this.initialPointsTotal));
		return this;
	}

	public ProjectBurndown update(Story story) {
		addStory(story);
		if (!hasCompletedSprints()) {
			initialPointsTotal = totalStoryPoints();
		} else {
			ProjectBurndownSprint active = getActiveSprint();
			active.setPointsTotal(totalRemainingStoryPoints());
		}
		return this;
	}

	private boolean hasCompletedSprints() {
		for (ProjectBurndownSprint sprint : sprints) {
			if (sprint.getStatus().equals("Completed")) {
				return true;
			}
		}
		return false;
	}

	private void addStory(Story story) {
		if (stories.contains(story)) {
			getStory(story.getId()).setStatus(story.getStatus());
		} else {
			story.setStoryProject(this);
			stories.add(story);
		}
	}

	private Story getStory(Integer storyId) {
		for (Story story : stories) {
			if (story.getId().equals(storyId)) {
				return story;
			}
		}
		throw new RuntimeException("cannot find story with id " + storyId);
	}

	public ProjectBurndown update(List<ProjectBurndownSprint> sprints) {
		for (ProjectBurndownSprint sprint : sprints) {
			update(sprint);
		}
		return this;
	}

	public ProjectBurndown update(ProjectBurndownSprint sprint) {
		if (sprints.contains(sprint)) {
			ProjectBurndownSprint existing = getSprint(sprint.getNumber());
			existing.setStatus(sprint.getStatus());
			if (existing.getStatus().equals("Completed")) {
				existing.setPointsTotal(totalRemainingStoryPoints());
			}
		} else {
			sprint.setSprintProject(this);
			sprints.add(sprint);
		}
		return this;
	}

	private int totalStoryPoints() {
		int total = 0;
		for (Story story : stories) {
			total += story.getStorypoints();
		}
		return total;
	}

	private int totalRemainingStoryPoints() {
		int total = 0;
		for (Story story : stories) {
			if (!story.getStatus().equals("Completed")) {
				total += story.getStorypoints();
			}
		}
		return total;
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Integer getNoSprints() {
		return noSprints;
	}

	public Integer getSprintLengthDays() {
		return sprintLengthDays;
	}

	private ProjectBurndownSprint getSprint(Integer sprintNumber) {
		for (ProjectBurndownSprint sprint : sprints) {
			if (sprint.getNumber().equals(sprintNumber)) {
				return sprint;
			}
		}
		throw new RuntimeException("Project burndown does not contain sprint with number " + sprintNumber);
	}

	private ProjectBurndownSprint getActiveSprint() {
		for (ProjectBurndownSprint sprint : sprints) {
			if (sprint.getStatus().equals("Active")) {
				return sprint;
			}
		}
		throw new RuntimeException("Cannot find active sprint");
	}

	@JsonGetter
	public List<ProjectBurndownSprint> getSprints() {
		List<ProjectBurndownSprint> listOfSprints = new ArrayList<ProjectBurndownSprint>(sprints);
		Collections.sort(listOfSprints, new Comparator<ProjectBurndownSprint>() {
			public int compare(ProjectBurndownSprint o1, ProjectBurndownSprint o2) {
				return o1.getNumber() - o2.getNumber();
			}
		});
		for (int i = 0; i < listOfSprints.size(); i++) {
			float coefficient = (float) i / (listOfSprints.size() - 1);
			float ideal = initialPointsTotal - (coefficient * initialPointsTotal);
			listOfSprints.get(i).setIdealPointsTotal(ideal);
		}
		return listOfSprints;
	}

	public static ProjectBurndown fromJSON(String message) {
		try {
			return new ObjectMapper().readValue(message, ProjectBurndown.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
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
