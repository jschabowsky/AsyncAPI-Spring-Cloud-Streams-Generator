package com.solace.events.asyncapi.spring.cloud.streams.generator;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "scs")
public class SpringCloudStreamsGeneratorProperties {
		
	@NotBlank
	private String clientUsername="default";
	
	@NotBlank
	private String clientPassword="default";
	
	@NotBlank
	private String msgVpn="default";
	
	public String getMsgVpn() {
		return msgVpn;
	}
	public void setMsgVpn(String msgVpn) {
		this.msgVpn = msgVpn;
	}
	public String getClientUsername() {
		return clientUsername;
	}
	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}
	public String getClientPassword() {
		return clientPassword;
	}
	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}	
}
