package com.estafet.microservices.api.project.burndown;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import io.opentracing.Tracer;

public class PropagatingTracingMessageConverter implements MessageConverter {

	private final MessageConverter messageConverter;
	private final SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();
	private final Tracer tracer;

	public PropagatingTracingMessageConverter(MessageConverter messageConverter, Tracer tracer) {
		this.messageConverter = messageConverter;
		this.tracer = tracer;
	}

	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		if (messageConverter != null) {
			return messageConverter.toMessage(object, session);
		}
		return simpleMessageConverter.toMessage(object, session);
	}

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		PropagatingTracingMessageUtils.buildFollowingSpan(message, tracer);
		if (messageConverter != null) {
			return messageConverter.fromMessage(message);
		}
		return simpleMessageConverter.fromMessage(message);
	}

}
