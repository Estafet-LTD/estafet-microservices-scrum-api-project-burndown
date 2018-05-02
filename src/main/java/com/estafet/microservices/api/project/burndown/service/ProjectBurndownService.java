package com.estafet.microservices.api.project.burndown.service;

import java.util.List;

import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndownSprint;
import com.estafet.microservices.api.project.burndown.model.Story;

/**
 * 
 * @author Dennis
 *
 */
public interface ProjectBurndownService {

	/**
	 * 
	 * @param id
	 * @return
	 */
	ProjectBurndown getProjectBurndown(int id);

	/**
	 * 
	 * @param projectBurndown
	 */
	void newProject(ProjectBurndown projectBurndown);

	/**
	 * 
	 * @param story
	 */
	void updateBurndown(Story story);

	/**
	 * 
	 * @param sprint
	 */
	void updateBurndown(ProjectBurndownSprint sprint);

	/**
	 * 
	 * @param sprintId
	 * @return
	 */
	ProjectBurndown getSprintProject(int sprintId);

	/**
	 * 
	 * @param sprintId
	 * @return
	 */
	ProjectBurndownSprint getSprint(int sprintId);

	/**
	 * 
	 * @param burndown
	 * @return
	 */
	List<ProjectBurndownSprint> calculateSprints(ProjectBurndown burndown);

}