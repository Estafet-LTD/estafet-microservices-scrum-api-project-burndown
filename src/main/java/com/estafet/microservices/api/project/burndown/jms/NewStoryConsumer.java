package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.entity.Project;
import com.estafet.microservices.api.project.burndown.message.Story;

@Component
public class NewStoryConsumer {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Transactional
	@JmsListener(destination = "new.story.topic", containerFactory = "myFactory")
	public void onMessage(Story story) {
		Project project = projectBurndownDAO.getProjectBurndown(story.getProjectId());
		projectBurndownDAO.update(project.addStory(story));
	}

}
