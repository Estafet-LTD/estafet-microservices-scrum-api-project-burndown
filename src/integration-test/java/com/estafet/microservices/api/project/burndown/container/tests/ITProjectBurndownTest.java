package com.estafet.microservices.api.project.burndown.container.tests;

import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ITProjectBurndownTest {

	@Before
	public void before() throws Exception {
		RestAssured.baseURI = System.getenv("PROJECT_BURNDOWN_SERVICE_URI");
	}

	@Test
	public void testGetAPI() {
		get("/api").then()
			.body("id", equalTo(1))
			.body("title", equalTo("my project"));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testGetProjectBurndown() {
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #6889"))
			.body("sprints.number", hasItems(0, 1, 2))
			.body("sprints.pointsTotal", hasItems(235, 54, 38))
			.body("sprints.idealPointsTotal", hasItems(235.0f, 117.5f, 0.0f));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-empty.xml")
	public void testNewProject() throws Exception {
		NewProjectTopic.sendMessage("{ \"id\": 1, \"title\":\"My Project #1\",\"noSprints\":5,\"sprintLengthDays\":5 }");
		NewSprintTopic.sendMessage("{ \"id\": 1, \"startDate\": \"2017-10-01 00:00:00\", \"endDate\": \"2017-10-06 00:00:00\", \"number\": 1, \"status\": \"Active\",  \"projectId\": 1,  \"noDays\": 5 }");
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #1"))
			.body("sprints.number", hasItems(0, 1, 2, 3, 4, 5))
			.body("sprints.pointsTotal", hasItems(0, 0, 0, 0, 0, 0))
			.body("sprints.idealPointsTotal", hasItems(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
			.body("sprints.status", hasItems(null, "Active", "Not Started", "Not Started", "Not Started", "Not Started"));
		
	}

	@Ignore
	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testNewStoryConsumer() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testUpdatedSprintConsumer() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testUpdatedStoryConsumer() {
		fail("Not yet implemented");
	}

}
