package com.estafet.microservices.api.project.burndown.container.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class TopicProducer {

	Connection connection;
	MessageProducer messageProducer;
	Session session;

	public TopicProducer(String topicName) {
		try {
			removeTopic(topicName);
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

	public MBeanServerConnection connect() {
		try {
			String username = System.getenv("JBOSS_A_MQ_BROKER_USER");
			String password = System.getenv("JBOSS_A_MQ_BROKER_PASSWORD");
			Map env = new HashMap();
			String[] credentials = new String[] { username, password };
			env.put(JMXConnector.CREDENTIALS, credentials);
			JMXConnector connector = JMXConnectorFactory
					.newJMXConnector(new JMXServiceURL(System.getenv("JBOSS_A_MQ_BROKER_JMX_URL")), env);
			connector.connect();
			MBeanServerConnection connection = connector.getMBeanServerConnection();
			return connection;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void removeTopic(String topicName) {
		MBeanServerConnection conn = connect();
		String brokerNameQuery = "org.apache.activemq:BrokerName=localhost,Type=Broker";
		String removeTopicOperationName = "removeTopic";
		Object[] params = { topicName };
		String[] sig = { "java.lang.String" };
		doTopicCrud(conn, topicName, brokerNameQuery, removeTopicOperationName, params, sig, "removed");
	}

	private void doTopicCrud(MBeanServerConnection conn, String topicName, String brokerNameQuery, String operationName,
			Object[] params, String[] sig, String verb) {
		try {
			ObjectName brokerObjName = new ObjectName(brokerNameQuery);
			conn.invoke(brokerObjName, operationName, params, sig);
		} catch (MalformedObjectNameException | InstanceNotFoundException | MBeanException | ReflectionException
				| IOException e) {
			throw new RuntimeException(e);
		}
	}

}