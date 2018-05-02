package com.estafet.microservices.api.project.burndown.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.estafet.microservices.api.project.burndown.model.ProjectBurndown;
import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@RunWith(MockitoJUnitRunner.class)
public class ProjectBurndownControllerTest {

	@InjectMocks
	ProjectBurndownController controller = new ProjectBurndownController();
	
	@Mock
	ProjectBurndownService projectBurndownService;
	
	@Before
	public void before() throws Exception {
	}

	@Test
	public void testGetAPI() {
		assertThat(controller.getAPI().getId(), is(1));
	}

	@Test
	public void testGetProjectBurndown() {
		when(projectBurndownService.getProjectBurndown(1)).thenReturn(ProjectBurndown.getAPI());
		assertEquals(controller.getProjectBurndown(1).getId().intValue(), 1);
	}

}
