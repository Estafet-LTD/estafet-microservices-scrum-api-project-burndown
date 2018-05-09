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

	String topicName;
	Connection connection;

	public TopicProducer(String topicName) {
		this.topicName = topicName;
	}

	public void send(String message) {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getenv("JBOSS_A_MQ_BROKER_URL"));
			connection = connectionFactory.createConnection(System.getenv("JBOSS_A_MQ_BROKER_USER"),
					System.getenv("JBOSS_A_MQ_BROKER_PASSWORD"));
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicName);
			MessageProducer messageProducer = session.createProducer(topic);
			TextMessage textMessage = session.createTextMessage(message);
			messageProducer.send(textMessage);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
	}

}