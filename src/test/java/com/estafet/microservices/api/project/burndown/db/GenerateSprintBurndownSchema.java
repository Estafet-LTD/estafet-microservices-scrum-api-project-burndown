package com.estafet.microservices.api.project.burndown.db;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class GenerateSprintBurndownSchema {

	public static void main(String[] args) {
		final String persistenceUnitName = "build";
	    EntityManager em = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
	    em.close();
	    em.getEntityManagerFactory().close();
	  }
	
}
