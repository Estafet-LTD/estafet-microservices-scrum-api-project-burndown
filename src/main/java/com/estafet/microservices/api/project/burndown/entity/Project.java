package com.estafet.microservices.api.project.burndown.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.estafet.microservices.api.project.burndown.message.Story;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "PROJECT")
public class Project {

	@Id
	@Column(name = "PROJECT_ID")
	private Integer id;
	
	@JsonIgnore
	@Column(name = "INITIAL_POINTS_TOTAL", nullable = false)
	private Integer initialPointsTotal = 0;

	@OneToMany(mappedBy = "sprintProject", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Sprint> sprints = new ArrayList<Sprint>();
	
	@JsonIgnore
	public Project getBurndown() {
		sprints.add(0, new Sprint().setNumber(0).setPointsTotal(this.initialPointsTotal));
		return this;
	}

	public Project addStory(Story story) {
		if (!sprints.isEmpty()) {
			Sprint sprint = getLastestSprint();
			addSprint(sprint.incrementPoints(story.getStorypoints()));	
		} else {
			initialPointsTotal += story.getStorypoints();
		}
		return this;
	}

	public Project addSprint(Sprint sprint) {
		sprint.setSprintProject(this);
		sprints.add(sprint);
		return this;
	}

	public Integer getId() {
		return id;
	}

	private Sprint getLastestSprint() {
		List<Sprint> listOfSprints = new ArrayList<Sprint>(sprints);
		Collections.sort(listOfSprints, new Comparator<Sprint>() {
			public int compare(Sprint o1, Sprint o2) {
				return o2.getNumber() - o1.getNumber();
			}
		});
		return getSprints().get(0);
	}

	public List<Sprint> getSprints() {
		return sprints;
	}

}
