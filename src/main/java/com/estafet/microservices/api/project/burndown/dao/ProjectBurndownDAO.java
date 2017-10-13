package com.estafet.microservices.api.project.burndown.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.estafet.microservices.api.project.burndown.model.Project;

@Repository
public class ProjectBurndownDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Project getProjectBurndown(int projectId) {
		return entityManager.find(Project.class, new Integer(projectId));
	}
	
	public void create(Project project) {
		entityManager.persist(project);
	}
	
	public void update(Project project) {
		entityManager.merge(project);
	}

}
