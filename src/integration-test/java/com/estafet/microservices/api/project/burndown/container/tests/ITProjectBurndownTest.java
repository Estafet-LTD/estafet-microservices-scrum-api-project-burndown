package com.estafet.microservices.api.project.burndown.container.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
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
		get("/project/1000/burndown").then()
			.body("id", is(1000))
			.body("title", is("My Project #6889"))
			.body("sprints.number", hasItems(0, 1, 2))
			.body("sprints.pointsTotal", hasItems(235, 54, 38))
			.body("sprints.idealPointsTotal", hasItems(235.0f, 117.5f, 0.0f));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testNewProject() {
		NewProjectTopic.sendMessage("{ \"id\": 1, \"title\":\"My Project #1\",\"noSprints\":5,\"sprintLengthDays\":5 }");
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #1"))
			.body("sprints.number", hasItems(0, 1, 2, 3, 4, 5))
			.body("sprints.pointsTotal", hasItems(0, 0, 0, 0, 0, 0))
			.body("sprints.idealPointsTotal", hasItems(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
			.body("sprints.status", hasItems("Not Started", "Not Started", "Not Started", "Not Started", "Not Started", "Not Started"));		
	}
	
	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testNewProjectThenNewSprint() {
		NewProjectTopic.sendMessage("{ \"id\": 1, \"title\":\"My Project #1\",\"noSprints\":5,\"sprintLengthDays\":5 }");		
		NewSprintTopic.sendMessage("{ \"id\": 1, \"startDate\": \"2017-10-01 00:00:00\", \"endDate\": \"2017-10-06 00:00:00\", \"number\": 1, \"status\": \"Active\",  \"projectId\": 1,  \"noDays\": 5 }");
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #1"))
			.body("sprints.number", hasItems(0, 1, 2, 3, 4, 5))
			.body("sprints.pointsTotal", hasItems(0, 0, 0, 0, 0, 0))
			.body("sprints.idealPointsTotal", hasItems(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
			.body("sprints.status", hasItems("Not Started", "Active", "Not Started", "Not Started", "Not Started", "Not Started"));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testNewStoriesAddedToProject() {
		NewProjectTopic.sendMessage("{ \"id\": 1, \"title\":\"My Project #1\",\"noSprints\":5,\"sprintLengthDays\":5 }");		
		NewSprintTopic.sendMessage("{ \"id\": 1, \"startDate\": \"2017-10-01 00:00:00\", \"endDate\": \"2017-10-06 00:00:00\", \"number\": 1, \"status\": \"Active\",  \"projectId\": 1,  \"noDays\": 5 }");
		NewStoryTopic.sendMessage("{ \"id\": 1, \"title\": \"some test story\",  \"description\": \"hghghg\",  \"storypoints\": 5,  \"projectId\": 1,  \"status\": \"Not Started\" }");
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #1"))
			.body("sprints.number", hasItems(0, 1, 2, 3, 4, 5))
			.body("sprints.pointsTotal", hasItems(5, 0, 0, 0, 0, 0))
			.body("sprints.idealPointsTotal", hasItems(5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
			.body("sprints.status", hasItems("Not Started", "Active", "Not Started", "Not Started", "Not Started", "Not Started"));
		NewStoryTopic.sendMessage("{ \"id\": 2, \"title\": \"another test story\",  \"description\": \"jhjhkhk\",  \"storypoints\": 20,  \"projectId\": 1, \"status\": \"Not Started\" }");
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #1"))
			.body("sprints.number", hasItems(0, 1, 2, 3, 4, 5))
			.body("sprints.pointsTotal", hasItems(25, 0, 0, 0, 0, 0))
			.body("sprints.idealPointsTotal", hasItems(25.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
			.body("sprints.status", hasItems("Not Started", "Active", "Not Started", "Not Started", "Not Started", "Not Started"));		
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testStoriesMovedToSprint() {
		NewProjectTopic.sendMessage("{ \"id\": 1, \"title\":\"My Project #1\",\"noSprints\":5,\"sprintLengthDays\":5 }");		
		NewSprintTopic.sendMessage("{ \"id\": 1, \"startDate\": \"2017-10-01 00:00:00\", \"endDate\": \"2017-10-06 00:00:00\", \"number\": 1, \"status\": \"Active\",  \"projectId\": 1,  \"noDays\": 5 }");
		NewStoryTopic.sendMessage("{ \"id\": 1, \"title\": \"some test story\",  \"description\": \"hghghg\",  \"storypoints\": 5,  \"projectId\": 1,  \"status\": \"Not Started\" }");
		NewStoryTopic.sendMessage("{ \"id\": 2, \"title\": \"another test story\",  \"description\": \"jhjhkhk\",  \"storypoints\": 20,  \"projectId\": 1,  \"status\": \"Not Started\" }");
		UpdatedStoryTopic.sendMessage("{ \"id\": 1, \"title\": \"some test story\",  \"description\": \"hghghg\",  \"storypoints\": 5,  \"projectId\": 1, \"sprintId\": 1,  \"status\": \"Not Started\" }");
		UpdatedStoryTopic.sendMessage("{ \"id\": 2, \"title\": \"another test story\",  \"description\": \"jhjhkhk\",  \"storypoints\": 20,  \"projectId\": 1, \"sprintId\": 1, \"status\": \"Not Started\" }");
		NewStoryTopic.sendMessage("{ \"id\": 3, \"title\": \"yet another test story\",  \"description\": \"jjhhuyy\",  \"storypoints\": 8,  \"projectId\": 1, \"status\": \"Not Started\" }");
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #1"))
			.body("sprints.number", hasItems(0, 1, 2, 3, 4, 5))
			.body("sprints.pointsTotal", hasItems(33, 0, 0, 0, 0, 0))
			.body("sprints.idealPointsTotal", hasItems(33.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
			.body("sprints.status", hasItems("Not Started", "Active", "Not Started", "Not Started", "Not Started", "Not Started"));		
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testCompletedSprint() {
		NewProjectTopic.sendMessage("{ \"id\": 1, \"title\":\"My Project #1\",\"noSprints\":5,\"sprintLengthDays\":5 }");		
		NewSprintTopic.sendMessage("{ \"id\": 1, \"startDate\": \"2017-10-01 00:00:00\", \"endDate\": \"2017-10-06 00:00:00\", \"number\": 1, \"status\": \"Active\",  \"projectId\": 1,  \"noDays\": 5 }");
		NewStoryTopic.sendMessage("{ \"id\": 1, \"title\": \"some test story\",  \"description\": \"hghghg\",  \"storypoints\": 5,  \"projectId\": 1,  \"status\": \"Not Started\" }");
		NewStoryTopic.sendMessage("{ \"id\": 2, \"title\": \"another test story\",  \"description\": \"jhjhkhk\",  \"storypoints\": 20,  \"projectId\": 1,  \"status\": \"Not Started\" }");
		NewStoryTopic.sendMessage("{ \"id\": 1, \"title\": \"some test story\",  \"description\": \"hghghg\",  \"storypoints\": 5,  \"projectId\": 1, \"sprintId\": 1,  \"status\": \"Not Started\" }");
		NewStoryTopic.sendMessage("{ \"id\": 2, \"title\": \"another test story\",  \"description\": \"jhjhkhk\",  \"storypoints\": 20,  \"projectId\": 1, \"sprintId\": 1, \"status\": \"Not Started\" }");
		NewStoryTopic.sendMessage("{ \"id\": 3, \"title\": \"yet another test story\",  \"description\": \"jjhhuyy\",  \"storypoints\": 8,  \"projectId\": 1, \"status\": \"Not Started\" }");
		UpdatedStoryTopic.sendMessage("{ \"id\": 1, \"title\": \"some test story\",  \"description\": \"hghghg\",  \"storypoints\": 5,  \"projectId\": 1, \"sprintId\": 1,  \"status\": \"Completed\" }");
		UpdatedStoryTopic.sendMessage("{ \"id\": 2, \"title\": \"another test story\",  \"description\": \"jhjhkhk\",  \"storypoints\": 20,  \"projectId\": 1, \"sprintId\": 1, \"status\": \"Completed\" }");
		UpdatedSprintTopic.sendMessage("{ \"id\": 1, \"startDate\": \"2017-10-01 00:00:00\", \"endDate\": \"2017-10-06 00:00:00\", \"number\": 1, \"status\": \"Completed\",  \"projectId\": 1,  \"noDays\": 5 }");
		UpdatedSprintTopic.sendMessage("{ \"id\": 2, \"startDate\": \"2017-10-01 00:00:00\", \"endDate\": \"2017-10-06 00:00:00\", \"number\": 1, \"status\": \"Active\",  \"projectId\": 1,  \"noDays\": 5 }");
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #1"))
			.body("sprints.number", hasItems(0, 1, 2, 3, 4, 5))
			.body("sprints.pointsTotal", hasItems(33, 8, 0, 0, 0, 0))
			.body("sprints.idealPointsTotal", hasItems(33.0f, 26.4f, 13.199999f, 6.6000004f, 0.0f));
		
	}

}
