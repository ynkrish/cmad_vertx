package com.krish.cmad;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class ServicesFactory {
	
	
	private static ThreadLocal<Datastore> mongoTL = new ThreadLocal<Datastore>();
	
	/**
	 * Method to retrieve a mongo database client from the thread local storage
	 * @return
	 */
	public static Datastore getMongoDB(){
		if(mongoTL.get()==null){
			MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
			MongoClient mongoClient = new MongoClient(connectionString);	
			Morphia morphia = new Morphia();
			morphia.mapPackage("com.krish.cmad");
			Datastore datastore = morphia.createDatastore(mongoClient, "test");
			datastore.ensureIndexes();
			mongoTL.set(datastore);
			return datastore;
		}
		return mongoTL.get();
	}
	
	public static void main(String[] args) {
		
		Datastore store = getMongoDB();	
		
		
    	
		UserDTO dto = new UserDTO();
		dto.setFirst("Krishnan");
		dto.setLast("Yechchan");
		dto.setUserName("kyechcha");
		dto.setCompanyId("Cisco");
		dto.setCompanyId("CSCO");
		dto.setDeptId("12345");
		dto.setDeptName("SPG");
		dto.setEmail("kyechcha@cisco.com");
		dto.setPassword("Cisco_123");
		dto.setSubdomain("sub");
		
    	//Key<User> usr = store.save(dto.toModel());
    	
    	//System.out.println("Saved object to DB :" + usr.getId());  //5710cc12577eea32e80282b3
    	
    	ObjectId oid = new ObjectId("5710cc12577eea32e80282b3");
    	List<User> users = store.createQuery(User.class).field("id").equal(oid).asList();
    	for (User usr : users)
    		System.out.println(usr.getFirst() + " " + usr.getLast());
    	
    	System.out.println(users);
    }
	
}