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

	NewProjectTopic projectTopic;
	NewSprintTopic sprintTopic;

	@Before
	public void before() throws Exception {
		RestAssured.baseURI = System.getenv("PROJECT_BURNDOWN_SERVICE_URI");
		projectTopic = new NewProjectTopic();
		sprintTopic = new NewSprintTopic();
	}

	@After
	public void after() throws Exception {
		projectTopic.closeConnection();
		sprintTopic.closeConnection();
	}

	@Test
	public void testGetAPI() {
		get("/api").then().body("id", equalTo(1)).body("title", equalTo("my project"));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-data.xml")
	public void testGetProjectBurndown() {
		get("/project/1/burndown").then()
				.body("id", is(1))
				.body("title", is("My Project #6889"))
				.body("sprints.id", hasItems(null, 1000, 1001))
				.body("sprints.pointsTotal", hasItems(235, 54, 38))
				.body("sprints.idealPointsTotal", hasItems(235.0f, 117.5f, 0.0f));
	}

	@Test
	@DatabaseSetup("ITProjectBurndownTest-empty.xml")
	public void testNewProject() throws Exception {
		projectTopic.send("{\"title\":\"My Project #1\",\"noSprints\":5,\"sprintLengthDays\":5}");
		sprintTopic.send("{ \\\"id\\\": 1, \\\"startDate\\\": \\\"2017-10-01 00:00:00\\\", \\\"endDate\\\": \\\"2017-10-06 00:00:00\\\", \\\"number\\\": 1, \\\"status\\\": \\\"Completed\\\",  \\\"projectId\\\": 1,  \\\"noDays\\\": 5 }");
		wait(2000);
		get("/project/1/burndown").then()
			.body("id", is(1))
			.body("title", is("My Project #1"));
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

	abstract class TopicProducer {

		Connection connection;
		MessageProducer messageProducer;
		Session session;

		public TopicProducer(String topicName) throws JMSException {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getenv("JBOSS_A_MQ_BROKER_URL"));
			connection = connectionFactory.createConnection(System.getenv("JBOSS_A_MQ_BROKER_USER"),
					System.getenv("JBOSS_A_MQ_BROKER_PASSWORD"));
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicName);
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

	class NewProjectTopic extends TopicProducer {
		public NewProjectTopic() throws JMSException {
			super("new.project.topic");
		}
	}
	
	class NewSprintTopic extends TopicProducer {
		public NewSprintTopic() throws JMSException {
			super("new.sprint.topic");
		}
	}

}
