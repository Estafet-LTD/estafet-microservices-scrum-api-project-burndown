package com.estafet.microservices.api.project.burndown.event;

import javax.persistence.OptimisticLockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.model.MessageEvent;

@Component
public class MessageEventHandler {

	@Autowired
	private MessageEventDAO messageEventDAO;

	@Transactional
	public boolean isValid(String topicName, String reference) {
		MessageEvent messageEvent = messageEventDAO.getMessageEvent(topicName);
		if (messageEvent == null || !messageEvent.getMessageReference().equals(reference)) {
			try {
				if (messageEvent == null) {
					messageEventDAO.create(new MessageEvent().setTopicId(topicName).setMessageReference(reference));
				} else {
					messageEventDAO.update(messageEvent);
				}
			} catch (OptimisticLockException e) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

}
