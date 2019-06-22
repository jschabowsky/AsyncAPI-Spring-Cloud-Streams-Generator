
package com.asyncapi.parser;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * An object representing a Server.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url",
    "description",
    "protocol",
    "protocolVersion",
    "variables",
    "baseChannel",
    "security"
})
public class Server {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("url")
    private String url;
    @JsonProperty("description")
    private String description;
    /**
     * The transfer protocol.
     * (Required)
     * 
     */
    @JsonProperty("protocol")
    @JsonPropertyDescription("The transfer protocol.")
    private String protocol;
    @JsonProperty("protocolVersion")
    private String protocolVersion;
    @JsonProperty("variables")
    private ServerVariables variables;
    @JsonProperty("baseChannel")
    private String baseChannel;
    @JsonProperty("security")
    private List<SecurityRequirement> security = new ArrayList<SecurityRequirement>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The transfer protocol.
     * (Required)
     * 
     */
    @JsonProperty("protocol")
    public String getProtocol() {
        return protocol;
    }

    /**
     * The transfer protocol.
     * (Required)
     * 
     */
    @JsonProperty("protocol")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @JsonProperty("protocolVersion")
    public String getProtocolVersion() {
        return protocolVersion;
    }

    @JsonProperty("protocolVersion")
    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @JsonProperty("variables")
    public ServerVariables getVariables() {
        return variables;
    }

    @JsonProperty("variables")
    public void setVariables(ServerVariables variables) {
        this.variables = variables;
    }

    @JsonProperty("baseChannel")
    public String getBaseChannel() {
        return baseChannel;
    }

    @JsonProperty("baseChannel")
    public void setBaseChannel(String baseChannel) {
        this.baseChannel = baseChannel;
    }

    @JsonProperty("security")
    public List<SecurityRequirement> getSecurity() {
        return security;
    }

    @JsonProperty("security")
    public void setSecurity(List<SecurityRequirement> security) {
        this.security = security;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Server.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("url");
        sb.append('=');
        sb.append(((this.url == null)?"<null>":this.url));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("protocol");
        sb.append('=');
        sb.append(((this.protocol == null)?"<null>":this.protocol));
        sb.append(',');
        sb.append("protocolVersion");
        sb.append('=');
        sb.append(((this.protocolVersion == null)?"<null>":this.protocolVersion));
        sb.append(',');
        sb.append("variables");
        sb.append('=');
        sb.append(((this.variables == null)?"<null>":this.variables));
        sb.append(',');
        sb.append("baseChannel");
        sb.append('=');
        sb.append(((this.baseChannel == null)?"<null>":this.baseChannel));
        sb.append(',');
        sb.append("security");
        sb.append('=');
        sb.append(((this.security == null)?"<null>":this.security));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.protocol == null)? 0 :this.protocol.hashCode()));
        result = ((result* 31)+((this.variables == null)? 0 :this.variables.hashCode()));
        result = ((result* 31)+((this.security == null)? 0 :this.security.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.protocolVersion == null)? 0 :this.protocolVersion.hashCode()));
        result = ((result* 31)+((this.baseChannel == null)? 0 :this.baseChannel.hashCode()));
        result = ((result* 31)+((this.url == null)? 0 :this.url.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Server) == false) {
            return false;
        }
        Server rhs = ((Server) other);
        return ((((((((this.protocol == rhs.protocol)||((this.protocol!= null)&&this.protocol.equals(rhs.protocol)))&&((this.variables == rhs.variables)||((this.variables!= null)&&this.variables.equals(rhs.variables))))&&((this.security == rhs.security)||((this.security!= null)&&this.security.equals(rhs.security))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.protocolVersion == rhs.protocolVersion)||((this.protocolVersion!= null)&&this.protocolVersion.equals(rhs.protocolVersion))))&&((this.baseChannel == rhs.baseChannel)||((this.baseChannel!= null)&&this.baseChannel.equals(rhs.baseChannel))))&&((this.url == rhs.url)||((this.url!= null)&&this.url.equals(rhs.url))));
    }

}
