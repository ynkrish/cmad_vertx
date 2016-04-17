package com.krish.cmad;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class RestVerticleTest {

	private Vertx vertx;

	@Before
	public void setUp(TestContext context) throws Exception {
		vertx = Vertx.vertx();
		vertx.deployVerticle(RestResponderVerticle.class.getName(), context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void testMyApplication(TestContext context) {
		final Async async = context.async();

		vertx.createHttpClient().getNow(8080, "localhost", "/services/user/5710d74d577eea295c637145", response -> {
			response.handler(body -> {
				context.assertTrue(body.toString().contains("satkuppu"));
				async.complete();
			});
		});
		
		//NOTE: Without this close, the execution does not end and test fails due to TimeoutException
		//Not sure if this is correct given that tearDown is also doing a close
		vertx.close();
	}

}
