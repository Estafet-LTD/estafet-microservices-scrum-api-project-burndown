package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.model.Story;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

@Component
public class NewStoryConsumer {

	@Autowired
	private Tracer tracer;
	
	@Autowired
	private ProjectBurndownService projectBurndownService;

	@JmsListener(destination = "new.story.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		ActiveSpan span = tracer.activeSpan().log(message);
		try {
			projectBurndownService.updateBurndown(Story.fromJSON(message));
		} finally {
			span.close();
		}	
	}

}
