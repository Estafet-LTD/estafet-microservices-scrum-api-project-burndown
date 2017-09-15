package com.estafet.microservices.api.project.burndown.entity;

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

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "PROJECT_BURN_PROJECT")
public class Project {

	@Id
	@Column(name = "PROJECT_ID")
	private int id;
	
	@Column(name = "STARTING_TOTAL_STORY_POINTS")
	private int startingTotalStoryPoints;

	@OneToMany(mappedBy = "sprintProject", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Sprint> sprints = new HashSet<Sprint>();
	
	public Project addStory(Story story) {
		if (sprints.isEmpty()) {
			startingTotalStoryPoints += story.getStorypoints();
		}
		return this;
	}
	
	public Project addSprint(Sprint sprint) {
		sprint.setSprintProject(this);
		sprints.add(sprint);
		return this;
	}

	public int getId() {
		return id;
	}

	public Project setId(int id) {
		this.id = id;
		return this;
	}

	@JsonProperty("sprints")
	public List<Sprint> getSprints() {
		List<Sprint> listOfSprints = new ArrayList<Sprint>(sprints);
		Collections.sort(listOfSprints, new Comparator<Sprint>() {
			public int compare(Sprint o1, Sprint o2) {
				return o1.getNumber() - o2.getNumber();
			}
		});
		return listOfSprints;
	}

	@JsonProperty("sprints")
	void setSprints(Set<Sprint> sprints) {
		for (Sprint sprint : sprints) {
			addSprint(sprint);
		}
	}

}
