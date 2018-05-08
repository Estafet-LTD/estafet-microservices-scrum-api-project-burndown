package com.estafet.microservices.api.project.burndown.container.tests;

import static org.junit.Assert.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ITProjectBurndownTest {

	NewProjectTopic topic;
	
	@Before
	public void before() throws Exception {
		RestAssured.baseURI = System.getenv("PROJECT_BURNDOWN_SERVICE_URI");
		topic = new NewProjectTopic();
	}
	
	@After
	public void after() throws Exception {
		topic.closeConnection();
	}

	@Test
	public void testGetAPI() {
		get("/api").then().body("id", equalTo(1)).body("title", equalTo("my project"));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testGetProjectBurndown() {
		get("/project/1/burndown").then().body("id", is(1)).body("title", is("My Project #6889"))
				.body("sprints.id", hasItems(null, 1000, 1001)).body("sprints.pointsTotal", hasItems(235, 54, 38))
				.body("sprints.idealPointsTotal", hasItems(235.0d, 117.5d, 0.0d));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-empty.xml")
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

	public class NewProjectTopic {

		Connection connection;
		MessageProducer messageProducer;
		Session session;

		public NewProjectTopic() throws JMSException {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getenv("JBOSS_A_MQ_BROKER_URL"));
			connection = connectionFactory.createConnection(System.getenv("JBOSS_A_MQ_BROKER_USER"),
					System.getenv("JBOSS_A_MQ_BROKER_PASSWORD"));
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic("new.project.topic");
			messageProducer = session.createProducer(topic);
		}

		public void closeConnection() throws JMSException {
			connection.close();
		}

		public void send(String message) throws JMSException {
			TextMessage textMessage = session.createTextMessage(message);
			messageProducer.send(textMessage);
		}
	}

}
