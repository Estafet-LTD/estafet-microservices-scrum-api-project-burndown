package com.estafet.microservices.api.project.burndown.container.tests;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;

public abstract class TopicProducer {

	ActiveMQConnection connection;
	MessageProducer messageProducer;
	Session session;

	public TopicProducer(String topicName) {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getenv("JBOSS_A_MQ_BROKER_URL"));
			connection = (ActiveMQConnection) connectionFactory.createConnection(
					System.getenv("JBOSS_A_MQ_BROKER_USER"), System.getenv("JBOSS_A_MQ_BROKER_PASSWORD"));

			ActiveMQDestination destination = ActiveMQDestination.createDestination(topicName,
					ActiveMQDestination.TOPIC_TYPE);
			connection.destroyDestination(destination);

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicName);
			messageProducer = session.createProducer(topic);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void send(String message) {
		try {
			TextMessage textMessage = session.createTextMessage(message);
			messageProducer.send(textMessage);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

}