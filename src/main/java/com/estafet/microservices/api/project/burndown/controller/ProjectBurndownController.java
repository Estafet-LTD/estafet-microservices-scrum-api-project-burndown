package com.estafet.microservices.api.project.burndown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.entity.Project;

@RestController
public class ProjectBurndownController {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;
	
	@Transactional(readOnly = true)
	@GetMapping("/project/{id}/burndown")
	public Project getProjectBurndown(@PathVariable int id) {
		return projectBurndownDAO.getProjectBurndown(id);
	}
	
}
