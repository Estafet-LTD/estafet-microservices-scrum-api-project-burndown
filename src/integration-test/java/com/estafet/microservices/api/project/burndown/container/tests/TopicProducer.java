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
	String topicName;

	public TopicProducer(String topicName) {
		try {
			this.topicName = topicName;
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getenv("JBOSS_A_MQ_BROKER_URL"));
			connection = connectionFactory.createConnection(System.getenv("JBOSS_A_MQ_BROKER_USER"),
					System.getenv("JBOSS_A_MQ_BROKER_PASSWORD"));
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
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicName);
			MessageProducer messageProducer = session.createProducer(topic);
			TextMessage textMessage = session.createTextMessage(message);
			messageProducer.send(textMessage);
			session.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

}