package com.estafet.microservices.api.project.burndown.jms;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.estafet.microservices.api.project.burndown.service.ProjectBurndownService;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class NewProjectConsumerTest {

	@InjectMocks
	NewProjectConsumer consumer = new NewProjectConsumer();
	
	@Mock
	ProjectBurndownService projectBurndownService;
	
	@Before
	public void before() throws Exception {
	}

	@Test
	public void testOnMessage() {
		fail("Not yet implemented");
	}

}
