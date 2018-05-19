package com.estafet.microservices.api.project.burndown.event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.model.MessageEvent;

@Repository
public class MessageEventDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	public MessageEvent getMessageEvent(String topic) {
		return entityManager.find(MessageEvent.class, topic);
	}
	
	@Transactional
	public void create(MessageEvent messageEvent) {
		entityManager.persist(messageEvent);
	}
	
	@Transactional
	public void update(MessageEvent messageEvent) {
		entityManager.merge(messageEvent);
	}

}
