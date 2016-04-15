package com.krish.cmad;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class PublisherVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("PublisherVerticle started");
		vertx.eventBus().publish("Channel99", "Test message 1");
		System.out.println("start of PublisherVerticle done");
		//startFuture.complete();
	}

	@Override
	public void stop(Future startFuture) {
		System.out.println("PublisherVerticle stopped");
	}
	
	public static void main(String[] args) {
		
		System.out.println("In main method of Publisher Verticle ");
	    VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
	    Vertx vertx = Vertx.vertx(options);  
	    
/*	    vertx.deployVerticle("com.krish.cmad.SubscriberVerticle", new Handler<AsyncResult<String>>() {

			@Override
            public void handle(AsyncResult<String> event) {
				System.out.println("Subscribe Verticle complete ");	
				if (!event.failed()) {
					//Ensure that subscriber verticle is also started
					vertx.deployVerticle("com.krish.cmad.PublisherVerticle");
				}
				
            }
	    });*/
	    
	    //Same stuff as above but with lambda syntax
	    
	    vertx.deployVerticle("com.krish.cmad.SubscriberVerticle", val -> {
	    	vertx.deployVerticle("com.krish.cmad.PublisherVerticle", val2 -> {
	    	});
	    });
	    
	    
	    
	    //publish message
	    
	   //on cunsumer side 
/*	    vertx.eventBus().consumer("Channel99", message -> {
	    	System.out.println("Message Body :" + message.body());
	    });*/
	    
	    System.out.println("Deployed Publisher vertx");
    }
}