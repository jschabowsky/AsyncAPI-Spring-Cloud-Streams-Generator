package com.asyncapi.parser;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.dentrassi.asyncapi.MessageReference;
import de.dentrassi.asyncapi.type.TypeReference;

public class Message extends MessageReference{

	public Message(String name) {
		super(name);
		this.name = name;
		// TODO Auto-generated constructor stub
	}
	@JsonProperty("headers")
	private Map<String, String> headers;
	@JsonProperty("payload")
	private TypeReference payload;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("title")
	private String title;
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public TypeReference getPayload() {
		return payload;
	}
	public void setPayload(TypeReference payload) {
		this.payload = payload;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	} 
}
