package com.krish.cmad;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User {
	private String firstName;
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	private int age;

	@Override
	public String toString() {
		return toJson();
	}
	
	public String toJson() {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.valueToTree(this);
		return node.toString();
	}
}
