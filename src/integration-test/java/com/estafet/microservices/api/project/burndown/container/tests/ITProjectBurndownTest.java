package com.estafet.microservices.api.project.burndown.container.tests;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

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

	@Ignore
	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testGetProjectBurndown() {
		fail("Not yet implemented");
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
