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
		get("/api").then().body("id", equalTo(1)).body("title", equalTo("my project"));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testGetProjectBurndown() {
		get("/project/1/burndown").then().body("id", is(1))
			.body("title", is("My Project #6889"))
			.body("sprints.id", hasItems(null, 1000, 10001))
			.body("sprints.pointsTotal", hasItems(235, 54, 38))
			.body("sprints.idealPointsTotal", hasItems(235.0, 117.5, 0.0));
	}
	
	@Ignore
	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testNewProjectConsumer() {
		fail("Not yet implemented");
	}
	
	@Ignore
	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testNewSprintConsumer() {
		fail("Not yet implemented");
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
