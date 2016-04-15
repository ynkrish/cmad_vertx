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
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


public class RestResponderVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("RestResponderVerticle started");
		
	    HttpServer server = vertx.createHttpServer();
	    Router router = Router.router(vertx);
	    
	    
	    //Static handler for resource
	    //By default will look for content under a folder called webroot under resources
	    router.route().handler(StaticHandler.create().setCachingEnabled(false)::handle);

	    
	    router.get("/services/user/:id").handler(x -> {
	    	String id = x.request().getParam("id");
	    	HttpServerResponse response = x.response();
	    	
	    	//TODO: For now, just doing this here, need to get this from some DB
	    	if (id == "Krish") {

	    		//Important to put this header in this place - if we send a header with content-type and dont have
	    		//content and just set the status - like in else, then, request gets stuck on browser
	    		response.putHeader("content-type", "application/json");
	    		User user = new User();
	    		user.setFirstName("Krish");
	    		user.setLastName("Y N");
	    		user.setAge(30);
	    		response.end(user.toJson());
	    	}
	    	else {
	    		response.setStatusCode(404);
	    		response.end();
	    	}
	    	
	    	System.out.println("Get success");
	    	
	    	
	    	//Important to end the response - else, you wont send response back to server
	    });
	    
	    
	    //Create a body hanlder to handle post body
	    router.route().handler(BodyHandler.create());
	    
	    
	    router.post("/services/user").handler(rc -> {
	    	String jSonString = rc.getBodyAsString(); //get JSON body as String
	    	//convert to object
	    	
	    	System.out.println("JSON String from POST " + jSonString);
	    	
	    	ObjectMapper mapper = new ObjectMapper();
	    	User user = null;
	    	try {
	    	  user = mapper.readValue(jSonString, User.class); 
	    	}
	    	catch (Exception ex) {
	    		ex.printStackTrace();
	    	}
	    	
	    	System.out.println(user);
	    	
	    	System.out.println("Post success");
	    	
	    	HttpServerResponse response = rc.response();
	    	response.setStatusCode(204);
	    	
	    	response.end();
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
		System.out.println("RestResponderVerticle stopped");
	}
	
	public static void main(String[] args) {
		
		System.out.println("In RestResponderVerticle main method ");
	    VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
	    //Vertx vertx = Vertx.factory.vertx(options); 
	    Vertx vertx = Vertx.vertx(options);  //JDK 1.8 compliance level in project settings to get this to work - static method in interface
	    
	    System.out.println("Created RestResponderVerticle vertx");
	    
	    vertx.deployVerticle("com.krish.cmad.RestResponderVerticle");
	    
	    System.out.println("Deployed RestResponderVerticle  vertx");
	    
    }
}