package com.estafet.microservices.api.project.burndown.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;

@Repository
public class ProjectBurndownDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public ProjectBurndown getProjectBurndown(Integer projectId) {
		return entityManager.find(ProjectBurndown.class, projectId);
	}
		
	public void create(ProjectBurndown projectBurndown) {
		entityManager.persist(projectBurndown);
	}
	
	public void update(ProjectBurndown projectBurndown) {
		entityManager.merge(projectBurndown);
	}

	public int deleteAll() {
		return entityManager.createQuery("DELETE FROM ProjectBurndown").executeUpdate();
	}
	
}
