package com.estafet.microservices.api.project.burndown.jms;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.entity.Project;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "newProjectConsumer")
public class NewProjectConsumer {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;
	
	@Transactional
	public void onMessage(String message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Project project  = mapper.readValue(message, Project.class);
			if (projectBurndownDAO.getProjectBurndown(project.getId()) == null) {
				projectBurndownDAO.create(project);
			}
		} catch (IOException  e) {
			throw new RuntimeException(e);
		} 
	}
	
}
