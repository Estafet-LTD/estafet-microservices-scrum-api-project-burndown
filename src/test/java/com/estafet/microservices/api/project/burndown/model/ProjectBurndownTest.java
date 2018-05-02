package com.estafet.microservices.api.project.burndown.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ProjectBurndownTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetBurndown() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateStory() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateListOfProjectBurndownSprint() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateProjectBurndownSprint() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTitle() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNoSprints() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSprintLengthDays() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSprints() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromJSON() {
		ProjectBurndown burndown = ProjectBurndown.fromJSON("{\"id\":1,\"title\":null,\"noSprints\":null,\"sprintLengthDays\":null,\"sprints\":[{\"id\":1,\"number\":1,\"pointsTotal\":20,\"status\":\"Not Started\",\"idealPointsTotal\":\"NaN\",\"projectId\":1,\"startDate\":null,\"endDate\":null}]}");
		assertThat(burndown.getId(), is(1));
		assertNull(burndown.getTitle());
	}

	@Test
	public void testToJSON() {
		assertThat(ProjectBurndown.getAPI().toJSON(), is("{\"id\":1,\"title\":null,\"noSprints\":null,\"sprintLengthDays\":null,\"sprints\":[{\"id\":1,\"number\":1,\"pointsTotal\":20,\"status\":\"Not Started\",\"idealPointsTotal\":\"NaN\",\"projectId\":1,\"startDate\":null,\"endDate\":null}]}"));
	}

	@Test
	public void testGetAPI() {
		fail("Not yet implemented");
	}

}
