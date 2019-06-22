package com.asyncapi.parser;

import de.dentrassi.asyncapi.MessageReference;

public class Channel {
	private String channel;
	private MessageReference publish;
	private MessageReference subscribe;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public MessageReference getPublish() {
		return publish;
	}

	public void setPublish(MessageReference publish) {
		this.publish = publish;
	}

	public MessageReference getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(MessageReference subscribe) {
		this.subscribe = subscribe;
	} 
	
}
