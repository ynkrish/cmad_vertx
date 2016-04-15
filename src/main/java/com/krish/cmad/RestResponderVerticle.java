package com.krish.cmad;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class RestResponderVerticle extends AbstractVerticle {


	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("RestResponderVerticle started" + Thread.currentThread().getId());
		
    	//TODO: Should this be moved into a Worker node code ?
    	Datastore store = ServicesFactory.getMongoDB();
    	System.out.println("Created Store..");

		
	    HttpServer server = vertx.createHttpServer();
	    Router router = Router.router(vertx);
	    
	    router.get("/services/user/:id").handler(x -> {
	    	String id = x.request().getParam("id");
	    	HttpServerResponse response = x.response();
	    	
	    	ObjectId oid = null;
	    	oid = new ObjectId(id);
	    	List<User> users = store.createQuery(User.class).field("id").equal(oid).asList();
	    	
	    	System.out.println("Thread :" + + Thread.currentThread().getId() + " GET -> ID :" + id + " Users :" + users);
	    	
	    	if (users == null || users.isEmpty()) {
	    		response.setStatusCode(404).end("Not Found !!");
	    	}
	    	else {
	    		//Important to put this header in this place - if we send a header with content-type and dont have
	    		//content and just set the status - then, for a not found scenario, i.e. 404, browser gets stuck
	    		//maybe a bug in Vert.x ?
	    		response.putHeader("content-type", "application/json");
				UserDTO dto = new UserDTO().fillFromModel(users.get(0));
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.valueToTree(dto);
				response.setStatusCode(200).end(node.toString());  //Note:this wont be valid JSON yet. need to take care of this.
	    	}
	    	
	    	System.out.println("GET success " + + Thread.currentThread().getId());
	    	//Important to end the response - else, you wont send response back to server
	    });
	    
	    
	    //Create a body hanlder to handle post body, this
	    //retrieves the Body content so that other handler which might reuqire it
	    //can use the same. Note that route does not have a path, so this handles all
	    //requests currently
	    router.route().handler(BodyHandler.create());

	    //Handle POST request for Storing user
	    router.post("/services/user").handler(rc -> {
	    	String jSonString = rc.getBodyAsString(); //get JSON body as String
	    	//convert to object
	    	
	    	System.out.println("JSON String from POST " + jSonString);
	    	
	    	ObjectMapper mapper = new ObjectMapper();
	    	UserDTO dto = null;
	    	try {
	    	  dto = mapper.readValue(jSonString, UserDTO.class); 
	    	}
	    	catch (Exception ex) {
	    		ex.printStackTrace();
	    	}
	    	
	    	User user = dto.toModel();
	    	
	    	//Store to DB - how to handle exceptions here ?
	    	Key<User> usr = store.save(user);
	    	
	    	System.out.println("POST success, ID: " + usr.getId() + " Thread :" +  Thread.currentThread().getId());
	    	
	    	HttpServerResponse response = rc.response();
	    	response.setStatusCode(204).end("Data Saved");
	    	
	    });
	    
	    //Static handler for resource
	    
	    //By default will look for content under a folder called webroot under resources
	    //Note: If we set this above the GET handler, then, it kind of swallows those 
	    //requests as well as we dont have a route speciifed for static stuff
	    router.route().handler(StaticHandler.create().setCachingEnabled(false)::handle);

	    
	    
/*	    server.requestHandler(new Handler<HttpServerRequest>() {

	    	@Override
            public void handle(HttpServerRequest event) {
	            router.accept(event);
            }
	    	
	    }).listen(8080);
	    */
	    
	    //below is again a JDK 8 syntax which is equivalent to above code - did not fully understand the
	    //router::accept - but it conveys the same as above implementation
	    server.requestHandler(router::accept).listen(8080);
	  
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