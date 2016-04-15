package com.krish.cmad;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class HttpResponderVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("HttpResponderVerticle started");
		
	    HttpServer server = vertx.createHttpServer();
	    Router router = Router.router(vertx);
	    
	    router.get("/services/products/:name").handler(new Handler<RoutingContext>() {

			@Override
            public void handle(RoutingContext event) {
				String name = event.request().getParam("name");
				HttpServerResponse resp = event.response();
				resp.putHeader("content-type", "application/json");
				
			/*	try {
				Thread.sleep(5000);
				} catch (InterruptedException e) {
	                e.printStackTrace();
                }*/
				resp.end("\"Name\": " + name + "}");
            }
	    	
	    });
	    
	    //With labmdas instead of using above syntax	    
	    
	    router.get("/services/users/:id").handler(x -> {
	    	String id = x.request().getParam("id");
	    	HttpServerResponse response = x.response();
	    	response.putHeader("content-type", "application/json");
	    	
	    	//Important to end the response - else, you wont send response back to server
	    	response.end("{\"Name\": " + id + ", \"Age\": 38}");
	    });
	    
	    server.requestHandler(new Handler<HttpServerRequest>() {

	    	@Override
            public void handle(HttpServerRequest event) {
	            router.accept(event);
            }
	    	
	    }).listen(8080);
	    
	    
	    //below is again a JDK 8 syntax which is equivalent to above code - did not fully understand the
	    //router::accept - but it conveys the same as above implementation
	   // server.requestHandler(router::accept).listen(8080);
	  
	}

	@Override
	public void stop(Future startFuture) {
		System.out.println("HttpResponderVerticle stopped");
	}
	
	public static void main(String[] args) {
		
		System.out.println("In HttpResponderVerticle main method ");
	    VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
	    //Vertx vertx = Vertx.factory.vertx(options); 
	    Vertx vertx = Vertx.vertx(options);  //JDK 1.8 compliance level in project settings to get this to work - static method in interface
	    
	    System.out.println("Created HttpResponderVerticle vertx");
	    
	    vertx.deployVerticle("com.krish.cmad.HttpResponderVerticle");
	    
	    System.out.println("Deployed HttpResponderVerticle  vertx");
	    
    }
}