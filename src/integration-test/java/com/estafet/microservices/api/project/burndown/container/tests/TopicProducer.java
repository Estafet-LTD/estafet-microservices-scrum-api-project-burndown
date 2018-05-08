package com.estafet.microservices.api.project.burndown.container.tests;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class TopicProducer {

	Connection connection;
	MessageProducer messageProducer;
	Session session;

	public TopicProducer(String topicName) {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getenv("JBOSS_A_MQ_BROKER_URL"));
			connection = connectionFactory.createConnection(System.getenv("JBOSS_A_MQ_BROKER_USER"),
					System.getenv("JBOSS_A_MQ_BROKER_PASSWORD"));
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