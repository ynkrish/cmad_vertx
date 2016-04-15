package com.krish.cmad;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class SubscriberVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("SubscriberVerticle started");
		//on cunsumer side 
		
	    
		vertx.eventBus().consumer("Channel99", message -> {
			System.out.println("Message Body :" + message.body());
		});
	    startFuture.complete();
	}

	@Override
	public void stop(Future startFuture) {
		System.out.println("SubscriberVerticle stopped");
	}
	
	public static void main(String[] args) {
		
		System.out.println("In subscriberverticle main method ");
	    VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
	    //Vertx vertx = Vertx.factory.vertx(options); 
	    Vertx vertx = Vertx.vertx(options);  //JDK 1.8 compliance level in project settings to get this to work - static method in interface
	    
	    System.out.println("Created subscriberverticle vertx");
	    
	    vertx.deployVerticle("com.krish.cmad.SubscriberVerticle");
	    
	    System.out.println("Deployed subscriber vertx");
	    
		   
	    	    
    }
}