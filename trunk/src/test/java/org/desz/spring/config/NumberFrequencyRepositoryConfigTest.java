package org.desz.spring.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.desz.numbertoword.repository.NumberFrequencyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@ComponentScan(basePackages = { "org.desz.spring.config",
		"org.desz.numbertoword.repository", "org.desz.numbertoword.service" })
public class NumberFrequencyRepositoryConfigTest {

	ApplicationContext ctx;

	@Before
	public void init() {
		ctx = new AnnotationConfigApplicationContext(
				NumberFrequencyRepositoryConfig.class);

	}

	@Test
	public void testConfigOk() {

		assertNotNull(ctx);
		assertTrue(ctx.containsBean("mongoTemplate"));
		assertNotNull(ctx.getBean(MongoTemplate.class));

		assertTrue(ctx.containsBean("numberFrequencyRepository"));
		assertNotNull(ctx.getBean(NumberFrequencyRepository.class));

	}

	@Test
	public void testMongoTemplate() {

		ctx.getBean(MongoTemplate.class).dropCollection("numberFrequency");
		ctx.getBean(MongoTemplate.class).createCollection("numberFrequency");
		assertTrue(ctx.getBean(MongoTemplate.class).collectionExists(
				"numberFrequency"));
	}

}
