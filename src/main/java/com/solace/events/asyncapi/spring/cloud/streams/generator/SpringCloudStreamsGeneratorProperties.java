package com.solace.events.asyncapi.spring.cloud.streams.generator;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "scs")
public class SpringCloudStreamsGeneratorProperties {
	static final String SINK = "SINK";
	static final String SOURCE = "SOURCE";
	static final String PROCESSOR = "PROCESSOR";
	
	@NotBlank
	private String packageName;
	
	@NotBlank
	private String baseDir;
	
	@NotBlank
	private String scsType;
	
	@NotBlank
	private String asyncAPIfile;
	
	public String getAsyncAPIfile() {
		return asyncAPIfile;
	}
	public void setAsyncAPIfile(String asyncAPIfile) {
		this.asyncAPIfile = asyncAPIfile;
	}
	public String getScsType() {
		return scsType;
	}
	public void setScsType(String scsType) {
		this.scsType = scsType;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getBaseDir() {
		return baseDir;
	}
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

}
