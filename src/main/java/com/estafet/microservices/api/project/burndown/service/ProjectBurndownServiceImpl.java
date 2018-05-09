package com.estafet.microservices.api.project.burndown.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.messages.CalculateSprints;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndownSprint;
import com.estafet.microservices.api.project.burndown.model.Story;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProjectBurndownServiceImpl implements ProjectBurndownService {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Autowired
	private RestTemplate restTemplate;

	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.burndown.service.ProjectBurndownService#getProjectBurndown(int)
	 */
	@Override
	@Transactional(readOnly = true)
	public ProjectBurndown getProjectBurndown(int id) {
		return projectBurndownDAO.getProjectBurndown(id).getBurndown();
	}

	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.burndown.service.ProjectBurndownService#newProject(com.estafet.microservices.api.project.burndown.model.ProjectBurndown)
	 */
	@Override
	@Transactional
	public void newProject(ProjectBurndown projectBurndown) {
		projectBurndown.update(calculateSprints(projectBurndown));
		projectBurndownDAO.create(projectBurndown);
	}

	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.burndown.service.ProjectBurndownService#updateBurndown(com.estafet.microservices.api.project.burndown.model.Story)
	 */
	@Override
	@Transactional
	public void updateBurndown(Story story) {
		ProjectBurndown projectBurndown = projectBurndownDAO.getProjectBurndown(story.getProjectId());
		projectBurndownDAO.update(projectBurndown.update(story));
	}

	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.burndown.service.ProjectBurndownService#updateBurndown(com.estafet.microservices.api.project.burndown.model.ProjectBurndownSprint)
	 */
	@Override
	@Transactional
	public void updateBurndown(ProjectBurndownSprint sprint) {
		System.out.println("sprint #1 " + sprint);
		ProjectBurndown projectBurndown = projectBurndownDAO.getProjectBurndown(sprint.getProjectId());
		System.out.println("sprint #2 " + sprint);
		System.out.println("projectBurndown " + projectBurndown);
		projectBurndownDAO.update(projectBurndown.update(sprint));
	}

	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.burndown.service.ProjectBurndownService#calculateSprints(com.estafet.microservices.api.project.burndown.model.ProjectBurndown)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List<ProjectBurndownSprint> calculateSprints(ProjectBurndown burndown) {
		CalculateSprints message = new CalculateSprints(burndown.getId(), burndown.getSprintLengthDays(),
				burndown.getNoSprints());
		List objects = restTemplate.postForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/calculate-sprints",
				message, List.class);
		List<ProjectBurndownSprint> sprints = new ArrayList<ProjectBurndownSprint>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			ProjectBurndownSprint sprint = mapper.convertValue(object, new TypeReference<ProjectBurndownSprint>() {
			});
			sprints.add(sprint);
		}
		return sprints;
	}

}
