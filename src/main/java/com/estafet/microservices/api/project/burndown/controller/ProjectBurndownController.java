package com.estafet.microservices.api.project.burndown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.project.burndown.messages.Deleted;
import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

import io.opentracing.Tracer;

@RestController
public class ProjectBurndownController {

	@Value("${app.version}")
	private String appVersion;
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private ProjectBurndownService projectBurndownService;
	
	@GetMapping("/api")
	public ProjectBurndown getAPI() {
		tracer.activeSpan().deactivate();
		return ProjectBurndown.getAPI(appVersion);
	}
	
	@GetMapping("/project/{id}/burndown")
	public ProjectBurndown getProjectBurndown(@PathVariable int id) {
		return projectBurndownService.getProjectBurndown(id);
	}
	
	@DeleteMapping("/project/burndowns")
	public Deleted deleteAll() {
		return projectBurndownService.deleteAll();
	}
	
}
