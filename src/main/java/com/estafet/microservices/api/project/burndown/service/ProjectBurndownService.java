package com.estafet.microservices.api.project.burndown.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndownSprint;
import com.estafet.microservices.api.project.burndown.model.Story;
import com.estafet.microservices.scrum.lib.commons.date.CalculatedSprint;
import com.estafet.microservices.scrum.lib.commons.date.DateHelper;

@Component
public class ProjectBurndownService {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Transactional(readOnly = true)
	public ProjectBurndown getProjectBurndown(int id) {
		return projectBurndownDAO.getProjectBurndown(id).getBurndown();
	}

	@Transactional
	public void newProject(ProjectBurndown projectBurndown) {
		projectBurndown.update(calculateSprints(projectBurndown));
		projectBurndownDAO.create(projectBurndown);
	}

	@Transactional
	public void updateBurndown(Story story) {
		ProjectBurndown projectBurndown = projectBurndownDAO.getProjectBurndown(story.getProjectId());
		projectBurndownDAO.update(projectBurndown.update(story));
	}

	@Transactional
	public void updateBurndown(ProjectBurndownSprint sprint) {
		ProjectBurndown projectBurndown = projectBurndownDAO.getProjectBurndown(sprint.getProjectId());
		projectBurndownDAO.update(projectBurndown.update(sprint));
	}

	public List<ProjectBurndownSprint> calculateSprints(ProjectBurndown burndown) {
		List<CalculatedSprint> calculatedSprints = DateHelper.calculateSprints(burndown.getSprintLengthDays(),
				burndown.getNoSprints());
		List<ProjectBurndownSprint> sprints = new ArrayList<ProjectBurndownSprint>(calculatedSprints.size());
		for (CalculatedSprint calculatedSprint : calculatedSprints) {
			ProjectBurndownSprint sprint = new ProjectBurndownSprint();
			sprint.setNumber(calculatedSprint.getNumber());
			sprint.setProjectId(burndown.getId());
			sprint.setEndDate(calculatedSprint.getEndDate());
			sprint.setStartDate(calculatedSprint.getStartDate());
			sprints.add(sprint);
		}
		return sprints;
	}

}
