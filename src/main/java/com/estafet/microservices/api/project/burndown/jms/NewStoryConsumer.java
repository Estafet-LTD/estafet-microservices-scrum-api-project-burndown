package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.model.Project;
import com.estafet.microservices.api.project.burndown.model.Story;

@Component
public class NewStoryConsumer {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Transactional
	@JmsListener(destination = "new.story.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		Story story = Story.fromJSON(message);
		Project project = projectBurndownDAO.getProjectBurndown(story.getProjectId());
		projectBurndownDAO.update(project.addStory(story));
	}

}
