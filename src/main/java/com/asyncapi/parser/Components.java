
package com.asyncapi.parser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * An object to hold a set of reusable objects for different aspects of the AsyncAPI Specification.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "schemas",
    "messages",
    "securitySchemes",
    "parameters",
    "correlationIds",
    "traits"
})
public class Components {

    /**
     * JSON objects describing schemas the API uses.
     * 
     */
    @JsonProperty("schemas")
    @JsonPropertyDescription("JSON objects describing schemas the API uses.")
    private Schemas schemas;
    /**
     * JSON objects describing the messages being consumed and produced by the API.
     * 
     */
    @JsonProperty("messages")
    @JsonPropertyDescription("JSON objects describing the messages being consumed and produced by the API.")
    private Messages messages;
    @JsonProperty("securitySchemes")
    private SecuritySchemes securitySchemes;
    /**
     * JSON objects describing re-usable channel parameters.
     * 
     */
    @JsonProperty("parameters")
    @JsonPropertyDescription("JSON objects describing re-usable channel parameters.")
    private Parameters parameters;
    @JsonProperty("correlationIds")
    private CorrelationIds correlationIds;
    @JsonProperty("traits")
    private Traits traits;

    /**
     * JSON objects describing schemas the API uses.
     * 
     */
    @JsonProperty("schemas")
    public Schemas getSchemas() {
        return schemas;
    }

    /**
     * JSON objects describing schemas the API uses.
     * 
     */
    @JsonProperty("schemas")
    public void setSchemas(Schemas schemas) {
        this.schemas = schemas;
    }

    /**
     * JSON objects describing the messages being consumed and produced by the API.
     * 
     */
    @JsonProperty("messages")
    public Messages getMessages() {
        return messages;
    }

    /**
     * JSON objects describing the messages being consumed and produced by the API.
     * 
     */
    @JsonProperty("messages")
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @JsonProperty("securitySchemes")
    public SecuritySchemes getSecuritySchemes() {
        return securitySchemes;
    }

    @JsonProperty("securitySchemes")
    public void setSecuritySchemes(SecuritySchemes securitySchemes) {
        this.securitySchemes = securitySchemes;
    }

    /**
     * JSON objects describing re-usable channel parameters.
     * 
     */
    @JsonProperty("parameters")
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * JSON objects describing re-usable channel parameters.
     * 
     */
    @JsonProperty("parameters")
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    @JsonProperty("correlationIds")
    public CorrelationIds getCorrelationIds() {
        return correlationIds;
    }

    @JsonProperty("correlationIds")
    public void setCorrelationIds(CorrelationIds correlationIds) {
        this.correlationIds = correlationIds;
    }

    @JsonProperty("traits")
    public Traits getTraits() {
        return traits;
    }

    @JsonProperty("traits")
    public void setTraits(Traits traits) {
        this.traits = traits;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Components.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("schemas");
        sb.append('=');
        sb.append(((this.schemas == null)?"<null>":this.schemas));
        sb.append(',');
        sb.append("messages");
        sb.append('=');
        sb.append(((this.messages == null)?"<null>":this.messages));
        sb.append(',');
        sb.append("securitySchemes");
        sb.append('=');
        sb.append(((this.securitySchemes == null)?"<null>":this.securitySchemes));
        sb.append(',');
        sb.append("parameters");
        sb.append('=');
        sb.append(((this.parameters == null)?"<null>":this.parameters));
        sb.append(',');
        sb.append("correlationIds");
        sb.append('=');
        sb.append(((this.correlationIds == null)?"<null>":this.correlationIds));
        sb.append(',');
        sb.append("traits");
        sb.append('=');
        sb.append(((this.traits == null)?"<null>":this.traits));
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
        result = ((result* 31)+((this.traits == null)? 0 :this.traits.hashCode()));
        result = ((result* 31)+((this.schemas == null)? 0 :this.schemas.hashCode()));
        result = ((result* 31)+((this.correlationIds == null)? 0 :this.correlationIds.hashCode()));
        result = ((result* 31)+((this.messages == null)? 0 :this.messages.hashCode()));
        result = ((result* 31)+((this.securitySchemes == null)? 0 :this.securitySchemes.hashCode()));
        result = ((result* 31)+((this.parameters == null)? 0 :this.parameters.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Components) == false) {
            return false;
        }
        Components rhs = ((Components) other);
        return (((((((this.traits == rhs.traits)||((this.traits!= null)&&this.traits.equals(rhs.traits)))&&((this.schemas == rhs.schemas)||((this.schemas!= null)&&this.schemas.equals(rhs.schemas))))&&((this.correlationIds == rhs.correlationIds)||((this.correlationIds!= null)&&this.correlationIds.equals(rhs.correlationIds))))&&((this.messages == rhs.messages)||((this.messages!= null)&&this.messages.equals(rhs.messages))))&&((this.securitySchemes == rhs.securitySchemes)||((this.securitySchemes!= null)&&this.securitySchemes.equals(rhs.securitySchemes))))&&((this.parameters == rhs.parameters)||((this.parameters!= null)&&this.parameters.equals(rhs.parameters))));
    }

}
